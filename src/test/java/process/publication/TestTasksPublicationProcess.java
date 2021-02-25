package process.publication;

import models.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.EnvironmentVariablesUtils;

import javax.security.auth.login.LoginException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.JDAUtils.initializeJDA;

public class TestTasksPublicationProcess {

  @BeforeAll
  public static void init() throws LoginException, InterruptedException {
    initializeJDA();
  }

  @Test
  public void testSendPublication_ok() {
    final String channel = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.CHANNEL_TEST);
    final String serverRef = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.SERVER_TEST);
    assertTrue(new TasksPublicationProcess().sendPublication(channel, serverRef));
  }

  @Test
  public void testSendPublication_ok_with_days_constraint() {
    final String channel = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.CHANNEL_TEST);
    final String serverRef = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.SERVER_TEST);
    assertTrue(new TasksPublicationProcess().sendPublication(channel, serverRef, 3));
  }

}
