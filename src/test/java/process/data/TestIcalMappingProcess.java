package process.data;

import models.Schedule;
import models.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static utils.DateUtils.dateToString;
import static utils.DateUtils.timeToString;

/**
 * Classe de test de {@link IcalMappingProcess}.
 */
public class TestIcalMappingProcess {

  private static IcalMappingProcess PROCESS;
  private static final String NAME = "Informatique";
  private static final String LOCATION = "F06";
  private static final String DATE = "16-09-2030";
  private static final String START = "09:00";
  private static final String END = "10:00";
  private static final String ICAL_DATA = "BEGIN:VCALENDAR\n" +
    "BEGIN:VEVENT\n" +
    "SUMMARY:Informatique\n" +
    "DTSTART;TZID=Europe/Paris:20300916T090000\n" +
    "DTEND;TZID=Europe/Paris:20300916T100000\n" +
    "LOCATION:F06\n" +
    "DESCRIPTION:Informatique\n" +
    "TRANSP:OPAQUE\n" +
    "END:VEVENT\n" +
    "END:VCALENDAR\n";
  private static final String PAST_ICAL_DATA = "BEGIN:VCALENDAR\n" +
    "BEGIN:VEVENT\n" +
    "SUMMARY:Informatique\n" +
    "DTSTART;TZID=Europe/Paris:20200916T090000\n" +
    "DTEND;TZID=Europe/Paris:20200916T100000\n" +
    "LOCATION:F06\n" +
    "DESCRIPTION:Informatique\n" +
    "TRANSP:OPAQUE\n" +
    "END:VEVENT\n" +
    "END:VCALENDAR\n";
  private static final String ICAL_DATA_NO_DATE = "BEGIN:VCALENDAR\n" +
    "BEGIN:VEVENT\n" +
    "SUMMARY:Informatique\n" +
    "LOCATION:F06\n" +
    "DESCRIPTION:Informatique\n" +
    "TRANSP:OPAQUE\n" +
    "END:VEVENT\n" +
    "END:VCALENDAR\n";
  private static final String ICAL_DATA_NO_NAME = "BEGIN:VCALENDAR\n" +
    "BEGIN:VEVENT\n" +
    "DTSTART;TZID=Europe/Paris:20300916T090000\n" +
    "DTEND;TZID=Europe/Paris:20300916T100000\n" +
    "LOCATION:F06\n" +
    "TRANSP:OPAQUE\n" +
    "END:VEVENT\n" +
    "END:VCALENDAR\n";
  private static final String ICAL_DATA_NO_LOCATION = "BEGIN:VCALENDAR\n" +
    "BEGIN:VEVENT\n" +
    "SUMMARY:Informatique\n" +
    "DTSTART;TZID=Europe/Paris:20300916T090000\n" +
    "DTEND;TZID=Europe/Paris:20300916T100000\n" +
    "DESCRIPTION:Informatique\n" +
    "TRANSP:OPAQUE\n" +
    "END:VEVENT\n" +
    "END:VCALENDAR\n";
  private static final String INVALID_DATA = "invalid";
  private static Schedule SCHEDULE;

  @BeforeAll
  public static void init() {
    PROCESS = new IcalMappingProcess();
    SCHEDULE = new Schedule();
    SCHEDULE.setId(1);
  }

  @Test
  public void testMap_valid_data() {
    List<Session> sessions = PROCESS.map(ICAL_DATA, SCHEDULE);
    assertAll(
      () -> assertFalse(sessions.isEmpty()),
      () -> assertEquals(sessions.get(0).getName(), NAME),
      () -> assertEquals(sessions.get(0).getLocation(), LOCATION),
      () -> assertEquals(sessions.get(0).getSchedule().getId(), SCHEDULE.getId()),
      () -> assertEquals(dateToString(sessions.get(0).getDate()), DATE),
      () -> assertEquals(timeToString(sessions.get(0).getStart()), START),
      () -> assertEquals(timeToString(sessions.get(0).getEnd()), END)
    );
  }

  @Test
  public void testMap_invalid_data() {
    assertDoesNotThrow(() -> PROCESS.map(INVALID_DATA, SCHEDULE));
    assertTrue(PROCESS.map(INVALID_DATA, SCHEDULE).isEmpty());
  }

  @Test
  public void testMap_past_session() {
    assertDoesNotThrow(() -> PROCESS.map(PAST_ICAL_DATA, SCHEDULE));
    assertTrue(PROCESS.map(INVALID_DATA, SCHEDULE).isEmpty());
  }

  @Test
  public void testMap_session_no_date() {
    assertDoesNotThrow(() -> PROCESS.map(ICAL_DATA_NO_DATE, SCHEDULE));
    assertTrue(PROCESS.map(INVALID_DATA, SCHEDULE).isEmpty());
  }

  @Test
  public void testMap_session_no_name() {
    assertDoesNotThrow(() -> PROCESS.map(ICAL_DATA_NO_NAME, SCHEDULE));
    assertTrue(PROCESS.map(INVALID_DATA, SCHEDULE).isEmpty());
  }

  @Test
  public void testMap_session_no_location() {
    List<Session> sessions = PROCESS.map(ICAL_DATA_NO_LOCATION, SCHEDULE);
    assertAll(
      () -> assertFalse(sessions.isEmpty()),
      () -> assertEquals(sessions.get(0).getName(), NAME),
      () -> assertNull(sessions.get(0).getLocation()),
      () -> assertEquals(sessions.get(0).getSchedule().getId(), SCHEDULE.getId()),
      () -> assertEquals(dateToString(sessions.get(0).getDate()), DATE),
      () -> assertEquals(timeToString(sessions.get(0).getStart()), START),
      () -> assertEquals(timeToString(sessions.get(0).getEnd()), END)
    );
  }

  @Test
  public void testMap_null_data() {
    assertDoesNotThrow(() -> PROCESS.map(null, SCHEDULE));
    assertTrue(PROCESS.map(INVALID_DATA, SCHEDULE).isEmpty());
  }
}
