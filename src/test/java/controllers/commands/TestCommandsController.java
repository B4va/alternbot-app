package controllers.commands;

import controllers.commands.schedule.ScheduleCommandsHandler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Classe de test de {@link CommandsController}.
 */
public class TestCommandsController {

  private static final CommandsController commandsController = new CommandsController();
  private static boolean TEST1, TEST2, TEST3;

  @Test
  public void init_doit_initialiser_lensemble_des_commands_handler_de_lapp() {
    commandsController.setRunnables(new ArrayList<>());
    commandsController.init();
    assertAll(
      // ScheduleCommandsHandler
      () -> assertTrue(commandsController.getRunnables()
        .stream()
        .anyMatch(r -> r.getClass().equals(ScheduleCommandsHandler.class)))
    );
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
