package models;

import models.dao.ModelDAO;
import models.dao.Schedule;
import models.dao.Server;
import models.dao.Task;
import org.junit.jupiter.api.*;

import javax.persistence.PersistenceException;
import java.text.ParseException;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.*;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Classe de test de {@link Task}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestTask implements TestModel {
  private static final String TEST_SCHEDULE_PROMO = "LP GL";
  private static final String TEST_SCHEDULE_URL = "url.test";
  private static final String TEST_SERVER_REFERENCE = "ref";
  private static final String TEST_TASK_DESCRIPTION = "TP GraphQL";
  private static final String TEST_TASK_DUE_DATE = "16-01-2021";
  private static final String TEST_TASK_DUE_TIME = "08:00";
  private static final String UPDATED_SERVER_REFERENCE = "refref";
  private static final String UPDATED_TASK_DESCRIPTION = "Avancement des travaux";
  private static final String UPDATED_TASK_DUE_DATE = "06-02-2021";
  private static final String UPDATED_TASK_DUE_TIME = "10:45";

  private static int SCHEDULE_ID;
  private static Schedule SCHEDULE;
  private static int SERVER_ID;
  private static Server SERVER;
  private static int UPDATED_SERVER_ID;
  private static Server UPDATED_SERVER;
  private static int TASK_ID;
  private static Task TASK;

  @AfterAll
  public static void tearDown() {
    TASK = ModelDAO.read(TASK_ID, Task.class);
    if (nonNull(TASK)) {
      TASK.delete();
    }

    UPDATED_SERVER = ModelDAO.read(UPDATED_SERVER_ID, Server.class);
    if (nonNull(UPDATED_SERVER)) {
      UPDATED_SERVER.delete();
    }

    SERVER = ModelDAO.read(SERVER_ID, Server.class);
    if (nonNull(SERVER)) {
      SERVER.delete();
    }
  }

  @Test
  @Order(1)
  @Override
  public void testCreate() {
    SCHEDULE = new Schedule(TEST_SCHEDULE_PROMO, TEST_SCHEDULE_URL);
    SCHEDULE_ID = SCHEDULE.create();
    SERVER = new Server(TEST_SERVER_REFERENCE, Schedule.read(SCHEDULE_ID, Schedule.class));
    SERVER_ID = SERVER.create();

    try {
      TASK = new Task(TEST_TASK_DESCRIPTION, stringToDate(TEST_TASK_DUE_DATE), stringToTime(TEST_TASK_DUE_TIME),
        Server.read(SERVER_ID, Server.class));
    } catch (ParseException e) {
      fail();
    }

    TASK_ID = TASK.create();
    final List<Task> tasks = ModelDAO.readAll(Task.class);
    assertTrue(tasks.stream().anyMatch(t -> t.getId() == TASK_ID));
  }

  @Test
  @Order(2)
  public void testCreate_null_description() {
    Task task = null;
    try {
      task = new Task(TEST_TASK_DESCRIPTION, stringToDate(TEST_TASK_DUE_DATE), stringToTime(TEST_TASK_DUE_TIME),
        Server.read(SERVER_ID, Server.class));
    } catch (ParseException e) {
      fail();
    }

    task.setDescription(null);
    assertThrows(PersistenceException.class, task::create);
  }

  @Test
  @Order(3)
  public void testCreate_null_due_date() {
    Task task = null;
    try {
      task = new Task(TEST_TASK_DESCRIPTION, stringToDate(TEST_TASK_DUE_DATE), stringToTime(TEST_TASK_DUE_TIME),
        Server.read(SERVER_ID, Server.class));
    } catch (ParseException e) {
      fail();
    }

    task.setDueDate(null);
    assertThrows(PersistenceException.class, task::create);
  }

  @Test
  @Order(4)
  public void testCreate_null_due_time() {
    Task task = null;
    try {
      task = new Task(TEST_TASK_DESCRIPTION, stringToDate(TEST_TASK_DUE_DATE), stringToTime(TEST_TASK_DUE_TIME),
        Server.read(SERVER_ID, Server.class));
    } catch (ParseException e) {
      fail();
    }

    task.setDueTime(null);
    assertThrows(PersistenceException.class, task::create);
  }

  @Test
  @Order(5)
  public void testCreate_null_server() {
    Task task = null;
    try {
      task = new Task(TEST_TASK_DESCRIPTION, stringToDate(TEST_TASK_DUE_DATE), stringToTime(TEST_TASK_DUE_TIME),
        Server.read(SERVER_ID, Server.class));
    } catch (ParseException e) {
      fail();
    }

    task.setServer(null);
    assertThrows(PersistenceException.class, task::create);
  }

  @Test
  @Order(6)
  @Override
  public void testRead() {
    Task task = Task.read(TASK_ID, Task.class);
    assertAll(
      () -> assertNotNull(task),
      () -> assertEquals(TASK.getId(), task.getId()),
      () -> assertEquals(TASK.getServer().getReference(), task.getServer().getReference()),
      () -> assertEquals(TASK.getDescription(), task.getDescription()),
      () -> assertEquals(TASK.getDueDate(), task.getDueDate()),
      () -> assertEquals(TASK.getDueTime(), task.getDueTime())
    );
  }

  @Test
  @Order(7)
  @Override
  public void testUpdate() {
    UPDATED_SERVER = new Server(UPDATED_SERVER_REFERENCE, Schedule.read(SCHEDULE_ID, Schedule.class));
    UPDATED_SERVER_ID = UPDATED_SERVER.create();

    TASK.setDescription(UPDATED_TASK_DESCRIPTION);
    TASK.setServer(UPDATED_SERVER);

    try {
      TASK.setDueDate(stringToDate(UPDATED_TASK_DUE_DATE));
      TASK.setDueTime(stringToTime(UPDATED_TASK_DUE_TIME));
    } catch (ParseException e) {
      fail();
    }

    TASK.update();
    TASK = ModelDAO.read(TASK_ID, Task.class);
    assertAll(
      () -> assertEquals(UPDATED_SERVER_REFERENCE, TASK.getServer().getReference()),
      () -> assertEquals(UPDATED_TASK_DESCRIPTION, TASK.getDescription()),
      () -> assertEquals(stringToDate(UPDATED_TASK_DUE_DATE), TASK.getDueDate()),
      () -> assertEquals(stringToTime(UPDATED_TASK_DUE_TIME), TASK.getDueTime())
    );
  }

  @Test
  @Order(8)
  public void testUpdate_null_description() {
    Task task = Task.read(TASK_ID, Task.class);
    task.setDescription(null);

    assertThrows(PersistenceException.class, task::update);
  }

  @Test
  @Order(9)
  public void testUpdate_null_due_date() {
    Task task = Task.read(TASK_ID, Task.class);
    task.setDueDate(null);

    assertThrows(PersistenceException.class, task::update);
  }

  @Test
  @Order(10)
  public void testUpdate_null_due_time() {
    Task task = Task.read(TASK_ID, Task.class);
    task.setDueTime(null);

    assertThrows(PersistenceException.class, task::update);
  }

  @Test
  @Order(11)
  public void testUpdate_null_server() {
    Task task = Task.read(TASK_ID, Task.class);
    task.setServer(null);

    assertThrows(PersistenceException.class, task::update);
  }

  @Test
  @Order(12)
  public void testDelete_task_with_associated_server() {
    assertThrows(PersistenceException.class, UPDATED_SERVER::delete);
  }

  @Test
  @Order(13)
  @Override
  public void testDelete() {
    TASK.delete();
    Task task = ModelDAO.read(TASK_ID, Task.class);
    assertNull(task);
  }
}
