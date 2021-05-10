package controllers.commons.workers;

import java.time.DayOfWeek;
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
  private static final String ERR_TIME = "Mauvais format h/min (h : 0-23 ; min/sec : 0-59).";
  private static final String ERR_DELAY = "Le délai doit être positif.";

  private final int hour;
  private final int minute;
  private int second;
  private final long delay;
  private boolean skipSunday;

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
      this.skipSunday = false;
    }
  }

  /**
   * Constructeur avec la possibilité de sauter les publications des cours le dimanche
   * @param hour        heure
   * @param minute      minute
   * @param delay       delai avant lancement (en secondes)
   * @param skipSunday  ne pas publier les cours du dimanche
   */
  public DailyWorker(int hour, int minute, long delay, boolean skipSunday) {
    this(hour, minute, delay);
    this.skipSunday = skipSunday;
  }

  /**
   * Constructeur avec secondes.
   *
   * @param hour   heure
   * @param minute minute
   * @param second seconde
   * @param delay  délai avant lancement (en secondes)
   */
  public DailyWorker(int hour, int minute, int second, long delay) {
    this(hour, minute, delay);
    if (second < 0 || second > 59) {
      throw new IllegalArgumentException(ERR_TIME);
    } else {
      this.second = second;
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
      .withSecond(second);
    ZonedDateTime nowPlusDelay = now.plusSeconds(delay);
    if (nowPlusDelay.isAfter(nextRun)) {
      //If now + delay = saturday then skip the run on saturday
      if(skipSunday && nowPlusDelay.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
        //next run =  sunday
        nextRun = nextRun.plusDays(2);
      }
      else {
        nextRun = nextRun.plusDays(1);
      }
    }
    long delay = Duration.between(now, nextRun).getSeconds() * 1000;
    new Timer(getThreadName()).schedule(getTimerTask(), delay, TimeUnit.DAYS.toMillis(1));
  }

}
