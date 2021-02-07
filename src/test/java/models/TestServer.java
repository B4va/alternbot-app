package models;

import org.junit.jupiter.api.*;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Date;
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
  private static int ID_TASK;
  private static Server SERVER;
  private static Schedule SCHEDULE;
  private static Task TASK;
  private static final String PROMOTION_TEST = "test";
  private static final String REFERENCE_TEST = "ref";
  private static final String URL_TEST = "url.com";
  private static final String UPDATED_REFERENCE = "updated_ref";
  private static final String TASK_DESCRIPTION_TEST = "TP GraphQL";

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
    TASK = Model.read(ID_TASK, Task.class);
    if (nonNull(TASK)) {
      TASK.delete();
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
  public void testCreate_tasks_null() {
    Server server = new Server(REFERENCE_TEST, Schedule.read(ID_SCHEDULE, Schedule.class));
    server.setTasks(null);
    assertDoesNotThrow(server::create);
    server.delete();
  }

  @Test
  @Order(5)
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
  @Order(6)
  public void testAssociations() {
    ID_TASK = new Task(TASK_DESCRIPTION_TEST, new Date(), new Date(), SERVER).create();
    Server server = Model.read(ID_SERVER, Server.class);
    assertAll(
      () -> assertEquals(1, server.getTasks().size()),
      () -> assertEquals(ID_TASK, new ArrayList<>(server.getTasks()).get(0).getId())
    );
  }

  @Test
  @Order(7)
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
  @Order(8)
  public void testUpdate_reference_null() {
    SERVER.setReference(null);
    assertThrows(PersistenceException.class, SERVER::update);
  }

  @Test
  @Order(9)
  public void testUpdate_schedule_null() {
    SERVER.setSchedule(null);
    assertThrows(PersistenceException.class, SERVER::update);
  }

  @Test
  @Order(10)
  public void testDelete_schedule_with_associated_server() {
    assertThrows(PersistenceException.class, SCHEDULE::delete);
  }

  @Test
  @Order(11)
  @Override
  public void testDelete() {
    Model.read(ID_TASK, Task.class).delete();
    Model.read(ID_SERVER, Server.class).delete();
    Server s = Model.read(ID_SERVER, Server.class);
    assertNull(s);
  }
}
