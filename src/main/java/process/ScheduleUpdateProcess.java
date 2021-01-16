package process;

import models.Model;
import models.Schedule;
import models.Session;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static utils.DateUtils.datetimeToDate;
import static utils.DateUtils.datetimeToTime;

/**
 * Process de récupération des données IUT.
 */
public class ScheduleUpdateProcess {

  private static final Logger LOGGER = LoggerUtils.buildLogger(ScheduleUpdateProcess.class);

  /**
   * Met à jour les données de la base en récupérant les données de l'IUT.
   */
  public void update() {
    LOGGER.info("Lancement du process de mise à jour des données IUT.");
    List<Schedule> schedules = Model.readAll(Schedule.class);
    schedules.forEach(s -> {
      LOGGER.debug("Mise à jour des cours de l'edt #{} - promotion : {}", s.getId(), s.getPromotion());
      String data = fetchIcalData(s);
      if (nonNull(data)) {
        List<VEvent> events = parseIcal(data, s);
        List<Session> newSessions = mapSessions(events, s);
        Set<Session> oldSessions = s.getSessions();
        newSessions.forEach(ns -> updateSession(ns, oldSessions));
      }
      LOGGER.debug("EDT #{} : OK", s.getId());
    });
    LOGGER.debug("Purge des cours mis à jour.");
    purgeSessions();
    LOGGER.debug("Purge OK.");
    LOGGER.info("Process terminé.");
  }

  private String fetchIcalData(Schedule schedule) {
    String data = null;
    try {
      URL url = new URL(schedule.getUrl());
      try (Scanner sc = new Scanner(url.openStream())) {
        StringBuilder sb = new StringBuilder();
        while (sc.hasNext()) {
          sb.append(sc.nextLine()).append("\n");
        }
        data = sb.toString();
      } catch (IOException e) {
        LOGGER.warn("Impossible d'accéder à l'URL : {}", url);
      }
    } catch (MalformedURLException e) {
      LOGGER.warn("Mauvais format de l'URL : {}", schedule.getUrl());
    }
    return data;
  }

  private List<VEvent> parseIcal(String data, Schedule schedule) {
    CalendarBuilder builder = new CalendarBuilder();
    List<VEvent> events = new ArrayList<>();
    try {
      Calendar calendar = builder.build(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
      for (Object o : calendar.getComponents("VEVENT")) {
        events.add((VEvent) o);
      }
    } catch (ParserException | IOException e) {
      LOGGER.warn("Impossible de parser les infos récupérées via '{}'.", schedule.getUrl());
    }
    return events.stream()
      .filter(e -> nonNull(e.getStartDate()))
      .filter(e -> e.getStartDate().getDate().after(new Date()))
      .collect(Collectors.toList());
  }

  private List<Session> mapSessions(List<VEvent> events, Schedule schedule) {
    AtomicInteger errorCount = new AtomicInteger();
    List<Session> sessions = events.stream().map(e -> {
      try {
        String name = e.getDescription().getValue();
        String location = nonNull(e.getLocation()) ? e.getLocation().getValue() : null;
        Date date, start, end;
        try {
          date = datetimeToDate(e.getStartDate().getDate());
          start = datetimeToTime(e.getStartDate().getDate());
          end = datetimeToTime(e.getEndDate().getDate());
        } catch (ParseException ex) {
          errorCount.getAndIncrement();
          return null;
        }
        return new Session(name, null, location, date, start, end, schedule);
      } catch (NullPointerException ex) {
        errorCount.getAndIncrement();
        return null;
      }
    }).collect(Collectors.toList());
    if (errorCount.get() > 0) {
      LOGGER.warn("{} erreurs lors de la mise à jour de l'edt '{}'.", errorCount.get(), schedule.getPromotion());
    }
    sessions.removeAll(Collections.singleton(null));
    return sessions;
  }

  // todo : finaliser après merge du process d'envoi de l'alerte en cas de modif de l'edt
  private void updateSession(Session session, Set<Session> oldSessions) {
    if (!isSaved(session, oldSessions)) {
      // ScheduleUpdatePublicationProcess pub = new ScheduleUpdatePublicationProcess()
      List<Session> overlapped = oldSessions.stream()
        .filter(s -> isOverlapping(session, s))
        .collect(Collectors.toList());
      // pub.sendPublication(session, overlapped)
      overlapped.forEach(s -> {
        s.setUpdated(true);
        s.update();
      });
      session.create();
    }
  }

  private boolean isSaved(Session session, Set<Session> oldSessions) {
    return oldSessions.stream()
      .anyMatch(s -> session.getName().equals(s.getName()) &&
        ((isNull(session.getLocation()) && isNull(s.getLocation())) || session.getLocation().equals(s.getLocation())) &&
        ((isNull(session.getTeacher()) && isNull(s.getTeacher())) || session.getTeacher().equals(s.getTeacher())) &&
        session.getStart().equals(s.getStart()) &&
        session.getEnd().equals(s.getEnd()) &&
        session.getDate().equals(s.getDate()));
  }

  private boolean isOverlapping(Session newSession, Session oldSession) {
    return newSession.getDate().equals(oldSession.getDate()) &&
      (newSession.getStart().after(oldSession.getStart()) &&
        newSession.getStart().before(oldSession.getEnd())) ||
      (newSession.getEnd().after(oldSession.getDate()) &&
        newSession.getEnd().before(oldSession.getEnd()));
  }

  // todo : PERFORMANCE - purge via une requête SQL unique
  private void purgeSessions() {
    List<Session> sessions = Model.readAll(Session.class);
    sessions.stream()
      .filter(Session::isUpdated)
      .forEach(Session::delete);
  }

  public static void main(String[] args) {
//    new Schedule("Test", "https://dptinfo.iutmetz.univ-lorraine.fr/lna/agendas/ical.php?ical=c3459ed54a02149").create();
    new ScheduleUpdateProcess().update();
  }
}
