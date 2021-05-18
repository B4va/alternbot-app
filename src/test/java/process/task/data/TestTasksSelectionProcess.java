package process.task.data;

import models.dao.ModelDAO;
import models.dao.Schedule;
import models.dao.Server;
import models.dao.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Classe de test de {@link TasksSelectionProcess}.
 */
public class TestTasksSelectionProcess {

  private static TasksSelectionProcess PROCESS;
  private static Schedule SCHEDULE;
  private static Server SERVER_ONE_TASK;
  private static Server SERVER_MULTIPLE_TASKS;
  private static Server SERVER_NO_TASK;
  private static Server SERVER_OLD_TASK;
  private static Server SERVER_NOT_SORTED_TASKS;
  private static Task TASK0;
  private static Task TASK1;
  private static Task TASK2;
  private static Task TASK3;
  private static Task TASK4;
  private static Task TASK5;
  private static Task TASK6;

  @BeforeAll
  public static void init() throws ParseException {
    PROCESS = new TasksSelectionProcess();
    SCHEDULE = new Schedule("promotion", "url");
    SCHEDULE.setId(SCHEDULE.create());
    SERVER_NO_TASK = new Server("ref", SCHEDULE);
    SERVER_NO_TASK.setId(SERVER_NO_TASK.create());
    SERVER_OLD_TASK = new Server("ref", SCHEDULE);
    SERVER_OLD_TASK.setId(SERVER_OLD_TASK.create());
    SERVER_ONE_TASK = new Server("ref", SCHEDULE);
    SERVER_ONE_TASK.setId(SERVER_ONE_TASK.create());
    SERVER_MULTIPLE_TASKS = new Server("ref", SCHEDULE);
    SERVER_MULTIPLE_TASKS.setId(SERVER_MULTIPLE_TASKS.create());
    SERVER_NOT_SORTED_TASKS = new Server("ref", SCHEDULE);
    SERVER_NOT_SORTED_TASKS.setId(SERVER_NOT_SORTED_TASKS.create());
    TASK0 = new Task("test", stringToDate("01-01-2000"), new Date(), SERVER_OLD_TASK);
    TASK0.setId(TASK0.create());
    TASK1 = new Task("test", stringToDate("01-01-2050"), new Date(), SERVER_ONE_TASK);
    TASK1.setId(TASK1.create());
    TASK2 = new Task("test", stringToDate("01-01-2050"), new Date(), SERVER_MULTIPLE_TASKS);
    TASK2.setId(TASK2.create());
    TASK3 = new Task("test", stringToDate("01-01-2050"), new Date(), SERVER_MULTIPLE_TASKS);
    TASK3.setId(TASK3.create());
    TASK4 = new Task("test", stringToDate("01-01-2050"), stringToTime("10:00"), SERVER_NOT_SORTED_TASKS);
    TASK4.setId(TASK4.create());
    TASK5 = new Task("test", stringToDate("01-01-2040"), stringToTime("10:00"), SERVER_NOT_SORTED_TASKS);
    TASK5.setId(TASK5.create());
    TASK6 = new Task("test", stringToDate("01-01-2040"), stringToTime("09:00"), SERVER_NOT_SORTED_TASKS);
    TASK6.setId(TASK6.create());
  }

  @AfterAll
  public static void tearDown() {
    Arrays.asList(TASK0, TASK1, TASK2, TASK3, TASK4, TASK5, TASK6)
      .forEach(ModelDAO::delete);
    Arrays.asList(SERVER_MULTIPLE_TASKS, SERVER_ONE_TASK, SERVER_NO_TASK, SERVER_OLD_TASK, SERVER_NOT_SORTED_TASKS)
      .forEach(ModelDAO::delete);
    SCHEDULE.delete();
  }

  @Test
  public void testSelect_no_corresponding_task() {
    List<Task> selection = PROCESS.select(SERVER_NO_TASK);
    assertTrue(selection.isEmpty());
  }

  @Test
  public void testSelect_old_task() {
    List<Task> selection = PROCESS.select(SERVER_OLD_TASK);
    assertTrue(selection.isEmpty());
  }

  @Test
  public void testSelect_one_corresponding_task() {
    List<Task> selection = PROCESS.select(SERVER_ONE_TASK);
    assertAll(
      () -> assertEquals(1,selection.size()),
      () -> assertEquals(TASK1.getId(),selection.get(0).getId())
    );
  }

  @Test
  public void testSelect_mutiple_corresponding_tasks() {
    List<Task> selection = PROCESS.select(SERVER_MULTIPLE_TASKS);
    assertAll(
      () -> assertEquals(2,selection.size()),
      () -> assertTrue(selection.stream().anyMatch(t -> t.getId() == TASK2.getId())),
      () -> assertTrue(selection.stream().anyMatch(t -> t.getId() == TASK3.getId()))
    );
  }

  @Test
  public void testSelect_with_date_constraint() {
    List<Task> selection = PROCESS.select(SERVER_ONE_TASK, 3);
    assertTrue(selection.isEmpty());
  }

  @Test
  public void testSelect_sorted_list_by_date() {
    List<Task> selection = PROCESS.select(SERVER_NOT_SORTED_TASKS);
    assertAll(
      () -> assertEquals(3,selection.size()),
      () -> assertEquals(TASK6.getId(),selection.get(0).getId()),
      () -> assertEquals(TASK5.getId(),selection.get(1).getId()),
      () -> assertEquals(TASK4.getId(),selection.get(2).getId())
    );
  }
}
