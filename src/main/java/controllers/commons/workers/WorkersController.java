package controllers.commons.workers;

import controllers.commons.Runner;
import controllers.schedule.workers.ScheduleWorkersHandler;
import controllers.task.workers.TaskWorkersHandler;
import utils.EnvironmentVariablesUtils;

/**
 * Contrôleur permettant d'initialiser la gestion des opérations automatiques.
 */
public class WorkersController extends Runner<WorkersHandler> {

  public static final int DAILY_HOUR = EnvironmentVariablesUtils.getInt(EnvironmentVariablesUtils.DAILY_HOUR, 18);
  public static final int DAILY_MINUTE = EnvironmentVariablesUtils.getInt(EnvironmentVariablesUtils.DAILY_MINUTE, 00);

  @Override
  public Runner<WorkersHandler> init() {
    runnables.add(new ScheduleWorkersHandler().init());
    runnables.add(new TaskWorkersHandler().init());
    return this;
  }
}
