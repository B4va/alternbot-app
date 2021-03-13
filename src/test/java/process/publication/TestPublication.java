package process.publication;

import models.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.EnvironmentVariablesUtils;

import javax.security.auth.login.LoginException;

import java.nio.charset.StandardCharsets;

import static java.util.Objects.isNull;
import static org.junit.jupiter.api.Assertions.*;
import static utils.EnvironmentVariablesUtils.CHANNEL_TEST;
import static utils.EnvironmentVariablesUtils.SERVER_TEST;
import static utils.JDAUtils.initializeJDA;

/**
 * Classe de test de {@link Publication}.
 */
public class TestPublication {

  private static Publication PROCESS;
  private static final String MESSAGE = "test";
  private static final String TEST_FILE_CONTENT = "Bleep bloop. I am a robot.";
  private static final String TEST_FILE_NAME = "test.txt";
  private static String CHANNEL = "général";
  private static final String INVALID_SERVER_REF = "ref";
  private static final String NOT_EXISTING_CHANNEL = "nochan";
  private static String LONG_MESSAGE = "";

  @BeforeAll
  public static void init() throws LoginException, InterruptedException {
    initializeJDA();
    PROCESS = new Publication() {
      @Override
      protected boolean sendMessage(String message, Server server, String channel) {
        return super.sendMessage(message, server, channel);
      }
    };
    CHANNEL = EnvironmentVariablesUtils.getString(CHANNEL_TEST, CHANNEL);
    for(int i=0;i<2001;i++){
      LONG_MESSAGE+="a ";
    }
  }

  @Test
  public void testSendMessage_ok() {
    Server server = new Server(EnvironmentVariablesUtils.getString(SERVER_TEST), null);
    if (isNull(server.getReference())) fail();
    assertTrue(PROCESS.sendMessage(MESSAGE, server, CHANNEL));
  }

  @Test
  public void testSendMessageLong(){
    Server server = new Server(EnvironmentVariablesUtils.getString(SERVER_TEST), null);
    if (isNull(server.getReference())) fail();
    assertFalse(PROCESS.sendMessage(LONG_MESSAGE, server, CHANNEL));
  }

  @Test
  public void testSendMessage_invalid_server() {
    Server server = new Server(INVALID_SERVER_REF, null);
    assertFalse(PROCESS.sendMessage(MESSAGE, server, CHANNEL));
  }

  @Test
  public void testSendMessage_not_existing_channel() {
    Server server = new Server(EnvironmentVariablesUtils.getString(SERVER_TEST), null);
    if (isNull(server.getReference())) fail();
    assertFalse(PROCESS.sendMessage(MESSAGE, server, NOT_EXISTING_CHANNEL));
  }

  @Test
  public void testSendFile_ok() {
    Server server = new Server(EnvironmentVariablesUtils.getString(SERVER_TEST), null);
    if (isNull(server.getReference())) fail();
    assertTrue(PROCESS.sendFile(TEST_FILE_CONTENT.getBytes(StandardCharsets.UTF_8), TEST_FILE_NAME, false, server, CHANNEL));
  }
  @Test
  public void testSendFile_okSpoiler() {
    Server server = new Server(EnvironmentVariablesUtils.getString(SERVER_TEST), null);
    if (isNull(server.getReference())) fail();
    assertTrue(PROCESS.sendFile(TEST_FILE_CONTENT.getBytes(StandardCharsets.UTF_8), TEST_FILE_NAME, true, server, CHANNEL));
  }

  @Test
  public void testSendFile_not_existing_channel() {
    Server server = new Server(EnvironmentVariablesUtils.getString(SERVER_TEST), null);
    if (isNull(server.getReference())) fail();
    assertFalse(PROCESS.sendFile(TEST_FILE_CONTENT.getBytes(StandardCharsets.UTF_8), TEST_FILE_NAME, false, server, NOT_EXISTING_CHANNEL));
  }

  @Test
  public void testSendFile_invalid_server() {
    final Server server = new Server(INVALID_SERVER_REF, null);
    assertFalse(PROCESS.sendFile(TEST_FILE_CONTENT.getBytes(StandardCharsets.UTF_8), TEST_FILE_NAME, false, server, CHANNEL));
  }

  @Test
  public void testSendFile_invalid_file_data() {
    Server server = new Server(EnvironmentVariablesUtils.getString(SERVER_TEST), null);
    if (isNull(server.getReference())) fail();
    assertFalse(PROCESS.sendFile(null, TEST_FILE_NAME, false, server, CHANNEL));
  }

  @Test
  public void testSendFile_invalid_file_name() {
    Server server = new Server(EnvironmentVariablesUtils.getString(SERVER_TEST), null);
    if (isNull(server.getReference())) fail();
    assertFalse(PROCESS.sendFile(TEST_FILE_CONTENT.getBytes(StandardCharsets.UTF_8), "", false, server, CHANNEL));
    assertFalse(PROCESS.sendFile(TEST_FILE_CONTENT.getBytes(StandardCharsets.UTF_8), null, false, server, CHANNEL));
  }

}
