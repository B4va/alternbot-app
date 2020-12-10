package controllers.workers.schedule;

import controllers.workers.WorkersHandler;

import java.util.concurrent.TimeUnit;

/**
 * Gère les opérations automatiques relative à l'emploi du temps.
 */
public class ScheduleWorkersHandler extends WorkersHandler {

  @Override
  protected void init() {
    // Tous les jours à 07h45.
    runnables.add(new SchedulePublicationWorker(7, 30, 0));
    // Toutes les heures.
    runnables.add(new ScheduleUpdateWorker(TimeUnit.HOURS.toMillis(1), 0));
  }
}
