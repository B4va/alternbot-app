package process.data;

import models.Model;
import models.Schedule;
import models.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Classe de test de {@link SessionsPurgeProcess}.
 */
public class TestSessionsPurgeProcess {

  private static final SessionsPurgeProcess PROCESS = new SessionsPurgeProcess();
  private static Session SESSION_TEST;
  private static final Schedule SCHEDULE = new Schedule("prom", "url");
  private static final String NAME_TEST = "CoursTest";
  private static final String TEACHER_TEST = null;
  private static final String LOCATION_TEST = "A1";
  private static final String DATE_TEST = "01-01-2021";
  private static final String START_TEST = "10:00";
  private static final String END_TEST = "12:00";

  @BeforeAll
  public static void init() {
    SCHEDULE.setId(SCHEDULE.create());
  }

  @AfterAll
  public static void tearDown() {
    SCHEDULE.delete();
  }

  @AfterEach
  public void tearDownEach() {
    Session session = Model.read(SESSION_TEST.getId(), Session.class);
    if (nonNull(session)) session.delete();
  }

  @Test
  public void testPurge_updated_session() throws ParseException {
    SESSION_TEST = new Session(NAME_TEST, TEACHER_TEST, LOCATION_TEST, stringToDate(DATE_TEST),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    SESSION_TEST.setUpdated(true);
    SESSION_TEST.setId(SESSION_TEST.create());
    int nbSessions = Model.readAll(Session.class).size();
    PROCESS.purge();
    int updatedNbSessions = Model.readAll(Session.class).size();
    assertEquals(updatedNbSessions, nbSessions - 1);
  }

  @Test
  public void testPurge_no_updated_session() throws ParseException {
    SESSION_TEST = new Session(NAME_TEST, TEACHER_TEST, LOCATION_TEST, stringToDate(DATE_TEST),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    SESSION_TEST.setId(SESSION_TEST.create());
    int nbSessions = Model.readAll(Session.class).size();
    PROCESS.purge();
    int updatedNbSessions = Model.readAll(Session.class).size();
    assertEquals(updatedNbSessions, nbSessions);
  }
}
