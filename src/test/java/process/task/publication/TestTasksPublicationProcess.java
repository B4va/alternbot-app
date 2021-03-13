package process.task.publication;

import models.dao.Schedule;
import models.dao.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.EnvironmentVariablesUtils;

import javax.security.auth.login.LoginException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.JDAUtils.initializeJDA;

public class TestTasksPublicationProcess {

  private static Schedule SCHEDULE;
  private static Server SERVEUR;

  @BeforeAll
  public static void init() throws LoginException, InterruptedException {
    SCHEDULE = new Schedule("ref", "url");
    SCHEDULE.setId(SCHEDULE.create());
    SERVEUR = new Server(EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.SERVER_TEST), SCHEDULE);
    SERVEUR.setId(SERVEUR.create());
    initializeJDA();
  }

  @AfterAll
  public static void tearDown() {
    SERVEUR.delete();
    SCHEDULE.delete();
  }

  @Test
  public void testSendPublication_ok() {
    final String channel = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.CHANNEL_TEST);
    final String serverRef = SERVEUR.getReference();
    assertTrue(new TasksPublicationProcess().sendPublication(channel, serverRef));
  }

  @Test
  public void testSendPublication_ok_with_days_constraint() {
    final String channel = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.CHANNEL_TEST);
    final String serverRef = SERVEUR.getReference();
    assertTrue(new TasksPublicationProcess().sendPublication(channel, serverRef, 3));
  }

}
