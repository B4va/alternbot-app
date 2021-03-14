package process.schedule.data;

import models.dao.Schedule;
import models.dao.Session;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static utils.DateUtils.datetimeToDate;
import static utils.DateUtils.datetimeToTime;

/**
 * Process de mapping de données ical.
 */
public class IcalMappingProcess {

  private static final Logger LOGGER = LoggerUtils.buildLogger(IcalMappingProcess.class);

  /**
   * Mappe des données au format ical.
   *
   * @param data     données au format ical
   * @param schedule emploi du temps associé aux données
   * @return liste de cours
   */
  public List<Session> map(String data, Schedule schedule) {
    if (isNull(data)) return Collections.emptyList();
    List<VEvent> icalData = parseIcal(data, schedule);
    return mapSessions(icalData, schedule);
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
      .collect(Collectors.toList());
  }

  private List<Session> mapSessions(List<VEvent> events, Schedule schedule) {
    AtomicInteger errorCount = new AtomicInteger();
    List<Session> sessions = events.stream()
      .map(e -> mapSession(e, schedule, errorCount))
      .collect(Collectors.toList());
    if (errorCount.get() > 0) {
      LOGGER.warn("{} informations manquantes lors de la mise à jour de l'edt '{}'.",
        errorCount.get(), schedule.getPromotion());
    }
    sessions.removeAll(Collections.singleton(null));
    return sessions.stream()
      .filter(s -> !s.isPast())
      .collect(Collectors.toList());
  }

  private Session mapSession(VEvent event, Schedule schedule, AtomicInteger errorCount) {
    try {
      String name = event.getDescription().getValue().split(" - ")[0];
      String location = nonNull(event.getLocation()) ? event.getLocation().getValue() : null;
      Date date, start, end;
      try {
        date = datetimeToDate(event.getStartDate().getDate());
        start = datetimeToTime(event.getStartDate().getDate());
        end = datetimeToTime(event.getEndDate().getDate());
      } catch (ParseException ex) {
        errorCount.getAndIncrement();
        return null;
      }
      return new Session(name, null, location, date, start, end, schedule);
    } catch (NullPointerException ex) {
      errorCount.getAndIncrement();
      return null;
    }
  }
}
