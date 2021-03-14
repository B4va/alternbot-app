package controllers.commons.commands;

import controllers.commons.Runner;
import controllers.general.commands.GeneralCommandsHandler;
import controllers.schedule.commands.ScheduleCommandsHandler;
import controllers.task.commands.TaskCommandsHandler;

/**
 * Contrôleur permettant d'initialiser la gestion des commandes utilisateur.
 */
public class CommandsController extends Runner<CommandsHandler> {

  @Override
  public Runner<CommandsHandler> init() {
    runnables.add(new GeneralCommandsHandler().init());
    runnables.add(new ScheduleCommandsHandler().init());
    runnables.add(new TaskCommandsHandler().init());
    return this;
  }
}
