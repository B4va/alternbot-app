package controllers.workers.task;

import controllers.workers.WorkersHandler;

import static controllers.workers.WorkersController.DAILY_HOUR;
import static controllers.workers.WorkersController.DAILY_MINUTE;

/**
 * Gère les opérations automatiques relative aux tâches/travaux.
 */
public class TaskWorkersHandler extends WorkersHandler {

  @Override
  public WorkersHandler init() {
    runnables.add(new TasksPublicationWorker(DAILY_HOUR, DAILY_MINUTE, 0));
    return this;
  }
}
