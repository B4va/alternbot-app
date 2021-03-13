package process.schedule.data;

import models.dao.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Classe de test de {@link SessionSelection}.
 */
public class TestSessionSelection {
  private static SessionSelection PROCESS;
  private static List<Session> ORDERED_SESSIONS;
  private static List<Session> UNORDERED_SESSIONS;
  private static List<Session> SESSIONS_WITH_NULL_DATE;
  private static List<Session> SESSIONS_WITH_NULL_DATE_OK;
  private static List<Session> SESSIONS_WITH_NULL_TIME;
  private static List<Session> SESSIONS_WITH_NULL_TIME_OK;

  @BeforeAll
  public static void init() throws ParseException {
    PROCESS = new SessionSelection() {
      @Override
      public List<Session> orderSessionsByDateAndStart(List<Session> sessions) {
        return super.orderSessionsByDateAndStart(sessions);
      }
    };

    final Session session1 = new Session("Maths", "Prof 1", "F4",
      stringToDate("01-02-2021"), stringToTime("08:00"), stringToTime("10:00"), null);
    final Session session2 = new Session("CondProj", "Prof 2", "F4",
      stringToDate("01-02-2021"), stringToTime("10:05"), stringToTime("12:00"), null);
    final Session session3 = new Session("Maths", "Prof 1", "F4",
      stringToDate("02-02-2021"), stringToTime("08:00"), stringToTime("09:00"), null);
    final Session session_null_date = new Session("Maths", "Prof 1", "F4",
      null, stringToTime("08:00"), stringToTime("09:00"), null);
    final Session session_null_time = new Session("Maths", "Prof 1", "F4",
      stringToDate("02-02-2021"), null, null, null);

    UNORDERED_SESSIONS = new ArrayList<Session>() {{
      add(session2);
      add(session1);
      add(session3);
    }};
    ORDERED_SESSIONS = new ArrayList<Session>() {{
      add(session1);
      add(session2);
      add(session3);
    }};

    SESSIONS_WITH_NULL_DATE = new ArrayList<Session>() {{
      add(session3);
      add(session_null_date);
      add(session1);
    }};
    SESSIONS_WITH_NULL_DATE_OK = new ArrayList<Session>() {{
      add(session1);
      add(session3);
      add(session_null_date);
    }};

    SESSIONS_WITH_NULL_TIME = new ArrayList<Session>() {{
      add(session3);
      add(session_null_time);
      add(session1);
    }};
    SESSIONS_WITH_NULL_TIME_OK = new ArrayList<Session>() {{
      add(session1);
      add(session3);
      add(session_null_time);
    }};
  }

  @Test
  public void testOrderByDateAndStart_nullSessions() {
    assertNull(PROCESS.orderSessionsByDateAndStart(null));
  }

  @Test
  public void testOrderByDateAndStart_emptySessions() {
    final List<Session> sessions = new ArrayList<>();
    assertNull(PROCESS.orderSessionsByDateAndStart(sessions));
  }

  @Test
  public void testOrderByDateAndStart_nullDate() {
    assertEquals(SESSIONS_WITH_NULL_DATE_OK, PROCESS.orderSessionsByDateAndStart(SESSIONS_WITH_NULL_DATE));
  }

  @Test
  public void testOrderByDateAndStart_nullTime() {
    assertEquals(SESSIONS_WITH_NULL_TIME_OK, PROCESS.orderSessionsByDateAndStart(SESSIONS_WITH_NULL_TIME));
  }

  @Test
  public void testOrderByDateAndStart_unordered() {
    assertEquals(ORDERED_SESSIONS, PROCESS.orderSessionsByDateAndStart(UNORDERED_SESSIONS));
  }

  @Test
  public void testOrderByDateAndStart_alreadyOrdered() {
    assertEquals(ORDERED_SESSIONS, PROCESS.orderSessionsByDateAndStart(ORDERED_SESSIONS));
  }
}
