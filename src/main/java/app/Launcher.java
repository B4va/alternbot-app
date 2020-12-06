package app;

import controllers.commands.CommandsController;
import controllers.workers.WorkersController;

public class Launcher {

  private static final String COMMANDS = "COMMANDS";
  private static final String WORKERS = "WORKERS";

  public static void main(String[] args) {
    new Thread(new CommandsController(), COMMANDS).start();
    new Thread(new WorkersController(), WORKERS).start();
  }
}
