package controllers.workers.schedule;

import controllers.workers.IntervalWorker;
import process.data.SchedulesUpdateProcess;

public class SchedulesUpdateWorker extends IntervalWorker {

  /**
   * Constructeur.
   *
   * @param interval délai entre chaque exécution (en millisecondes)
   * @param delay    délai avant lancement (en millisecondes)
   */
  public SchedulesUpdateWorker(long interval, long delay) {
    super(interval, delay);
  }

  @Override
  public void runOne() {
    new SchedulesUpdateProcess().update();
  }

  @Override
  protected String getTask() {
    return "mise à jour des edt";
  }
}
