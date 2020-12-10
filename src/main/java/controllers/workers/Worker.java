package controllers.workers;

import java.util.TimerTask;

/**
 * Gère la programmation de process exécutés automatiquement.
 */
public abstract class Worker implements Runnable {

  /**
   * Lance le process.
   */
  public abstract void runOne();

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
