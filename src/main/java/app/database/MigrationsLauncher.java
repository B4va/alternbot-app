package app.database;

import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import utils.EnvironmentVariablesUtils;
import utils.LoggerUtils;

/**
 * Lanceur des processus migrations via {@link Flyway}.
 */
public class MigrationsLauncher {

  public static final String MIGRATE = "-migrate";
  public static final String CLEAN = "-clean";
  private static final Logger LOGGER = LoggerUtils.buildLogger(MigrationsLauncher.class);
  private static final String LOCATION = "/migrations";

  /**
   * Commandes disponibles : migrate, repair, clean.
   */
  public static void main(String[] args) {
    try {
      final String url = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.DB_URL);
      final String user = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.DB_USER);
      final String password = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.DB_PASSWORD);
      Flyway flyway = Flyway.configure()
        .locations("migrations")
        .dataSource(url, user, password).load();
      execute(flyway, args);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      LOGGER.info("Processus terminée.");
    }
  }

  private static void execute(Flyway flyway, String[] args) {
    if (args.length < 1) {
      LOGGER.error("Paramètre manquant.");
    } else {
      switch (args[0]) {
        case MIGRATE:
          LOGGER.info("RUN Migrate.");
          flyway.migrate();
          break;
        case CLEAN:
          LOGGER.info("RUN Clean.");
          flyway.clean();
          break;
        default:
          LOGGER.error("Paramètre inconnu.");
          break;
      }
    }
  }
}
