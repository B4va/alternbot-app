package controllers.workers;

import controllers.Runner;
import controllers.workers.schedule.ScheduleWorkersHandler;

/**
 * Contrôleur permettant d'initialiser la gestion des opérations automatiques.
 */
public class WorkersController extends Runner<WorkersHandler> {

  @Override
  public Runner<WorkersHandler> init() {
    runnables.add(new ScheduleWorkersHandler().init());
    return this;
  }
}