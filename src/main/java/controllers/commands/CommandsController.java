package controllers.commands;

import controllers.Runner;
import controllers.commands.schedule.ScheduleCommandsHandler;

public class CommandsController extends Runner<CommandsHandler> {

  @Override
  protected void init() {
    runnables.add(new ScheduleCommandsHandler());
  }
}
