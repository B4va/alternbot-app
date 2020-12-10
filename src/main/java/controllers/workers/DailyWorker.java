package controllers.workers;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

/**
 * Gère la programmation d'opérations devant être exécutées quotidiennement.
 */
public abstract class DailyWorker extends Worker {

  private static final String ZONE_ID = "Europe/Paris";
  private static final String ERR_TIME = "Mauvais format h/min (h : 0-23 ; min : 0-59).";
  private static final String ERR_DELAY = "Le délai doit être positif.";

  private final int hour;
  private final int minute;
  private final long delay;

  /**
   * Constructeur.
   *
   * @param hour   heure
   * @param minute minute
   * @param delay  délai avant lancement (en secondes)
   */
  public DailyWorker(int hour, int minute, long delay) {
    if (hour < 0 || hour >= 24 || minute < 0 || minute >= 60) {
      throw new IllegalArgumentException(ERR_TIME);
    } else if (delay < 0) {
      throw new IllegalArgumentException(ERR_DELAY);
    } else {
      this.hour = hour;
      this.minute = minute;
      this.delay = delay;
    }
  }

  /**
   * Lance le {@link Worker}.
   */
  @Override
  public void run() {
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of(ZONE_ID));
    ZonedDateTime nextRun = now
      .withHour(hour)
      .withMinute(minute)
      .withSecond(0);
    if (now.plusSeconds(delay).isAfter(nextRun)) {
      nextRun = nextRun.plusDays(1);
    }
    long delay = Duration.between(now, nextRun).getSeconds() * 1000;
    new Timer(getThreadName()).schedule(getTimerTask(), delay, TimeUnit.DAYS.toMillis(1));
  }

}
