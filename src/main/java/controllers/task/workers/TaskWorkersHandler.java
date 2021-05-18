package controllers.task.workers;

import controllers.commons.workers.WorkersHandler;

import static controllers.commons.workers.WorkersController.DAILY_HOUR;
import static controllers.commons.workers.WorkersController.DAILY_MINUTE;

/**
 * Gère les opérations automatiques relative aux tâches/travaux.
 */
public class TaskWorkersHandler extends WorkersHandler {

  @Override
  public WorkersHandler init() {
    runnables.add(new TasksPublicationWorker(DAILY_HOUR, DAILY_MINUTE, 0));
    runnables.add(new TaskDeleteWorker(DAILY_HOUR,DAILY_MINUTE,0));
    return this;
  }
}
