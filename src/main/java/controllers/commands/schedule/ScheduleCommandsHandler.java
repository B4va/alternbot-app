package controllers.commands.schedule;

import controllers.commands.CommandsHandler;

public class ScheduleCommandsHandler extends CommandsHandler {

  @Override
  public void init() {
    runnables.add(new SchedulePublicationCommandListener());
    runnables.add(new ScheduleExportCommandListener());
  }
}
