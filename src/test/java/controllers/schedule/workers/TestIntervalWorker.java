package controllers.schedule.workers;

import controllers.commons.workers.IntervalWorker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test de {@link IntervalWorker}.
 */
public class TestIntervalWorker {

  private static final int INTERVAL = 100;
  private static final int TIME_EXEC = 1000;
  public static final int DELAY = 500;
  private static int TEST1, TEST2;

  @Test
  @Timeout(10)
  public void run_doit_programmer_un_traitement_realise_a_intervalle_regulier() throws InterruptedException {
    IntervalWorker intervalWorker = new IntervalWorker(INTERVAL, 0) {
      @Override
      public void doRunOne() {
        TEST1++;
      }

      @Override
      protected String getTask() {
        return null;
      }
    };
    intervalWorker.run();
    Thread.sleep(TIME_EXEC);
    intervalWorker.stop();
    assertAll(
      () -> assertTrue(TIME_EXEC / INTERVAL - 1 <= TEST1),
      () -> assertTrue(TIME_EXEC / INTERVAL + 1 >= TEST1)
    );
  }

  @Test
  @Timeout(10)
  public void run_doit_programmer_un_traitement_avec_le_bon_delai() throws InterruptedException {
    IntervalWorker intervalWorker = new IntervalWorker(INTERVAL, DELAY) {
      @Override
      public void doRunOne() {
        TEST2++;
      }

      @Override
      protected String getTask() {
        return null;
      }
    };
    intervalWorker.run();
    Thread.sleep(TIME_EXEC);
    intervalWorker.stop();
    assertAll(
      () -> assertTrue(TIME_EXEC / INTERVAL - DELAY / INTERVAL - 1 <= TEST2),
      () -> assertTrue(TIME_EXEC / INTERVAL - DELAY / INTERVAL + 1 >= TEST2)
    );
  }

  @Test
  public void ne_doit_pas_accepter_un_intervalle_negatif() {
    assertThrows(IllegalArgumentException.class, () ->
      new IntervalWorker(-1, 0) {
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
      new IntervalWorker(INTERVAL, -1) {
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
