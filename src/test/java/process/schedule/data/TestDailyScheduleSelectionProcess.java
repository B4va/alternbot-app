package process.schedule.data;

import models.dao.Schedule;
import models.dao.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static utils.DateUtils.stringToDate;

/**
 * Classe de test de {@link DailyScheduleSelectionProcess}.
 */
public class TestDailyScheduleSelectionProcess {

  private static final DailyScheduleSelectionProcess PROCESS = new DailyScheduleSelectionProcess();
  private static final Schedule SCHEDULE = new Schedule();
  private static Date DATE;
  private static Session CORRESPONDING_SESSION_1;
  private static final int CS_1_ID = 1;
  private static Session CORRESPONDING_SESSION_2;
  private static final int CS_2_ID = 2;
  private static Session PAST_SESSION;
  private static final int PS_ID = 3;
  private static Session FUTURE_SESSION;
  private static final int FS_ID = 4;
  private static final String REQ_DATE = "01-01-2021";
  private static final String PAST_DATE = "01-01-2020";
  private static final String FUTURE_DATE = "01-01-2022";

  @BeforeAll
  public static void init() throws ParseException {
    DATE = stringToDate(REQ_DATE);
    CORRESPONDING_SESSION_1 = new Session(null, null, null, stringToDate(REQ_DATE),
      null, null, null);
    CORRESPONDING_SESSION_1.setId(CS_1_ID);
    CORRESPONDING_SESSION_2 = new Session(null, null, null, stringToDate(REQ_DATE),
      null, null, null);
    CORRESPONDING_SESSION_2.setId(CS_2_ID);
    PAST_SESSION = new Session(null, null, null, stringToDate(PAST_DATE),
      null, null, null);
    PAST_SESSION.setId(PS_ID);
    FUTURE_SESSION = new Session(null, null, null, stringToDate(FUTURE_DATE),
      null, null, null);
    FUTURE_SESSION.setId(FS_ID);
  }

  @Test
  public void testSelect_no_corresponding_session() {
    Set<Session> sessions = new HashSet<>();
    sessions.add(PAST_SESSION);
    sessions.add(FUTURE_SESSION);
    SCHEDULE.setSessions(sessions);
    List<Session> selection = PROCESS.select(SCHEDULE, DATE);
    assertTrue(selection.isEmpty());
  }

  @Test
  public void testSelect_no_session() {
    Set<Session> sessions = Collections.emptySet();
    SCHEDULE.setSessions(sessions);
    List<Session> selection = PROCESS.select(SCHEDULE, DATE);
    assertTrue(selection.isEmpty());
  }

  @Test
  public void testSelect_corresponding_session() {
    Set<Session> sessions = new HashSet<>();
    sessions.add(PAST_SESSION);
    sessions.add(FUTURE_SESSION);
    sessions.add(CORRESPONDING_SESSION_1);
    SCHEDULE.setSessions(sessions);
    List<Session> selection = PROCESS.select(SCHEDULE, DATE);
    assertAll(
      () -> assertEquals(1, selection.size()),
      () -> assertEquals(CS_1_ID,selection.get(0).getId())
    );
  }

  @Test
  public void testSelect_corresponding_sessions() {
    Set<Session> sessions = new HashSet<>();
    sessions.add(PAST_SESSION);
    sessions.add(FUTURE_SESSION);
    sessions.add(CORRESPONDING_SESSION_1);
    sessions.add(CORRESPONDING_SESSION_2);
    SCHEDULE.setSessions(sessions);
    List<Session> selection = PROCESS.select(SCHEDULE, DATE);
    assertAll(
      () -> assertEquals(2, selection.size()),
      () -> assertTrue(selection.stream().anyMatch(s -> s.getId() == CS_1_ID)),
      () -> assertTrue(selection.stream().anyMatch(s -> s.getId() == CS_2_ID))
    );
  }
}
