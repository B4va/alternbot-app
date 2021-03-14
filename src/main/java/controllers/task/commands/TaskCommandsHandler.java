package controllers.task.commands;

import controllers.commons.commands.CommandsHandler;

/**
 * Gère les commandes utilisateur relative aux tâches/travaux.
 */
public class TaskCommandsHandler extends CommandsHandler {

  @Override
  public CommandsHandler init() {
    runnables.add(new TaskOperationsCommandListener());
    return this;
  }
}
