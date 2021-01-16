package models;

import org.junit.jupiter.api.*;

import javax.persistence.PersistenceException;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test de {@link Server}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestServer implements TestModel {

  private static int ID_SERVER;
  private static int ID_SCHEDULE;
  private static Server SERVER;
  private static Schedule SCHEDULE;
  private static final String PROMOTION_TEST = "test";
  private static final String REFERENCE_TEST = "ref";
  private static final String URL_TEST = "url.com";
  private static final String UPDATED_REFERENCE = "updated_ref";

  @AfterAll
  public static void tearDown() {
    SERVER = Model.read(ID_SERVER, Server.class);
    if (nonNull(SERVER)) {
      SERVER.delete();
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
    SERVER = new Server(REFERENCE_TEST, Schedule.read(ID_SCHEDULE, Schedule.class));
    ID_SERVER = SERVER.create();
    List<Server> servers = Model.readAll(Server.class);
    assertTrue(servers.stream().anyMatch(s -> s.getId() == ID_SERVER));
  }

  @Test
  @Order(2)
  public void testCreate_reference_null() {
    Server server = new Server();
    server.setSchedule(Schedule.read(ID_SCHEDULE, Schedule.class));
    assertThrows(PersistenceException.class, server::create);
  }

  @Test
  @Order(3)
  public void testCreate_schedule_null() {
    Server server = new Server();
    server.setReference(REFERENCE_TEST);
    assertThrows(PersistenceException.class, server::create);
  }

  @Test
  @Order(4)
  @Override
  public void testRead() {
    Server s = Model.read(ID_SERVER, Server.class);
    assertAll(
      () -> assertNotNull(s),
      () -> assertEquals(s.getId(), SERVER.getId()),
      () -> assertEquals(s.getReference(), SERVER.getReference()),
      () -> assertEquals(s.getSchedule().getId(), ID_SCHEDULE)
    );
  }

  @Test
  @Order(5)
  @Override
  public void testUpdate() {
    SERVER.setReference(UPDATED_REFERENCE);
    SERVER.setSchedule(SCHEDULE);
    SERVER.update();
    SERVER = Model.read(ID_SERVER, Server.class);
    assertNotNull(SERVER);
    assertEquals(SERVER.getReference(), UPDATED_REFERENCE);
  }

  @Test
  @Order(6)
  public void testUpdate_reference_null() {
    SERVER.setReference(null);
    assertThrows(PersistenceException.class, SERVER::update);
  }

  @Test
  @Order(7)
  public void testUpdate_schedule_null() {
    SERVER.setSchedule(null);
    assertThrows(PersistenceException.class, SERVER::update);
  }

  @Test
  @Order(8)
  public void testDelete_schedule_with_associated_server() {
    assertThrows(PersistenceException.class, SCHEDULE::delete);
  }

  @Test
  @Order(9)
  @Override
  public void testDelete() {
    Model.read(ID_SERVER, Server.class).delete();
    Server s = Model.read(ID_SERVER, Server.class);
    assertNull(s);
  }

}
