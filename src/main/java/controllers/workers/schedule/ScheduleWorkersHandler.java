package controllers.workers.schedule;

import controllers.workers.WorkersHandler;

import java.util.concurrent.TimeUnit;

import static controllers.workers.WorkersController.DAILY_HOUR;
import static controllers.workers.WorkersController.DAILY_MINUTE;

/**
 * Gère les opérations automatiques relative à l'emploi du temps.
 */
public class ScheduleWorkersHandler extends WorkersHandler {

  @Override
  public WorkersHandler init() {
    runnables.add(new SchedulePublicationWorker(DAILY_HOUR, DAILY_MINUTE, 0));
    runnables.add(new ScheduleUpdateWorker(TimeUnit.HOURS.toMillis(2), 0));
    return this;
  }
}
