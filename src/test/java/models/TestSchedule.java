package models;

import models.dao.ModelDAO;
import models.dao.Schedule;
import models.dao.Server;
import models.dao.Session;
import org.junit.jupiter.api.*;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test de {@link Schedule}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestSchedule implements TestModel {

  private static int ID_SESSION;
  private static int ID_SERVER;
  private static int ID_SCHEDULE;
  private static Schedule SCHEDULE;
  private static final String PROMOTION_TEST = "Promotion test";
  private static final String URL_TEST = "url.com";
  private static final String PROMOTION_UPDATE = "Promotion mise à jour";
  private static final String URL_UPDATE = "url.update.com";
  public static final String REF_SERVER = "0132456789";
  public static final String SESSION_NAME = "session";
  public static final String SESSION_TEACHER = "prof";
  public static final String SESSION_LOCATION = "location";
  public static final String SESSION_TYPE = "EXAM";

  @AfterAll
  public static void tearDown() {
    Session session = ModelDAO.read(ID_SESSION, Session.class);
    if (nonNull(session)) session.delete();
    Server server = ModelDAO.read(ID_SERVER, Server.class);
    if (nonNull(server)) server.delete();
    SCHEDULE = ModelDAO.read(ID_SCHEDULE, Schedule.class);
    if (nonNull(SCHEDULE)) SCHEDULE.delete();
  }

  @Test
  @Order(1)
  @Override
  public void testCreate() {
    SCHEDULE = new Schedule(PROMOTION_TEST, URL_TEST);
    ID_SCHEDULE = SCHEDULE.create();
    List<Schedule> schedules = ModelDAO.readAll(Schedule.class);
    assertTrue(schedules.stream().anyMatch(s -> s.getId() == ID_SCHEDULE));
  }

  @Test
  @Order(2)
  public void testCreate_promotion_null() {
    Schedule schedule = new Schedule();
    schedule.setUrl(URL_TEST);
    assertThrows(PersistenceException.class, schedule::create);
  }

  @Test
  @Order(2)
  public void testCreate_url_null() {
    Schedule schedule = new Schedule();
    schedule.setPromotion(PROMOTION_TEST);
    assertThrows(PersistenceException.class, schedule::create);
  }

  @Test
  @Order(3)
  @Override
  public void testRead() {
    Schedule schedule = ModelDAO.read(ID_SCHEDULE, Schedule.class);
    assertAll(
      () -> assertNotNull(schedule),
      () -> assertEquals(schedule.getId(), SCHEDULE.getId()),
      () -> assertEquals(schedule.getPromotion(), SCHEDULE.getPromotion()),
      () -> assertEquals(schedule.getUrl(), SCHEDULE.getUrl())
    );
  }

  @Test
  @Order(4)
  public void testAssociations() {
    ID_SERVER = new Server(REF_SERVER, SCHEDULE).create();
    ID_SESSION = new Session(SESSION_NAME, SESSION_TEACHER, SESSION_LOCATION, new Date(), new Date(), new Date(), SCHEDULE, SESSION_TYPE).create();
    Schedule finalSchedule = ModelDAO.read(ID_SCHEDULE, Schedule.class);
    assertAll(
      () -> assertEquals(finalSchedule.getServers().size(), 1),
      () -> assertEquals(new ArrayList<>(finalSchedule.getServers()).get(0).getId(), ID_SERVER),
      () -> assertEquals(finalSchedule.getSessions().size(), 1),
      () -> assertEquals(new ArrayList<>(finalSchedule.getSessions()).get(0).getId(), ID_SESSION)
    );
  }

  @Test
  @Order(5)
  public void testUpdate_promotion_null() {
    Schedule schedule = new Schedule(null, URL_UPDATE);
    schedule.setId(SCHEDULE.getId());
    assertThrows(PersistenceException.class, schedule::update);
  }

  @Test
  @Order(6)
  public void testUpdate_url_null() {
    Schedule schedule = new Schedule(PROMOTION_UPDATE, null);
    schedule.setId(SCHEDULE.getId());
    assertThrows(PersistenceException.class, schedule::update);
  }

  @Test
  @Order(7)
  @Override
  public void testUpdate() {
    SCHEDULE.setPromotion(PROMOTION_UPDATE);
    SCHEDULE.setUrl(URL_UPDATE);
    SCHEDULE.update();
    SCHEDULE = ModelDAO.read(ID_SCHEDULE, Schedule.class);
    assertNotNull(SCHEDULE);
    assertAll(
      () -> assertEquals(SCHEDULE.getPromotion(), PROMOTION_UPDATE),
      () -> assertEquals(SCHEDULE.getUrl(), URL_UPDATE)
    );
  }

  @Test
  @Order(8)
  public void testDelete_whith_existing_associations() {
    assertThrows(PersistenceException.class, () -> SCHEDULE.delete());
  }

  @Test
  @Order(9)
  @Override
  public void testDelete() {
    Session session = ModelDAO.read(ID_SESSION, Session.class);
    if (nonNull(session)) session.delete();
    Server server = ModelDAO.read(ID_SERVER, Server.class);
    if (nonNull(server)) server.delete();
    SCHEDULE.delete();
    Schedule schedule = ModelDAO.read(ID_SCHEDULE, Schedule.class);
    assertNull(schedule);
  }
}
