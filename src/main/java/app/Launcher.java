package app;

import app.database.MigrationsLauncher;
import controllers.commands.CommandsController;
import controllers.workers.WorkersController;
import utils.EnvironmentVariablesUtils;

/**
 * Lanceur de l'application.
 */
public class Launcher {

  private static final String COMMANDS = "COMMANDS";
  private static final String WORKERS = "WORKERS";
  private static final String PROD_ENV = "prod";
  private static final String DEV_ENV = "dev";

  /**
   * Exécute les migrations si lancement en production, puis lance les controleurs gérant les
   * commandes et les opérations automatisées.
   *
   * @param args arguments programme
   */
  public static void main(String[] args) {
    runMigrations();
    new Thread(new CommandsController().init(), COMMANDS).start();
    new Thread(new WorkersController().init(), WORKERS).start();
  }

  private static void runMigrations() {
    String env = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.ENVIRONMENT, DEV_ENV);
    if (env.equals(PROD_ENV)) {
      MigrationsLauncher.main(new String[]{MigrationsLauncher.MIGRATE});
    }
  }
}
