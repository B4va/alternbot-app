package controllers.task.commands;

import controllers.commons.commands.CommandListener;
import controllers.commons.commands.CommandsController;
import controllers.commons.commands.CommandsHandler;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test de {@link CommandsController}.
 */
public class TestCommandsController {

  private static final CommandsController commandsController = new CommandsController();
  private static boolean TEST1, TEST2, TEST3;

  @Test
  public void init_doit_enregistrer_des_handlers() {
    CommandsController commandsController = (CommandsController) new CommandsController().init();
    assertFalse(commandsController.getRunnables().isEmpty());
  }

  @Test
  public void run_doit_pouvoir_lancer_un_CommandsHandler_unique() {
    List<CommandsHandler> handlers = mockUniqueCommandsHandler();
    commandsController.setRunnables(handlers);
    commandsController.run();
    assertTrue(TEST1);
  }

  @Test
  public void run_doit_pouvoir_lancer_plusieurs_CommandsHandler() {
    List<CommandsHandler> handlers = mockMultipleCommandsHandlers();
    commandsController.setRunnables(handlers);
    commandsController.run();
    assertAll(
      () -> assertTrue(TEST2),
      () -> assertTrue(TEST3)
    );
  }

  private List<CommandsHandler> mockUniqueCommandsHandler() {
    CommandListener commandListener = new CommandListener() {
      @Override
      public void run() {
        TEST1 = true;
      }
      @Override
      public void handleCommand(GuildMessageReceivedEvent event, List<String> message) {
      }
      @Override
      public String getCommand() {
        return null;
      }
    };
    CommandsHandler commandsHandler = new CommandsHandler() {
      @Override
      public CommandsHandler init() {
        runnables.add(commandListener);
        return this;
      }
    }.init();
    return Collections.singletonList(commandsHandler);
  }

  private List<CommandsHandler> mockMultipleCommandsHandlers() {
    CommandListener commandListener1 = new CommandListener() {
      @Override
      public void run() {
        TEST2 = true;
      }
      @Override
      public void handleCommand(GuildMessageReceivedEvent event, List<String> message) {
      }
      @Override
      public String getCommand() {
        return null;
      }
    };
    CommandsHandler commandsHandler1 = new CommandsHandler() {
      @Override
      public CommandsHandler init() {
        runnables.add(commandListener1);
        return this;
      }
    }.init();
    CommandListener commandListener2 = new CommandListener() {
      @Override
      public void run() {
        TEST3 = true;
      }
      @Override
      public void handleCommand(GuildMessageReceivedEvent event, List<String> message) {
      }
      @Override
      public String getCommand() {
        return null;
      }
    };
    CommandsHandler commandsHandler2 = new CommandsHandler() {
      @Override
      public CommandsHandler init() {
        runnables.add(commandListener2);
        return this;
      }
    }.init();
    return Arrays.asList(commandsHandler1, commandsHandler2);
  }
}
