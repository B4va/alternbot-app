package controllers.schedule.commands;

import controllers.commons.commands.CommandsHandler;

/**
 * Gère les commandes utilisateur relative à l'emploi du temps.
 */
public class ScheduleCommandsHandler extends CommandsHandler {

  @Override
  public CommandsHandler init() {
    runnables.add(new SchedulePublicationCommandListener());
    return this;
  }
}
