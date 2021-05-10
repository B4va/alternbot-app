package controllers.schedule.workers;

import controllers.commons.workers.WorkersHandler;

import java.util.concurrent.TimeUnit;

import static controllers.commons.workers.WorkersController.DAILY_HOUR;
import static controllers.commons.workers.WorkersController.DAILY_MINUTE;

/**
 * Gère les opérations automatiques relative à l'emploi du temps.
 */
public class ScheduleWorkersHandler extends WorkersHandler {

  @Override
  public WorkersHandler init() {
    runnables.add(new SchedulePublicationWorker(DAILY_HOUR, DAILY_MINUTE, 0, true));
    runnables.add(new ScheduleUpdateWorker(TimeUnit.HOURS.toMillis(2), 0));
    return this;
  }
}
