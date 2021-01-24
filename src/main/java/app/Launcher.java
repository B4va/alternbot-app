package app;

import controllers.commands.CommandsController;
import controllers.workers.WorkersController;
import org.apache.logging.log4j.Logger;
import programs.MigrationsLauncher;
import utils.EnvironmentVariablesUtils;
import utils.LoggerUtils;

import javax.security.auth.login.LoginException;

import static utils.JDAUtils.initializeJDA;

/**
 * Lanceur de l'application.
 */
public class Launcher {

  private static final Logger LOGGER = LoggerUtils.buildLogger(Launcher.class);
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
    initJDA();
    initThreads();
  }

  private static void runMigrations() {
    String env = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.ENVIRONMENT, DEV_ENV);
    if (env.equals(PROD_ENV)) {
      MigrationsLauncher.main(new String[]{MigrationsLauncher.MIGRATE});
    }
  }

  private static void initJDA() {
    try {
      initializeJDA();
    } catch (LoginException | InterruptedException e) {
      LOGGER.fatal("Impossible d'établir la connexion JDA.");
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private static void initThreads() {
    new Thread(new CommandsController().init(), COMMANDS).start();
    new Thread(new WorkersController().init(), WORKERS).start();
  }
}
