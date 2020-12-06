package controllers.workers;

import java.util.Timer;
import java.util.TimerTask;

public abstract class IntervalWorker extends Worker {

  private static final String ERR_INTERVAL = "L'intervalle doit être strictement positif.";
  private static final String ERR_DELAY = "Le délai doit être positif.";

  private final long interval;
  private final long delay;

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

  @Override
  public void run() {
    TimerTask timerTask = getTimerTask();
    new Timer(getThreadName()).schedule(timerTask, delay, interval);
  }
}
