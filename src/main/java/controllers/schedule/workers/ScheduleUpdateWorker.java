package controllers.schedule.workers;

import controllers.commons.workers.IntervalWorker;
import process.schedule.data.SchedulesUpdateProcess;

/**
 * Gère la mise à jour régulière des données relative aux emplois du temps
 * via les services de l'IUT.
 */
public class ScheduleUpdateWorker extends IntervalWorker {

  public ScheduleUpdateWorker(long interval, long delay) {
    super(interval, delay);
  }

  @Override
  public void doRunOne() {
    new SchedulesUpdateProcess().update();
  }

  @Override
  protected String getTask() {
    return "Mise à jour des edt";
  }
}
