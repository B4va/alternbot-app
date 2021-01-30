package controllers.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test de {@link CommandListener}.
 */
public class TestCommandListener {

  private static CommandListener LISTENER;
  private static final String SIMPLE_COMMAND = "$cmd";
  private static final String PARAMETER = "-p1";
  private static final String PARAMETER_VALUE = "test";
  private static String COMPLEX_COMMAND;

  @BeforeAll
  public static void init() {
    LISTENER = new CommandListener() {
      @Override
      protected String getCommand() {
        return null;
      }
      @Override
      protected void handleCommand(GuildMessageReceivedEvent event, List<String> message) {
      }
    };
    COMPLEX_COMMAND = SIMPLE_COMMAND + " " + PARAMETER + " " + PARAMETER_VALUE;
  }

  @Test
  public void testParseCommand_simple_command() {
    List<String> cmd = LISTENER.parseCommand(SIMPLE_COMMAND);
    assertAll(
      () -> assertEquals(cmd.size(), 1),
      () -> assertTrue(cmd.contains(SIMPLE_COMMAND))
    );
  }

  @Test
  public void testParseCommand_parameterized_command() {
    List<String> cmd = LISTENER.parseCommand(COMPLEX_COMMAND);
    assertAll(
      () -> assertEquals(cmd.size(), 3),
      () -> assertTrue(cmd.containsAll(Arrays.asList(SIMPLE_COMMAND, PARAMETER, PARAMETER_VALUE)))
    );
  }

  @Test
  public void testHasParameter_true() {
    List<String> cmd = LISTENER.parseCommand(COMPLEX_COMMAND);
    assertTrue(LISTENER.hasParameter(cmd, PARAMETER));
  }

  @Test
  public void testHasParameter_false() {
    List<String> cmd = LISTENER.parseCommand(SIMPLE_COMMAND);
    assertFalse(LISTENER.hasParameter(cmd, PARAMETER));
  }

  @Test
  public void testGetParameter_ok() {
    List<String> cmd = LISTENER.parseCommand(COMPLEX_COMMAND);
    String param = LISTENER.getParameter(cmd, PARAMETER);
    assertEquals(param, PARAMETER_VALUE);
  }

  @Test
  public void testGetParameter_no_value() {
    List<String> cmd = LISTENER.parseCommand(SIMPLE_COMMAND + " " + PARAMETER);
    String param = LISTENER.getParameter(cmd, PARAMETER);
    assertNull(param);
  }

  @Test
  public void testGetParameter_no_parameter() {
    List<String> cmd = LISTENER.parseCommand(SIMPLE_COMMAND);
    String param = LISTENER.getParameter(cmd, PARAMETER);
    assertNull(param);
  }
}
