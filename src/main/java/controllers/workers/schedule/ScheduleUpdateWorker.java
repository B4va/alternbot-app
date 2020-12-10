package controllers.workers.schedule;

import controllers.workers.IntervalWorker;

/**
 * Gère la mise à jour automatique de l'emploi du temps.
 */
public class ScheduleUpdateWorker extends IntervalWorker {

  private static final String TASK = "Mise à jour EDT";

  public ScheduleUpdateWorker(long intervall, long delay) {
    super(intervall, delay);
  }

  @Override
  public void runOne() {
    // TODO
  }

  @Override
  protected String getTask() {
    return TASK;
  }
}
