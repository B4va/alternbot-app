package app;

import app.database.MigrationsLauncher;
import app.database.SeedLauncher;
import models.Test;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import utils.DbUtils;
import utils.EnvironmentVariablesUtils;

import java.util.List;

import static utils.LoggerUtils.buildLogger;

/**
 * Classe de test.
 */
public class MainTest {

  private static final Logger LOGGER = buildLogger(MainTest.class);

  public static void main(String[] args) {
    try {
      MigrationsLauncher.main(new String[]{MigrationsLauncher.MIGRATE});
      SeedLauncher.main(null);
      Session session = DbUtils.getSessionFactory().openSession();
      List<Test> tests = DbUtils.getAll(session, Test.class);
      session.close();
      tests.forEach(test -> LOGGER.info("Id : " + test.getId() + " - Text : " + test.getText()));
      JDA jda = JDABuilder
        .createDefault(EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.BOT_TOKEN))
        .build();
      jda.awaitReady();
      jda.shutdown();
      LOGGER.info("L'environnement est correctement configuré.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
