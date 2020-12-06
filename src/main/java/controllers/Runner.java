package controllers;

import java.util.ArrayList;
import java.util.List;

abstract public class Runner<T extends Runnable> implements Runnable {

  protected List<T> runnables = new ArrayList<>();

  @Override
  public void run() {
    init();
    runnables.forEach(Runnable::run);
  }

  protected abstract void init();
}
