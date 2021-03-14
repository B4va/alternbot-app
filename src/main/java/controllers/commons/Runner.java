package controllers.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * Gère le lancement de plusieurs process.
 *
 * @param <T> type des process à exécuter
 */
abstract public class Runner<T extends Runnable> implements Runnable {

  protected List<T> runnables = new ArrayList<>();

  public List<T> getRunnables() {
    return runnables;
  }

  public void setRunnables(List<T> runnables) {
    this.runnables = runnables;
  }

  /**
   * Exécute chacun des process {@link Runnable}.
   */
  @Override
  public void run() {
    runnables.forEach(Runnable::run);
  }

  /**
   * Initialise le lanceur en ajoutant des process à la liste des {@link Runnable}.
   */
  public abstract Runner<T> init();
}
