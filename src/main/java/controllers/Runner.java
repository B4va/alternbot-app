package controllers;

import java.util.ArrayList;
import java.util.List;

/**
 * Gère le lancement de plusieurs process.
 *
 * @param <T> type des process à exécuter
 */
abstract public class Runner<T extends Runnable> implements Runnable {

  protected List<T> runnables = new ArrayList<>();

  /**
   * Exécute chacun des process {@link Runnable}.
   */
  @Override
  public void run() {
    init();
    runnables.forEach(Runnable::run);
  }

  /**
   * Initialise le lanceur en ajoutant des process à la liste des {@link Runnable}.
   */
  protected abstract void init();
}
