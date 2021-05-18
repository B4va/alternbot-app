package process.schedule.data;

import models.dao.Schedule;
import models.dao.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Classe de test de {@link ScheduleExportSessionSelectionProcess}.
 */
public class TestScheduleExportSessionSelectionProcess {
  private static final ScheduleExportSessionSelectionProcess PROCESS = new ScheduleExportSessionSelectionProcess();
  private static final String DATE_ALL_MATCH = "18-01-2021";
  private static final String DATE_ONE_MATCH = "22-01-2021";
  private static final String DATE_NO_MATCH = "25-01-2021";

  private static Session SESSION_18_01;
  private static Session SESSION_20_01_1;
  private static Session SESSION_20_01_2;
  private static Session SESSION_22_01;

  private static final String SCHEDULE_PROMO = "LP Génie logiciel";
  private static final String SCHEDULE_URL = "url.test";
  private static Schedule INVALID_SCHEDULE;
  private static Schedule VALID_SCHEDULE;
  private static Schedule EMPTY_SCHEDULE;

  @BeforeAll
  public static void init() throws ParseException {
    INVALID_SCHEDULE = new Schedule(SCHEDULE_PROMO, SCHEDULE_URL);
    EMPTY_SCHEDULE = new Schedule(SCHEDULE_PROMO, SCHEDULE_URL);
    VALID_SCHEDULE = new Schedule(SCHEDULE_PROMO, SCHEDULE_URL);

    EMPTY_SCHEDULE.setSessions(Collections.emptySet());

    SESSION_18_01 = new Session("Arts martiaux", "Pai Mei", "F13", stringToDate("18-01-2021"), stringToTime("08:00"), stringToTime("13:00"), VALID_SCHEDULE);
    SESSION_20_01_1 = new Session("Chimie organique", "Marie Curie", "R86", stringToDate("20-01-2021"), stringToTime("11:00"), stringToTime("18:30"), VALID_SCHEDULE);
    SESSION_20_01_2 = new Session("Escrime", "Tesshin", "F13", stringToDate("20-01-2021"), stringToTime("14:30"), stringToTime("17:00"), VALID_SCHEDULE);
    SESSION_22_01 = new Session("Physique nucléaire", "Emmett Brown", "F13", stringToDate("22-01-2021"), stringToTime("01:15"), stringToTime("13:00"), VALID_SCHEDULE);

    // Les sessions sont volontairement dans le désordre afin de tester le tri du process
    VALID_SCHEDULE.setSessions(new HashSet<Session>(Arrays.asList(
      SESSION_20_01_2,
      SESSION_20_01_1,
      SESSION_22_01,
      SESSION_18_01
    )));
  }

  @Test
  public void testSelect_null_schedule() throws ParseException {
    assertNull(PROCESS.select(null, stringToDate(DATE_ALL_MATCH)));
  }

  @Test
  public void testSelect_null_date() {
    assertNull(PROCESS.select(VALID_SCHEDULE, null));
  }

  @Test
  public void testSelect_empty_schedule() throws ParseException {
    final List<Session> listeVide = new ArrayList<>();
    assertEquals(PROCESS.select(EMPTY_SCHEDULE, stringToDate(DATE_ALL_MATCH)),listeVide);
  }

  @Test
  public void testSelect_invalid_schedule() throws ParseException {
    final List<Session> listeVide = new ArrayList<>();
    assertEquals(PROCESS.select(INVALID_SCHEDULE, stringToDate(DATE_ALL_MATCH)),listeVide);
  }

  @Test
  public void testSelect_no_matching_session() throws ParseException {
    final List<Session> listeVide = new ArrayList<>();
    assertEquals(PROCESS.select(VALID_SCHEDULE, stringToDate(DATE_NO_MATCH)),listeVide);
  }

  @Test
  public void testSelect_one_matching_session() throws ParseException {
    final List<Session> result = PROCESS.select(VALID_SCHEDULE, stringToDate(DATE_ONE_MATCH));
    assertEquals(1, result.size());
    assertEquals(SESSION_22_01, result.get(0));
  }

  @Test
  public void testSelect_all_matching_session() throws ParseException {
    final List<Session> result = PROCESS.select(VALID_SCHEDULE, stringToDate(DATE_ALL_MATCH));
    assertEquals(4, result.size());
    assertAll(
      () -> assertEquals(SESSION_18_01, result.get(0)),
      () -> assertEquals(SESSION_20_01_1, result.get(1)),
      () -> assertEquals(SESSION_20_01_2, result.get(2)),
      () -> assertEquals(SESSION_22_01, result.get(3))
    );
  }
}
