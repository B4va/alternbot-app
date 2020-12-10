package controllers.commands.schedule;

import controllers.commands.CommandsHandler;

/**
 * Gère les commandes utilisateur relative à l'emploi du temps.
 */
public class ScheduleCommandsHandler extends CommandsHandler {

  @Override
  public void init() {
    runnables.add(new SchedulePublicationCommandListener());
    runnables.add(new ScheduleExportCommandListener());
  }
}
