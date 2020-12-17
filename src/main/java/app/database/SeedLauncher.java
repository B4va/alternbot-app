package app.database;

import models.Schedule;
import models.Server;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.DbUtils;
import utils.EnvironmentVariablesUtils;
import utils.LoggerUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Initialisation de la base de données avec des objets pré-configurés.
 */
public class SeedLauncher {

  private static final Logger LOGGER = LoggerUtils.buildLogger(SeedLauncher.class);

  public static void main(String[] args) {
    LOGGER.info("RUN Seed.");
    try {
      Session session = DbUtils.getSessionFactory().openSession();
      Transaction transaction = session.beginTransaction();
      LOGGER.info("Suppression des objets.");
      deleteAll(session);
      seed(session);
      transaction.commit();
      session.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      LOGGER.info("Processus terminé.");
    }
  }

  private static void seed(Session session) {
    List<Schedule> schedules = seedSchedules(session);
    seedServers(session, schedules);
  }

  private static void deleteAll(Session session) {
    DbUtils.deleteAll(session, Server.class);
    DbUtils.deleteAll(session, Schedule.class);
  }

  private static List<Schedule> seedSchedules(Session session) {
    logSeed(Schedule.class);
    List<Schedule> schedules = Arrays.asList(
      new Schedule("LP Génie logiciel"),
      new Schedule("BUT Génie mécanique et productique"),
      new Schedule("LP Statistique et informatique décisionnelle"),
      new Schedule("BUT Techniques de commercialisation"),
      new Schedule("LP Acquisition de données, qualification d'appareillages en milieu industriel")
    );
    schedules.forEach(session::persist);
    return schedules;
  }

  private static List<Server> seedServers(Session session, List<Schedule> schedules) {
    logSeed(Server.class);
    String serveurTest1 = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.SERVEUR_TEST, "0123456789");
    String serveurTest2 = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.SERVEUR_TEST_2, serveurTest1);
    List<Server> servers = Arrays.asList(
      new Server(serveurTest1, schedules.get(0)),
      new Server(serveurTest2, schedules.get(1))
    );
    servers.forEach(session::persist);
    return servers;
  }

  private static <T> void logSeed(Class<T> c) {
    LOGGER.info("Seed des objets " + c.getSimpleName() + ".");
  }
}
