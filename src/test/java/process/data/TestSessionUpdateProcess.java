package process.data;

import models.Model;
import models.Schedule;
import models.Session;
import models.business.SessionChange;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;

import java.text.ParseException;
import java.util.*;

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
  private static Session OLD_SESSION;
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
  private static final String NAME_OLD_SESSION = "CoursTest_OldSession";
  private static final String DATE_OLD_SESSION = "01-01-2030";

  @BeforeAll
  public static void init() throws ParseException {
    PROCESS = new SessionUpdateProcess();
    SCHEDULE.setId(SCHEDULE.create());
    SESSION_TEST = new Session(NAME_TEST, TEACHER_TEST, LOCATION_TEST, stringToDate(DATE_TEST),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    SESSION_TEST.setId(SESSION_TEST.create());
    OLD_SESSION = new Session(NAME_OLD_SESSION, TEACHER_TEST, LOCATION_TEST, stringToDate(DATE_OLD_SESSION),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    OLD_SESSION.setId(OLD_SESSION.create());
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
    OLD_SESSION.delete();
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
    OLD_SESSION.setUpdated(false);
    OLD_SESSION.update();
  }

  @Test
  public void testUpdateWithNew_new_session() {
    int nbSessions = Model.readAll(Session.class).size();
    List<SessionChange> changes = new ArrayList<>();
    Set<Session> oldSessions = Collections.singleton(SESSION_RDM);
    changes = PROCESS.updateWithNew(SESSION_TEST, oldSessions, changes);
    int updatedNbSessions = Model.readAll(Session.class).size();
    List<SessionChange> finalChanges = changes;
    assertAll(
      () -> assertEquals(updatedNbSessions, nbSessions + 1),
      () -> assertEquals(finalChanges.size(), 1),
      () -> assertEquals(finalChanges.get(0).getNewSession(), SESSION_TEST)
    );
  }

  @Test
  public void testUpdateWithNew_new_session_no_old() {
    int nbSessions = Model.readAll(Session.class).size();
    List<SessionChange> changes = new ArrayList<>();
    Set<Session> oldSessions = Collections.emptySet();
    changes = PROCESS.updateWithNew(SESSION_TEST, oldSessions, changes);
    int updatedNbSessions = Model.readAll(Session.class).size();
    List<SessionChange> finalChanges = changes;
    assertAll(
      () -> assertEquals(updatedNbSessions, nbSessions + 1),
      () -> assertEquals(finalChanges.size(), 1),
      () -> assertEquals(finalChanges.get(0).getNewSession(), SESSION_TEST)
    );
  }

  @Test
  public void testUpdateWithNew_overlapping_session_with_end() {
    int nbSessions = Model.readAll(Session.class).size();
    List<SessionChange> changes = new ArrayList<>();
    Set<Session> oldSessions = Collections.singleton(SESSION_OL_END);
    changes = PROCESS.updateWithNew(SESSION_TEST, oldSessions, changes);
    int updatedNbSessions = Model.readAll(Session.class).size();
    Session oldSession = Model.read(SESSION_OL_END.getId(), Session.class);
    List<SessionChange> finalChanges = changes;
    assertAll(
      () -> assertEquals(updatedNbSessions, nbSessions + 1),
      () -> assertTrue(oldSession.isUpdated()),
      () -> assertEquals(finalChanges.size(), 1),
      () -> assertEquals(finalChanges.get(0).getReplacedSessions().size(), 1),
      () -> assertEquals(SESSION_TEST, finalChanges.get(0).getNewSession()),
      () -> assertTrue(finalChanges.get(0).getReplacedSessions().contains(SESSION_OL_END))
    );
  }

  @Test
  public void testUpdateWithNew_overlapping_session_with_start() {
    int nbSessions = Model.readAll(Session.class).size();
    List<SessionChange> changes = new ArrayList<>();
    Set<Session> oldSessions = Collections.singleton(SESSION_OL_START);
    changes = PROCESS.updateWithNew(SESSION_TEST, oldSessions, changes);
    int updatedNbSessions = Model.readAll(Session.class).size();
    Session oldSession = Model.read(SESSION_OL_START.getId(), Session.class);
    List<SessionChange> finalChanges = changes;
    assertAll(
      () -> assertEquals(updatedNbSessions, nbSessions + 1),
      () -> assertTrue(oldSession.isUpdated()),
      () -> assertEquals(finalChanges.size(), 1),
      () -> assertEquals(finalChanges.get(0).getReplacedSessions().size(), 1),
      () -> assertEquals(SESSION_TEST, finalChanges.get(0).getNewSession()),
      () -> assertTrue(finalChanges.get(0).getReplacedSessions().contains(SESSION_OL_START))
    );
  }

  @Test
  public void testUpdateWithNew_multiple_overlapping() {
    int nbSessions = Model.readAll(Session.class).size();
    List<SessionChange> changes = new ArrayList<>();
    Set<Session> oldSessions = Sets.newSet(SESSION_OL_END, SESSION_OL_START);
    changes = PROCESS.updateWithNew(SESSION_TEST, oldSessions, changes);
    int updatedNbSessions = Model.readAll(Session.class).size();
    Session oldSession1 = Model.read(SESSION_OL_END.getId(), Session.class);
    Session oldSession2 = Model.read(SESSION_OL_START.getId(), Session.class);
    List<SessionChange> finalChanges = changes;
    assertAll(
      () -> assertEquals(updatedNbSessions, nbSessions + 1),
      () -> assertTrue(oldSession1.isUpdated()),
      () -> assertTrue(oldSession2.isUpdated()),
      () -> assertEquals(finalChanges.size(), 1),
      () -> assertEquals(finalChanges.get(0).getReplacedSessions().size(), 2),
      () -> assertEquals(SESSION_TEST, finalChanges.get(0).getNewSession()),
      () -> assertTrue(finalChanges.get(0).getReplacedSessions()
        .containsAll(Arrays.asList(SESSION_OL_START, SESSION_OL_END)))
    );
  }

  @Test
  public void testUpdateWithNew_already_saved() throws ParseException {
    Session alreadySaved = new Session(NAME_TEST, TEACHER_TEST, LOCATION_TEST, stringToDate(DATE_TEST),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    alreadySaved.setId(alreadySaved.create());
    int nbSessions = Model.readAll(Session.class).size();
    List<SessionChange> changes = new ArrayList<>();
    Set<Session> oldSessions = Collections.singleton(alreadySaved);
    changes = PROCESS.updateWithNew(SESSION_TEST, oldSessions, changes);
    int updatedNbSessions = Model.readAll(Session.class).size();
    List<SessionChange> finalChanges = changes;
    assertAll(
      () -> assertEquals(updatedNbSessions, nbSessions),
      () -> assertFalse(alreadySaved.isUpdated()),
      () -> assertTrue(finalChanges.isEmpty())
    );
  }

  @Test
  public void testUpdateFromOld_deleted() throws ParseException {
    List<SessionChange> changes = new ArrayList<>();
    PROCESS.updateFromOld(OLD_SESSION, Collections.emptyList(), changes);
    assertAll(
      () -> assertTrue(OLD_SESSION.isUpdated()),
      () -> assertFalse(changes.isEmpty())
    );
  }

  @Test
  public void testUpdateFromOld_deleted_but_past() throws ParseException {
    List<SessionChange> changes = new ArrayList<>();
    OLD_SESSION.setDate(stringToDate("01-01-2000"));
    PROCESS.updateFromOld(OLD_SESSION, Collections.emptyList(), changes);
    OLD_SESSION.setDate(stringToDate(DATE_OLD_SESSION));
    assertAll(
      () -> assertFalse(OLD_SESSION.isUpdated()),
      () -> assertTrue(changes.isEmpty())
    );
  }

  @Test
  public void testUpdateFromOld_not_changed() throws ParseException {
    List<SessionChange> changes = new ArrayList<>();
    Session newSession = new Session(NAME_OLD_SESSION, TEACHER_TEST, LOCATION_TEST, stringToDate(DATE_OLD_SESSION),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    List<Session> newSessions = Collections.singletonList(newSession);
    PROCESS.updateFromOld(OLD_SESSION, newSessions, changes);
    assertAll(
      () -> assertFalse(OLD_SESSION.isUpdated()),
      () -> assertTrue(changes.isEmpty())
    );
  }

  @Test
  public void testUpdateFromOld_already_in_session_changes() throws ParseException {
    List<SessionChange> changes = new ArrayList<>();
    Session alreadyChanged = new Session(NAME_OLD_SESSION, TEACHER_TEST, LOCATION_TEST, stringToDate(DATE_OLD_SESSION),
      stringToTime(START_TEST), stringToTime(END_TEST), SCHEDULE);
    changes.add(new SessionChange(new Session(), Collections.singletonList(alreadyChanged)));
    List<Session> newSessions = Collections.emptyList();
    PROCESS.updateFromOld(OLD_SESSION, newSessions, changes);
    assertAll(
      () -> assertFalse(OLD_SESSION.isUpdated()),
      () -> assertEquals(changes.size(), 1)
    );
  }
}
