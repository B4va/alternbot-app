package process.commons;

import models.dao.Server;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.junit.jupiter.api.*;
import utils.EnvironmentVariablesUtils;

import javax.security.auth.login.LoginException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;
import static org.junit.jupiter.api.Assertions.*;
import static utils.EnvironmentVariablesUtils.CHANNEL_TEST;
import static utils.EnvironmentVariablesUtils.SERVER_TEST;
import static utils.JDAUtils.getJDAInstance;
import static utils.JDAUtils.initializeJDA;

/**
 * Classe de test de {@link Publication}.
 */
public class TestPublication {

  private static Publication PROCESS;
  private static String LONG_MESSAGE;
  private static final String MESSAGE = "test";
  private static final String TEST_FILE_CONTENT = "Bleep bloop. I am a robot.";
  private static final String TEST_FILE_NAME = "test.txt";
  private static String CHANNEL = "général";
  private static final String INVALID_SERVER_REF = "ref";
  private static final String NOT_EXISTING_CHANNEL = "nochan";

  /**
   * Tests pour l'envoi de messages et de fichiers dans des channels qui n'existent pas.
   */
  @Nested
  class NotExistingChannel {
    private Server NOTEXISTINGCHANNEL_SERVER;

    private boolean hasChannel(Server server, String channelName) {
      Guild guild = getJDAInstance().getGuildById(server.getReference());
      if (isNull(guild))
        return false;

      return !guild.getTextChannelsByName(channelName, true).isEmpty();
    }

    private void deleteChannel(Server server, String channelName) {
      Guild guild = getJDAInstance().getGuildById(server.getReference());
      if (isNull(guild))
        return;

      TextChannel channel = guild.getTextChannelsByName(channelName, true).get(0);
      channel.delete().complete();
      // Délai nécessaire sinon il arrive que la suppression du channel n'ait pas eu le temps de se faire au niveau du
      // serveur.
      final CountDownLatch waiter = new CountDownLatch(1);
      try {
        waiter.await(1000, TimeUnit.MILLISECONDS);
      } catch (InterruptedException ignored) {
      }
    }

    @BeforeEach
    void init() {
      NOTEXISTINGCHANNEL_SERVER = new Server(EnvironmentVariablesUtils.getString(SERVER_TEST), null);
      if (isNull(NOTEXISTINGCHANNEL_SERVER.getReference())) fail();
      assertFalse(hasChannel(NOTEXISTINGCHANNEL_SERVER, NOT_EXISTING_CHANNEL));
    }

    @AfterEach
    void tearDown() {
      deleteChannel(NOTEXISTINGCHANNEL_SERVER, NOT_EXISTING_CHANNEL);
    }

    @Test
    public void testSendMessage_not_existing_channel() {
      assertTrue(PROCESS.sendMessage(MESSAGE, NOTEXISTINGCHANNEL_SERVER, NOT_EXISTING_CHANNEL));
      assertTrue(hasChannel(NOTEXISTINGCHANNEL_SERVER, NOT_EXISTING_CHANNEL));
    }

    @Test
    public void testSendFile_not_existing_channel() {
      assertTrue(PROCESS.sendFile(
        TEST_FILE_CONTENT.getBytes(StandardCharsets.UTF_8), TEST_FILE_NAME, false, NOTEXISTINGCHANNEL_SERVER, NOT_EXISTING_CHANNEL
      ));
      assertTrue(hasChannel(NOTEXISTINGCHANNEL_SERVER, NOT_EXISTING_CHANNEL));
    }

    @Test
    public void testSendMessageLong_not_existing_channel() {
      assertTrue(PROCESS.sendMessage(LONG_MESSAGE, NOTEXISTINGCHANNEL_SERVER, NOT_EXISTING_CHANNEL));
      assertTrue(hasChannel(NOTEXISTINGCHANNEL_SERVER, NOT_EXISTING_CHANNEL));
    }
  }

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

    StringBuilder longMessage = new StringBuilder("Test\n").append("```\n");
    for (int i = 0; i < 200; i++)
      longMessage.append("Information de cours\n");
    longMessage.append("```").append("\ntest");
    LONG_MESSAGE = longMessage.toString();
  }

  @Test
  public void testSendMessage_ok() {
    Server server = new Server(EnvironmentVariablesUtils.getString(SERVER_TEST), null);
    if (isNull(server.getReference())) fail();
    assertTrue(PROCESS.sendMessage(MESSAGE, server, CHANNEL));
  }

  @Test
  public void testSendMessage_invalid_server() {
    Server server = new Server(INVALID_SERVER_REF, null);
    assertFalse(PROCESS.sendMessage(MESSAGE, server, CHANNEL));
  }

  /**
   * Formatage à confirmer dans le serveur de test.
   */
  @Test
  public void testSendMessageLong_ok() {
    Server server = new Server(EnvironmentVariablesUtils.getString(SERVER_TEST), null);
    if (isNull(server.getReference())) fail();
    assertTrue(PROCESS.sendMessage(LONG_MESSAGE, server, CHANNEL));
  }

  @Test
  public void testSendMessageLong_invalid_server() {
    Server server = new Server(INVALID_SERVER_REF, null);
    assertFalse(PROCESS.sendMessage(LONG_MESSAGE, server, CHANNEL));
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
