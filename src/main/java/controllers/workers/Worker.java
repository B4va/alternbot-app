package controllers.workers;

import java.util.TimerTask;

public abstract class Worker implements Runnable {

  public abstract void runOne();

  protected String getThreadName() {
    return Thread.currentThread().getName() + " > " + getTask();
  }

  protected abstract String getTask();

  protected TimerTask getTimerTask() {
    return new TimerTask() {
      @Override
      public void run() {
        runOne();
      }
    };
  }
}
