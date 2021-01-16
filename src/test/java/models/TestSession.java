package models;

import org.junit.jupiter.api.*;

import javax.persistence.PersistenceException;
import java.text.ParseException;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.*;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Classe de test de {@link Session}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestSession implements TestModel {

  private static int ID_SESSION;
  private static int ID_SCHEDULE;
  private static Session SESSION;
  private static Schedule SCHEDULE;
  private static final String PROMOTION_TEST = "promotion test";
  private static final String URL_TEST = "url.com";
  private static final String UPDATED_NAME = "Updated name";
  private static final String DATE = "01-01-2021";
  private static final String START_TIME = "14:00";
  private static final String END_TIME = "15:00";
  private static final String LOCATION = "A01";
  private static final String TEACHER = "M. Prof";
  private static final String NAME = "Info";

  @AfterAll
  public static void tearDown() {
    SESSION = Model.read(ID_SESSION, Session.class);
    if (nonNull(SESSION)) {
      SESSION.delete();
    }
    SCHEDULE = Model.read(ID_SCHEDULE, Schedule.class);
    if (nonNull(SCHEDULE)) {
      SCHEDULE.delete();
    }
  }

  @Test
  @Order(1)
  @Override
  public void testCreate() {
    SCHEDULE = new Schedule(PROMOTION_TEST, URL_TEST);
    ID_SCHEDULE = SCHEDULE.create();
    try {
      SESSION = new Session(NAME, TEACHER, LOCATION, stringToDate(DATE),
        stringToTime(START_TIME), stringToTime(END_TIME), Schedule.read(ID_SCHEDULE, Schedule.class));
    } catch (ParseException e) {
      fail();
    }
    ID_SESSION = SESSION.create();
    List<Session> sessions = Model.readAll(Session.class);
    assertTrue(sessions.stream().anyMatch(s -> s.getId() == ID_SESSION));
  }

  @Test
  @Order(2)
  public void testCreate_name_null() {
    Session session = null;
    try {
      session = new Session(NAME, TEACHER, LOCATION, stringToDate(DATE),
        stringToTime(START_TIME), stringToTime(END_TIME), Schedule.read(ID_SCHEDULE, Schedule.class));
    } catch (ParseException e) {
      fail();
    }
    session.setName(null);
    assertThrows(PersistenceException.class, session::create);
  }

  @Test
  @Order(3)
  public void testCreate_teacher_null() {
    Session session = null;
    try {
      session = new Session(NAME, TEACHER, LOCATION, stringToDate(DATE),
        stringToTime(START_TIME), stringToTime(END_TIME), Schedule.read(ID_SCHEDULE, Schedule.class));
    } catch (ParseException e) {
      fail();
    }
    session.setTeacher(null);
    assertDoesNotThrow(session::create);
    session.delete();
  }

  @Test
  @Order(4)
  public void testCreate_location_null() {
    Session session = null;
    try {
      session = new Session(NAME, TEACHER, LOCATION, stringToDate(DATE),
        stringToTime(START_TIME), stringToTime(END_TIME), Schedule.read(ID_SCHEDULE, Schedule.class));
    } catch (ParseException e) {
      fail();
    }
    session.setLocation(null);
    assertDoesNotThrow(session::create);
    session.delete();
  }

  @Test
  @Order(5)
  public void testCreate_date_null() {
    Session session = null;
    try {
      session = new Session(NAME, TEACHER, LOCATION, stringToDate(DATE),
        stringToTime(START_TIME), stringToTime(END_TIME), Schedule.read(ID_SCHEDULE, Schedule.class));
    } catch (ParseException e) {
      fail();
    }
    session.setDate(null);
    assertThrows(PersistenceException.class, session::create);
  }

  @Test
  @Order(6)
  public void testCreate_start_null() {
    Session session = null;
    try {
      session = new Session(NAME, TEACHER, LOCATION, stringToDate(DATE),
        stringToTime(START_TIME), stringToTime(END_TIME), Schedule.read(ID_SCHEDULE, Schedule.class));
    } catch (ParseException e) {
      fail();
    }
    session.setStart(null);
    assertThrows(PersistenceException.class, session::create);
  }

  @Test
  @Order(7)
  public void testCreate_end_null() {
    Session session = null;
    try {
      session = new Session(NAME, TEACHER, LOCATION, stringToDate(DATE),
        stringToTime(START_TIME), stringToTime(END_TIME), Schedule.read(ID_SCHEDULE, Schedule.class));
    } catch (ParseException e) {
      fail();
    }
    session.setEnd(null);
    assertThrows(PersistenceException.class, session::create);
  }

  @Test
  @Order(8)
  public void testCreate_schedule_null() {
    Session session = null;
    try {
      session = new Session(NAME, TEACHER, LOCATION, stringToDate(DATE),
        stringToTime(START_TIME), stringToTime(END_TIME), Schedule.read(ID_SCHEDULE, Schedule.class));
    } catch (ParseException e) {
      fail();
    }
    session.setSchedule(null);
    assertThrows(PersistenceException.class, session::create);
  }

  @Test
  @Order(9)
  @Override
  public void testRead() {
    Session session = Session.read(ID_SESSION, Session.class);
    assertAll(
      () -> assertNotNull(session),
      () -> assertEquals(session.getName(), SESSION.getName()),
      () -> assertEquals(session.getTeacher(), SESSION.getTeacher()),
      () -> assertEquals(session.getLocation(), SESSION.getLocation()),
      () -> assertEquals(session.getSchedule().getId(), SESSION.getSchedule().getId()),
      () -> assertEquals(session.getDate(), SESSION.getDate()),
      () -> assertEquals(session.getStart(), SESSION.getStart()),
      () -> assertEquals(session.getEnd(), SESSION.getEnd())
    );
  }

  @Test
  @Order(10)
  public void testUpdate_name_null() {
    Session session = Session.read(ID_SESSION, Session.class);
    session.setName(null);
    assertThrows(PersistenceException.class, session::update);
  }

  @Test
  @Order(11)
  public void testUpdate_teacher_null() {
    Session session = Session.read(ID_SESSION, Session.class);
    session.setTeacher(null);
    assertDoesNotThrow(session::update);
  }

  @Test
  @Order(12)
  public void testUpdate_location_null() {
    Session session = Session.read(ID_SESSION, Session.class);
    session.setLocation(null);
    assertDoesNotThrow(session::update);
  }

  @Test
  @Order(13)
  public void testUpdate_schedule_null() {
    Session session = Session.read(ID_SESSION, Session.class);
    session.setSchedule(null);
    assertThrows(PersistenceException.class, session::update);
  }

  @Test
  @Order(14)
  public void testUpdate_date_null() {
    Session session = Session.read(ID_SESSION, Session.class);
    session.setDate(null);
    assertThrows(PersistenceException.class, session::update);
  }

  @Test
  @Order(15)
  public void testUpdate_start_null() {
    Session session = Session.read(ID_SESSION, Session.class);
    session.setStart(null);
    assertThrows(PersistenceException.class, session::update);
  }

  @Test
  @Order(16)
  public void testUpdate_end_null() {
    Session session = Session.read(ID_SESSION, Session.class);
    session.setEnd(null);
    assertThrows(PersistenceException.class, session::update);
  }

  @Test
  @Order(17)
  @Override
  public void testUpdate() {
    SESSION.setName(UPDATED_NAME);
    SESSION.update();
    SESSION = Model.read(ID_SESSION, Session.class);
    assertEquals(SESSION.getName(), UPDATED_NAME);
  }

  @Test
  @Order(18)
  public void testDelete_schedule_with_associated_sessions() {
    assertThrows(PersistenceException.class, SCHEDULE::delete);
  }

  @Test
  @Order(19)
  @Override
  public void testDelete() {
    SESSION.delete();
    Session session = Model.read(ID_SESSION, Session.class);
    assertNull(session);
  }
}
