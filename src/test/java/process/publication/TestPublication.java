package process.publication;

import models.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.EnvironmentVariablesUtils;

import javax.security.auth.login.LoginException;

import static java.util.Objects.isNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static utils.EnvironmentVariablesUtils.CHANNEL_TEST;
import static utils.EnvironmentVariablesUtils.SERVEUR_TEST;

/**
 * Classe de test de {@link Publication}.
 */
public class TestPublication {

  private static Publication PROCESS;
  private static final String MESSAGE = "test";
  private static String CHANNEL = "général";

  @BeforeAll
  public static void init() {
    PROCESS = new Publication() {
      @Override
      protected boolean sendMessage(String message, Server server, String channel) throws LoginException, InterruptedException {
        return super.sendMessage(message, server, channel);
      }
    };
    CHANNEL = EnvironmentVariablesUtils.getString(CHANNEL_TEST, CHANNEL);
  }

  @Test
  public void testSendMessage_ok() throws LoginException, InterruptedException {
    Server server = new Server(EnvironmentVariablesUtils.getString(SERVEUR_TEST), null);
    if (isNull(server.getReference())) fail();
    assertTrue(PROCESS.sendMessage(MESSAGE, server, CHANNEL));
  }
}
