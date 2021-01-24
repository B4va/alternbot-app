package controllers.workers.schedule;

import controllers.workers.WorkersHandler;

import java.util.concurrent.TimeUnit;

/**
 * Gère les opérations automatiques relative à l'emploi du temps.
 */
public class ScheduleWorkersHandler extends WorkersHandler {

  @Override
  public WorkersHandler init() {
    runnables.add(new SchedulePublicationWorker(20, 0, 0));
    runnables.add(new ScheduleUpdateWorker(TimeUnit.HOURS.toMillis(2), 0));
    return this;
  }
}
