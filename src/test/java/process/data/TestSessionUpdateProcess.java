package process.data;

import models.Model;
import models.Schedule;
import models.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Classe de test de {@link SessionUpdateProcess}.
 */
public class TestSessionUpdateProcess {

  private static SessionUpdateProcess PROCESS;
  private static Session SESSION_TEST;
  private static Session SESSION_RDM;
  private static Session SESSION_OL_END;
  private static Session SESSION_OL_START;
  private static final Schedule SCHEDULE = new Schedule("prom", "url");
  private static final String NAME_TEST = "CoursTest_TestSessionUpdateProcess";
  private static final String TEACHER_TEST = null;
  private static final String LOCATION_TEST = "A1";
  private static final String DATE_TEST = "01-01-2021";
  private static final String START_TEST = "10:00";
  private static final String END_TEST = "12:00";
  private static final String NAME_RDM = "CoursTest_TestSessionUpdateProcess_rdm";
  private static final String TEACHER_RDM = null;
  private static final String LOCATION_RDM = "A0";
  private static final String DATE_RDM = "01-01-2000";
  private static final String START_RDM = "10:00";
  private static final String END_RDM = "12:00";
  private static final String NAME_OL_END = "CoursTest_TestSessionUpdateProcess_ol_end";
  private static final String TEACHER_OL_END = null;
  private static final String LOCATION_OL_END = "A2";
  private static final String DATE_OL_END = "01-01-2021";
  private static final String START_OL_END = "11:00";
  private static final String END_OL_END = "13:00";
  private static final String NAME_OL_START = "CoursTest_TestSessionUpdateProcess_ol_start";
  private static final String TEACHER_OL_START = null;
  private static final String LOCATION_OL_START = "A3";
  private static final String DATE_OL_START = "01-01-2021";
  private static final String START_OL_START = "09:00";
  private static final String END_OL_START = "11:00";

  @BeforeAll
  public static void init() throws ParseException {
    PROCESS = new SessionUpdateProcess();
    SCHEDULE.setId(SCHEDULE.create());
    SESSION_TEST = new Session(NAME_TEST, TEACHER_TEST, LOCATION_TEST, stringToDate(DATE_TEST),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    SESSION_TEST.setId(SESSION_TEST.create());
    SESSION_RDM = new Session(NAME_RDM, TEACHER_RDM, LOCATION_RDM, stringToDate(DATE_RDM),
      stringToTime(START_RDM), stringToTime(END_RDM), SCHEDULE);
    SESSION_RDM.setId(SESSION_RDM.create());
    SESSION_OL_END = new Session(NAME_OL_END, TEACHER_OL_END, LOCATION_OL_END, stringToDate(DATE_OL_END),
      stringToTime(START_OL_END), stringToTime(END_OL_END), SCHEDULE);
    SESSION_OL_END.setId(SESSION_OL_END.create());
    SESSION_OL_START = new Session(NAME_OL_START, TEACHER_OL_START, LOCATION_OL_START, stringToDate(DATE_OL_START),
      stringToTime(START_OL_START), stringToTime(END_OL_START), SCHEDULE);
    SESSION_OL_START.setId(SESSION_OL_START.create());
  }

  @AfterAll
  public static void tearDown() {
    SESSION_TEST.delete();
    SESSION_RDM.delete();
    SESSION_OL_END.delete();
    SESSION_OL_START.delete();
    SCHEDULE.delete();
  }

  @BeforeEach
  public void initEach() {
    SESSION_RDM.update();
    SESSION_OL_END.update();
    SESSION_OL_START.update();
    List<Session> sessions = Model.readAll(Session.class);
    sessions.stream()
      .filter(s -> s.getName().equals(NAME_TEST))
      .forEach(Model::delete);
  }

  @Test
  public void testUpdate_new_session() {
    int nbSessions = Model.readAll(Session.class).size();
    Set<Session> oldSessions = Collections.singleton(SESSION_RDM);
    PROCESS.update(SESSION_TEST, oldSessions);
    int updatedNbSessions = Model.readAll(Session.class).size();
    Session oldSession = Model.read(SESSION_RDM.getId(), Session.class);
    assertAll(
      () -> assertEquals(updatedNbSessions, nbSessions + 1),
      () -> assertFalse(oldSession.isUpdated())
    );
  }

  @Test
  public void testUpdate_new_session_no_old() {
    int nbSessions = Model.readAll(Session.class).size();
    Set<Session> oldSessions = Collections.emptySet();
    PROCESS.update(SESSION_TEST, oldSessions);
    int updatedNbSessions = Model.readAll(Session.class).size();
    assertEquals(updatedNbSessions, nbSessions + 1);
  }

  @Test
  public void testUpdate_overlapping_session_with_end() {
    int nbSessions = Model.readAll(Session.class).size();
    Set<Session> oldSessions = Collections.singleton(SESSION_OL_END);
    PROCESS.update(SESSION_TEST, oldSessions);
    int updatedNbSessions = Model.readAll(Session.class).size();
    Session oldSession = Model.read(SESSION_OL_END.getId(), Session.class);
    assertAll(
      () -> assertEquals(updatedNbSessions, nbSessions + 1),
      () -> assertTrue(oldSession.isUpdated())
    );
  }

  @Test
  public void testUpdate_overlapping_session_with_start() {
    int nbSessions = Model.readAll(Session.class).size();
    Set<Session> oldSessions = Collections.singleton(SESSION_OL_START);
    PROCESS.update(SESSION_TEST, oldSessions);
    int updatedNbSessions = Model.readAll(Session.class).size();
    Session oldSession = Model.read(SESSION_OL_START.getId(), Session.class);
    assertAll(
      () -> assertEquals(updatedNbSessions, nbSessions + 1),
      () -> assertTrue(oldSession.isUpdated())
    );
  }

  @Test
  public void testUpdate_multiple_overlapping() {
    int nbSessions = Model.readAll(Session.class).size();
    Set<Session> oldSessions = Sets.newSet(SESSION_OL_END, SESSION_OL_START);
    PROCESS.update(SESSION_TEST, oldSessions);
    int updatedNbSessions = Model.readAll(Session.class).size();
    Session oldSession1 = Model.read(SESSION_OL_END.getId(), Session.class);
    Session oldSession2 = Model.read(SESSION_OL_START.getId(), Session.class);
    assertAll(
      () -> assertEquals(updatedNbSessions, nbSessions + 1),
      () -> assertTrue(oldSession1.isUpdated()),
      () -> assertTrue(oldSession2.isUpdated())
    );
  }

  @Test
  public void testUpdate_already_saved() throws ParseException {
    Session alreadySaved = new Session(NAME_TEST, TEACHER_TEST, LOCATION_TEST, stringToDate(DATE_TEST),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    alreadySaved.setId(alreadySaved.create());
    int nbSessions = Model.readAll(Session.class).size();
    Set<Session> oldSessions = Collections.singleton(alreadySaved);
    PROCESS.update(SESSION_TEST, oldSessions);
    int updatedNbSessions = Model.readAll(Session.class).size();
    assertAll(
      () -> assertEquals(updatedNbSessions, nbSessions),
      () -> assertFalse(alreadySaved.isUpdated())
    );
  }
}
