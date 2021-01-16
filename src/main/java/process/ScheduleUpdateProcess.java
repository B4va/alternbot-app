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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * Process de récupération des données IUT.
 */
public class ScheduleUpdateProcess {

  private static final Logger LOGGER = LoggerUtils.buildLogger(ScheduleUpdateProcess.class);

  /**
   * Met à jour les données de la base en récupérant les données de l'IUT.
   */
  public void update() {
    List<Schedule> schedules = Model.readAll(Schedule.class);
    schedules.forEach(s -> {
      String data = fetchIcalData(s);
      List<VEvent> events = parseIcal(data, s);
      List<Session> sessions = mapSessions(events, s);
      sessions.forEach(this::saveSession);
    });
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
    return events;
  }

  private List<Session> mapSessions(List<VEvent> events, Schedule schedule) {
    AtomicInteger errorCount = new AtomicInteger();
    List<Session> sessions = events.stream().map(e -> {
      try {
        String name = e.getDescription().getValue();
        String location = nonNull(e.getLocation()) ? e.getLocation().getValue() : null;
        Date startDate = e.getStartDate().getDate();
        Date endDate = e.getEndDate().getDate();
        return new Session(name, null, location, startDate, startDate, endDate, schedule);
      } catch (NullPointerException ex) {
        errorCount.getAndIncrement();
        return null;
      }
    }).collect(Collectors.toList());
    if (errorCount.get() > 0) {
      LOGGER.warn("{} erreurs lors de la mise à jour de l'edt '{}'.", errorCount.get(), schedule.getPromotion());
    }
    return sessions;
  }

  private void saveSession(Session session) {
    /*
    TODO :
      si cours déjà enregistré au même horaire :
        - si identique, ne rien faire
        - si différent, modifier et envoyer publication alerte
      sinon :
        - enregistrer le cours
     */
  }

  public static void main(String[] args) {
    Schedule schedule = new Schedule("Test", "https://dptinfo.iutmetz.univ-lorraine.fr/lna/agendas/ical.php?ical=c3459ed54a02149");
    String data = new ScheduleUpdateProcess().fetchIcalData(schedule);
    System.out.println(data);
    List<VEvent> events = new ScheduleUpdateProcess().parseIcal(data, schedule);
    List<Session> sessions = new ScheduleUpdateProcess().mapSessions(events, schedule);
    System.exit(0);
  }
}
