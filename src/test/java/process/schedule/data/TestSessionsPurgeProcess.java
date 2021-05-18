package process.schedule.data;

import models.dao.ModelDAO;
import models.dao.Schedule;
import models.dao.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Calendar;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.DateUtils.*;

/**
 * Classe de test de {@link SessionsPurgeProcess}.
 */
public class TestSessionsPurgeProcess {

  private static final SessionsPurgeProcess PROCESS = new SessionsPurgeProcess();
  private static Session SESSION_TEST;
  private static final Schedule SCHEDULE = new Schedule("prom", "url");
  private static final String NAME_TEST = "CoursTest 1";
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
    if (nonNull(SESSION_TEST)) {
      Session session = ModelDAO.read(SESSION_TEST.getId(), Session.class);
      if (nonNull(session)) session.delete();
    }
  }

  @Test
  public void testPurgeAllUpdated_updated_session() throws ParseException {
    SESSION_TEST = new Session(NAME_TEST, TEACHER_TEST, LOCATION_TEST, stringToDate(DATE_TEST),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    SESSION_TEST.setUpdated(true);
    SESSION_TEST.setId(SESSION_TEST.create());
    int nbSessions = ModelDAO.readAll(Session.class).size();
    PROCESS.purgeAllUpdated();
    int updatedNbSessions = ModelDAO.readAll(Session.class).size();
    assertEquals(nbSessions - 1,updatedNbSessions);
  }

  @Test
  public void testPurgeAllUpdated_no_updated_session() throws ParseException {
    SESSION_TEST = new Session(NAME_TEST, TEACHER_TEST, LOCATION_TEST, stringToDate(DATE_TEST),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    SESSION_TEST.setId(SESSION_TEST.create());
    int nbSessions = ModelDAO.readAll(Session.class).size();
    PROCESS.purgeAllUpdated();
    int updatedNbSessions = ModelDAO.readAll(Session.class).size();
    assertEquals(nbSessions,updatedNbSessions);
  }

  @Test
  public void testPurgePastDaysThreshold_fail_invalid_days_threshold() throws ParseException {
    SESSION_TEST = new Session(NAME_TEST, TEACHER_TEST, LOCATION_TEST, stringToDate(DATE_TEST),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    SESSION_TEST.setId(SESSION_TEST.create());

    assertEquals(0, PROCESS.purgePastDaysThreshold(0));
    assertEquals(0, PROCESS.purgePastDaysThreshold(-1));
  }

  @Test
  public void testPurgePastDaysThreshold_no_sessions() {
    assertEquals(0, PROCESS.purgePastDaysThreshold(1));
  }

  @Test
  public void testPurgePastDaysThreshold_nothing_deleted() throws ParseException {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -3);
    SESSION_TEST = new Session(NAME_TEST, TEACHER_TEST, LOCATION_TEST, calendar.getTime(),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    SESSION_TEST.setId(SESSION_TEST.create());

    final int nbSessions = ModelDAO.readAll(Session.class).size();
    assertEquals(0, PROCESS.purgePastDaysThreshold(4));
    assertEquals(nbSessions, ModelDAO.readAll(Session.class).size());
  }

  @Test
  public void testPurgePastDaysThreshold_ok() throws ParseException {
    // Cours en avance de 1 jour sur le seuil, ne sera pas supprimé
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -29);
    SESSION_TEST = new Session(NAME_TEST, TEACHER_TEST, LOCATION_TEST, calendar.getTime(),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    SESSION_TEST.setId(SESSION_TEST.create());

    // Cours dont la date est égale au seuil, sera supprimé
    calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -30);
    Session session_test_2 = new Session(NAME_TEST, TEACHER_TEST, LOCATION_TEST, calendar.getTime(),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    session_test_2.setId(session_test_2.create());

    // Cours dont la date est au-delà du seuil, sera supprimé
    calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -31);
    Session session_test_3 = new Session(NAME_TEST, TEACHER_TEST, LOCATION_TEST, calendar.getTime(),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    session_test_3.setId(session_test_3.create());

    final int nbSessions = ModelDAO.readAll(Session.class).size();
    assertEquals(2, PROCESS.purgePastDaysThreshold(30));
    assertEquals(nbSessions - 2, ModelDAO.readAll(Session.class).size());
  }
}
