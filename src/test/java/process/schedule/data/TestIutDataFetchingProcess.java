package process.schedule.data;

import models.Schedule;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import process.schedule.data.IutDataFetchingProcess;
import utils.EnvironmentVariablesUtils;

import static java.util.Objects.isNull;
import static org.junit.jupiter.api.Assertions.*;
import static utils.EnvironmentVariablesUtils.SCHEDULE_URL;

/**
 * Classe de test de {@link IutDataFetchingProcess}.
 */
public class TestIutDataFetchingProcess {

  private static IutDataFetchingProcess PROCESS;
  private static final String URL_TEST = "urltest";
  private static final String START_ICAL = "BEGIN:VCALENDAR";

  @BeforeAll
  public static void init() {
    PROCESS = new IutDataFetchingProcess();
  }

  @Test
  public void testFetch_valid_url() {
    Schedule schedule = new Schedule();
    schedule.setUrl(EnvironmentVariablesUtils.getString(SCHEDULE_URL));
    if (isNull(schedule.getUrl())) fail();
    String data = PROCESS.fetch(schedule);
    Assertions.assertAll(
      () -> assertNotNull(data),
      () -> assertNotEquals(data, Strings.EMPTY),
      () -> assertTrue(() -> data.startsWith(START_ICAL))
    );
  }

  @Test
  public void testFetch_invalid_url_no_blocking_error() {
    Schedule schedule = new Schedule();
    schedule.setUrl(URL_TEST);
    assertDoesNotThrow(() -> PROCESS.fetch(schedule));
    String data = PROCESS.fetch(schedule);
    assertNull(data);
  }
}
