package controllers.schedule.workers;

import controllers.commons.workers.DailyWorker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test de {@link DailyWorker}.
 */
public class TestDailyWorker {

  private static final String ZONE_ID = "Europe/Paris";
  private static int sec, min, hour;
  private static boolean TEST;

  @BeforeEach
  public void init() {
    TEST = false;
    ZonedDateTime time = ZonedDateTime.now(ZoneId.of(ZONE_ID)).plusSeconds(1);
    sec = time.getSecond();
    min = time.getMinute();
    hour = time.getHour();
  }

  @Test
  @Timeout(10)
  public void run_doit_programmer_un_traitement_realise_a_heure_fixe() throws InterruptedException {
    DailyWorker dailyWorker = new DailyWorker(hour, min, sec, 0) {
      @Override
      public void doRunOne() {
        TEST = true;
      }

      @Override
      protected String getTask() {
        return null;
      }
    };
    dailyWorker.run();
    int secNow = ZonedDateTime.now(ZoneId.of(ZONE_ID)).getSecond();
    int secWorker = sec == 59 ? 0 : sec + 1;
    while (secNow != secWorker + 1) {
      Thread.sleep(1000);
      secNow = ZonedDateTime.now(ZoneId.of(ZONE_ID)).getSecond();
    }
    dailyWorker.stop();
    assertTrue(TEST);
  }

  @Test
  @Timeout(10)
  public void run_doit_programmer_un_traitement_avec_un_delai() throws InterruptedException {
    DailyWorker dailyWorker = new DailyWorker(hour, min, sec, 30) {
      @Override
      public void doRunOne() {
        TEST = true;
      }

      @Override
      protected String getTask() {
        return null;
      }
    };
    dailyWorker.run();
    int secNow = ZonedDateTime.now(ZoneId.of(ZONE_ID)).getSecond();
    int secWorker = sec == 59 ? 0 : sec + 1;
    while (secNow != secWorker) {
      Thread.sleep(1000);
      secNow = ZonedDateTime.now(ZoneId.of(ZONE_ID)).getSecond();
    }
    dailyWorker.stop();
    assertFalse(TEST);
  }

  @Test
  public void run_doit_programmer_un_traitement_netant_pas_execute_avant_lheure_prevue() throws InterruptedException {
    DailyWorker dailyWorker = new DailyWorker(hour, min, 0) {
      @Override
      public void doRunOne() {
        TEST = true;
      }

      @Override
      protected String getTask() {
        return null;
      }
    };
    dailyWorker.run();
    Thread.sleep(1000);
    dailyWorker.stop();
    assertFalse(TEST);
  }

  @Test
  public void ne_doit_pas_accepter_un_horaire_errone() {
    assertThrows(IllegalArgumentException.class, () ->
      new DailyWorker(25, 70, 0) {
        @Override
        public void doRunOne() {
        }

        @Override
        protected String getTask() {
          return null;
        }
      });
  }

  @Test
  public void ne_doit_pas_accepter_un_delai_negatif() {
    assertThrows(IllegalArgumentException.class, () ->
      new DailyWorker(0, 0, -1) {
        @Override
        public void doRunOne() {
        }

        @Override
        protected String getTask() {
          return null;
        }
      });
  }

}
