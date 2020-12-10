package controllers.workers;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Gère la programmation d'opérations devant être exécutées à intervalle régulier.
 */
public abstract class IntervalWorker extends Worker {

  private static final String ERR_INTERVAL = "L'intervalle doit être strictement positif.";
  private static final String ERR_DELAY = "Le délai doit être positif.";

  private final long interval;
  private final long delay;

  /**
   * Constructeur.
   *
   * @param interval délai entre chaque exécution (en millisecondes)
   * @param delay    délai avant lancement (en millisecondes)
   */
  public IntervalWorker(long interval, long delay) {
    if (interval <= 0) {
      throw new IllegalArgumentException(ERR_INTERVAL);
    } else if (delay < 0) {
      throw new IllegalArgumentException(ERR_DELAY);
    } else {
      this.interval = interval;
      this.delay = delay;
    }
  }

  /**
   * Lance le {@link Worker}.
   */
  @Override
  public void run() {
    TimerTask timerTask = getTimerTask();
    new Timer(getThreadName()).schedule(timerTask, delay, interval);
  }
}
