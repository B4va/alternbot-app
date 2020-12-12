package controllers.workers;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Classe de test de {@link WorkersController}.
 */
public class TestWorkersController {

  private static final WorkersController workersController = new WorkersController();
  private static boolean TEST1, TEST2, TEST3;

  @Test
  public void run_doit_pouvoir_lancer_un_CommandsHandler_unique() {
    List<WorkersHandler> handlers = mockUniqueWorkersHandler();
    workersController.setRunnables(handlers);
    workersController.run();
    assertTrue(TEST1);
  }

  @Test
  public void run_doit_pouvoir_lancer_plusieurs_CommandsHandler() {
    List<WorkersHandler> handlers = mockMultipleWorkersHandlers();
    workersController.setRunnables(handlers);
    workersController.run();
    assertAll(
      () -> assertTrue(TEST2),
      () -> assertTrue(TEST3)
    );
  }

  private List<WorkersHandler> mockUniqueWorkersHandler() {
    Worker worker = new Worker() {
      @Override
      public void run() {
        TEST1 = true;
      }
      @Override
      public void runOne() {}
      @Override
      protected String getTask() {
        return null;
      }
    };
    WorkersHandler workersHandler = new WorkersHandler() {
      @Override
      public WorkersHandler init() {
        runnables.add(worker);
        return this;
      }
    }.init();
    return Collections.singletonList(workersHandler);
  }

  private List<WorkersHandler> mockMultipleWorkersHandlers() {
    Worker worker1 = new Worker() {
      @Override
      public void run() {
        TEST2 = true;
      }
      @Override
      public void runOne() {}
      @Override
      protected String getTask() {
        return null;
      }
    };
    WorkersHandler workersHandler1 = new WorkersHandler() {
      @Override
      public WorkersHandler init() {
        runnables.add(worker1);
        return this;
      }
    }.init();
    Worker worker2 = new Worker() {
      @Override
      public void run() {
        TEST3 = true;
      }
      @Override
      public void runOne() {}
      @Override
      protected String getTask() {
        return null;
      }
    };
    WorkersHandler workersHandler2 = new WorkersHandler() {
      @Override
      public WorkersHandler init() {
        runnables.add(worker2);
        return this;
      }
    }.init();
    return Arrays.asList(workersHandler1, workersHandler2);
  }
}
