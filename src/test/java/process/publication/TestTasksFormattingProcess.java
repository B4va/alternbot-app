package process.publication;

import models.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Classe de test de {@link TasksFormattingProcess}.
 */
public class TestTasksFormattingProcess {

  private static TasksFormattingProcess PROCESS;
  private static List<Task> NO_TASK;
  private static List<Task> ONE_TASK;
  private static List<Task> MULTIPLE_TASKS;
  private static final String RES_NO_TASK = "```\nAucune tâche en cours\n```";
  private static final String RES_ONE_TASK = "```\n" +
    "LISTE DES TÂCHES EN COURS :" +
    "\n   - [1] Tâche 1 : 01-01-2020 - 10:00" +
    "\n```";
  private static final String RES_MULTIPLE_TASKS = "```\n" +
    "LISTE DES TÂCHES EN COURS :" +
    "\n   - [1] Tâche 1 : 01-01-2020 - 10:00" +
    "\n   - [2] Tâche 2 : 01-01-2020 - 10:00" +
    "\n```";
  private static final String RES_PLUS_ONE_DAY = "```\n" +
    "LISTE DES TÂCHES EN COURS (J+1) :" +
    "\n   - [1] Tâche 1 : 01-01-2020 - 10:00" +
    "\n```";

  @BeforeAll
  public static void init() throws ParseException {
    PROCESS = new TasksFormattingProcess();
    NO_TASK = new ArrayList<>();
    ONE_TASK = new ArrayList<>();
    MULTIPLE_TASKS = new ArrayList<>();
    Task t1 = new Task("Tâche 1", stringToDate("01-01-2020"), stringToTime("10:00"), null);
    t1.setId(1);
    Task t2 = new Task("Tâche 2", stringToDate("01-01-2020"), stringToTime("10:00"), null);
    t2.setId(2);
    ONE_TASK.add(t1);
    MULTIPLE_TASKS.add(t1);
    MULTIPLE_TASKS.add(t2);
  }

  @Test
  public void testFormat_no_task() {
    assertEquals(PROCESS.format(NO_TASK), RES_NO_TASK);
  }

  @Test
  public void testFormat_one_task() {
    assertEquals(PROCESS.format(ONE_TASK), RES_ONE_TASK);
  }

  @Test
  public void testFormat_mutiple_tasks() {
    assertEquals(PROCESS.format(MULTIPLE_TASKS), RES_MULTIPLE_TASKS);
  }

  @Test
  public void testFormat_period() {
    assertEquals(PROCESS.format(ONE_TASK, 1), RES_PLUS_ONE_DAY);
  }
}
