package controllers.commands.general;

import controllers.commands.CommandsHandler;

/**
 * Gère les commandes utilisateur relative à des fonctions générales.
 */
public class GeneralCommandsHandler extends CommandsHandler {

  @Override
  public GeneralCommandsHandler init() {
    runnables.add(new HelpCommandListener());
    return this;
  }
}
