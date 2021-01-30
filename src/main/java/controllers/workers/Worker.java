package controllers.workers;

import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Gère la programmation de process exécutés automatiquement.
 */
public abstract class Worker implements Runnable {

  private static final Logger LOGGER = LoggerUtils.buildLogger(Worker.class);

  protected Timer timer;

  /**
   * Lance le process.
   */
  public void runOne() {
    LOGGER.info("Lancement du processus.");
    doRunOne();
    LOGGER.info("Processus terminé.");
  }

  protected abstract void doRunOne();

  /**
   * Arrête le worker.
   */
  public void stop() {
    if (Objects.nonNull(timer)) {
      timer.cancel();
    }
  }

  /**
   * Formate le nom du {@link Thread} généré.
   *
   * @return nom du thread
   */
  protected String getThreadName() {
    return Thread.currentThread().getName() + " > " + getTask();
  }

  /**
   * Définit le libellé de la tâche associée au worker.
   *
   * @return nom du worker
   */
  protected abstract String getTask();

  /**
   * Encapsule le process à lancer dans une {@link TimerTask}.
   *
   * @return task
   */
  protected TimerTask getTimerTask() {
    return new TimerTask() {
      @Override
      public void run() {
        runOne();
      }
    };
  }
}
