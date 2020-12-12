package controllers.workers;

import org.junit.jupiter.api.RepeatedTest;
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

  @RepeatedTest(2)
  @Timeout(3)
  public void run_doit_programmer_un_traitement_realise_a_heure_fixe() throws InterruptedException {
    initTime();
    DailyWorker dailyWorker = new DailyWorker(hour, min, sec, 0) {
      @Override
      public void runOne() {
        TEST = true;
      }

      @Override
      protected String getTask() {
        return null;
      }
    };
    dailyWorker.run();
    int secNow = ZonedDateTime.now(ZoneId.of(ZONE_ID)).getSecond();
    int secWorker = sec;
    while (secNow != secWorker + 1) {
      Thread.sleep(1000);
      secNow = ZonedDateTime.now(ZoneId.of(ZONE_ID)).getSecond();
    }
    dailyWorker.stop();
    assertTrue(TEST);
  }

  @RepeatedTest(2)
  @Timeout(3)
  public void run_doit_programmer_un_traitement_avec_un_delai() throws InterruptedException {
    initTime();
    DailyWorker dailyWorker = new DailyWorker(hour, min, sec, 30) {
      @Override
      public void runOne() {
        TEST = true;
      }

      @Override
      protected String getTask() {
        return null;
      }
    };
    dailyWorker.run();
    int secNow = ZonedDateTime.now(ZoneId.of(ZONE_ID)).getSecond();
    int secWorker = sec;
    while (secNow != secWorker + 1) {
      Thread.sleep(1000);
      secNow = ZonedDateTime.now(ZoneId.of(ZONE_ID)).getSecond();
    }
    dailyWorker.stop();
    assertFalse(TEST);
  }

  @RepeatedTest(2)
  public void run_doit_programmer_un_traitement_netant_pas_execute_avant_lheure_prevue() throws InterruptedException {
    initTime();
    DailyWorker dailyWorker = new DailyWorker(hour, min, 0) {
      @Override
      public void runOne() {
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
  public void ne_doit_pas_accepter_un_horaire_errone() throws InterruptedException {
    assertThrows(IllegalArgumentException.class, () ->
      new DailyWorker(25, 70, 0) {
        @Override
        public void runOne() {
        }

        @Override
        protected String getTask() {
          return null;
        }
      });
  }

  @Test
  public void ne_doit_pas_accepter_un_delai_negatif() throws InterruptedException {
    assertThrows(IllegalArgumentException.class, () ->
      new DailyWorker(0, 0, -1) {
        @Override
        public void runOne() {
        }

        @Override
        protected String getTask() {
          return null;
        }
      });
  }

  private void initTime() {
    TEST = false;
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of(ZONE_ID));
    sec = now.getSecond() + 1;
    min = now.getMinute();
    hour = now.getHour();
    if (sec >= 60 || min >= 60 || hour >= 24) {
      fail();
    }
  }

}
