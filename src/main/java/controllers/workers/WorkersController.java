package controllers.workers;

import controllers.Runner;
import controllers.workers.schedule.ScheduleWorkersHandler;

/**
 * Contrôleur permettant d'initialiser la gestion des opérations automatiques.
 */
public class WorkersController extends Runner<WorkersHandler> {

  @Override
  protected void init() {
    runnables.add(new ScheduleWorkersHandler());
  }
}