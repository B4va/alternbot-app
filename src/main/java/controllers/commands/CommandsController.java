package controllers.commands;

import controllers.Runner;
import controllers.commands.general.GeneralCommandsHandler;
import controllers.commands.schedule.ScheduleCommandsHandler;

/**
 * Contrôleur permettant d'initialiser la gestion des commandes utilisateur.
 */
public class CommandsController extends Runner<CommandsHandler> {

  @Override
  public Runner<CommandsHandler> init() {
    runnables.add(new GeneralCommandsHandler().init());
    runnables.add(new ScheduleCommandsHandler().init());
    return this;
  }
}
