package process.schedule.publication;

import models.dao.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Classe de test de {@link DailyScheduleFormattingProcess}.
 */
public class TestDailyScheduleFormattingProcess {

  public static final DailyScheduleFormattingProcess PROCESS = new DailyScheduleFormattingProcess();
  private static Session SESSION1;
  private static Session SESSION2;
  private static final String DATE = "01-01-2021";
  private static final String START1 = "13:00";
  private static final String START2 = "14:00";
  private static final String END1 = "14:00";
  private static final String END2 = "15:00";
  private static final String SESSION1_NAME = "Session 1";
  private static final String SESSION2_NAME = "Session 2";
  private static final String SESSION1_TYPE = "TD";
  private static final String SESSION2_TYPE = "EXAM";
  private static final String TEACHER = "Prof";
  private static final String LOCATION = "A01";
  private static final String MESSAGE_NO_SESSION =
    "```\nEmploi du temps du 01-01-2021\n" +
      "\nAucun cours prévu ce jour." +
      "\n```";
  private static final String MESSAGE_ONE_SESSION =
    "```\nEmploi du temps du 01-01-2021\n" +
      "\n13:00 - 14:00 : Session 1 - Prof - A01 - TD" +
      "\n```";
  private static final String MESSAGE_NO_TEACHER_NO_LOCATION =
    "```\nEmploi du temps du 01-01-2021\n" +
      "\n14:00 - 15:00 : Session 2 - EXAM" +
      "\n```";
  private static final String MESSAGE_MULTIPLE_SESSIONS =
    "```\nEmploi du temps du 01-01-2021\n" +
      "\n13:00 - 14:00 : Session 1 - Prof - A01 - TD" +
      "\n14:00 - 15:00 : Session 2 - EXAM" +
      "\n```";

  @BeforeAll
  public static void init() throws ParseException {
    SESSION1 = new Session(SESSION1_NAME, TEACHER, LOCATION, stringToDate(DATE),
      stringToTime(START1), stringToTime(END1), null, SESSION1_TYPE);
    SESSION2 = new Session(SESSION2_NAME, null, null, stringToDate(DATE),
      stringToTime(START2), stringToTime(END2), null, SESSION2_TYPE);
  }

  @Test
  public void testFormat_no_session() throws ParseException {
    String message = PROCESS.format(Collections.emptyList(), stringToDate(DATE));
    assertEquals(MESSAGE_NO_SESSION, message);
  }

  @Test
  public void testFormat_one_session() throws ParseException {
    String message = PROCESS.format(Collections.singletonList(SESSION1), stringToDate(DATE));
    assertEquals(MESSAGE_ONE_SESSION, message);
  }

  @Test
  public void testFormat_one_session_no_teacher_no_location() throws ParseException {
    String message = PROCESS.format(Collections.singletonList(SESSION2), stringToDate(DATE));
    assertEquals(MESSAGE_NO_TEACHER_NO_LOCATION, message);
  }

  @Test
  public void testFormat_multiple_sessions() throws ParseException {
    List<Session> sessions = new ArrayList<>();
    sessions.add(SESSION1);
    sessions.add(SESSION2);
    String message = PROCESS.format(sessions, stringToDate(DATE));
    assertEquals(MESSAGE_MULTIPLE_SESSIONS, message);
  }
}
