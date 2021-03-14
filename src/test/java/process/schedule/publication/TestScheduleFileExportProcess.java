package process.schedule.publication;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import process.schedule.publication.ScheduleFileExportProcess;
import utils.EnvironmentVariablesUtils;

import javax.security.auth.login.LoginException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static utils.EnvironmentVariablesUtils.CHANNEL_TEST;
import static utils.EnvironmentVariablesUtils.SERVER_TEST;
import static utils.JDAUtils.initializeJDA;

/**
 * Classe de test de {@link ScheduleFileExportProcess}.
 */
public class TestScheduleFileExportProcess {
  private static final ScheduleFileExportProcess PROCESS = new ScheduleFileExportProcess();

  private static String TEST_SERVER_REF = "";
  private static final String INVALID_SERVER_REF = "5555";
  private static String CHANNEL = "général";
  private static final String INVALID_CHANNEL_NAME = "much-error";

  @BeforeAll
  public static void init() throws LoginException, InterruptedException {
    initializeJDA();
    TEST_SERVER_REF = EnvironmentVariablesUtils.getString(SERVER_TEST);
    CHANNEL = EnvironmentVariablesUtils.getString(CHANNEL_TEST, CHANNEL);
  }

  @Test
  public void testExport_null_serverref() {
    assertFalse(PROCESS.export(null, CHANNEL));
  }

  @Test
  public void testExport_null_channelname() {
    assertFalse(PROCESS.export(TEST_SERVER_REF, null));
  }

  @Test
  public void testExport_null_date() {
    assertFalse(PROCESS.export(TEST_SERVER_REF, CHANNEL, null));
  }

  @Test
  public void testExport_no_server_found() {
    assertFalse(PROCESS.export(INVALID_SERVER_REF, CHANNEL));
  }
}
