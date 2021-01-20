package programs;

import models.Model;
import models.Schedule;
import models.Server;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.DbUtils;
import utils.EnvironmentVariablesUtils;
import utils.LoggerUtils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Initialisation de la base de données avec des objets pré-configurés.
 */
public class SeedLauncher {

  private static final Logger LOGGER = LoggerUtils.buildLogger(SeedLauncher.class);

  public static void main(String[] args) {
    LOGGER.info("RUN Seed.");
    try {
      LOGGER.info("Suppression des anciens objets.");
      deleteAll();
      LOGGER.info("Enregistrement des nouveaux objets.");
      Session session = DbUtils.getSessionFactory().openSession();
      Transaction transaction = session.beginTransaction();
      seed(session);
      transaction.commit();
      session.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      LOGGER.info("Processus terminé.");
    }
  }

  private static void seed(Session session) throws ParseException {
    List<Schedule> schedules = seedSchedules(session);
    seedServers(session, schedules);
    seedSessions(session, schedules);
  }

  private static void deleteAll() {
    Model.deleteAll(Server.class);
    Model.deleteAll(models.Session.class);
    Model.deleteAll(Schedule.class);
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
    String serveurTest1 = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.SERVER_TEST, "0123456789");
    String serveurTest2 = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.SERVER_TEST_2, serveurTest1);
    List<Server> servers = Arrays.asList(
      new Server(serveurTest1, schedules.get(0)),
      new Server(serveurTest2, schedules.get(1))
    );
    servers.forEach(session::persist);
    return servers;
  }

  private static List<models.Session> seedSessions(Session session, List<Schedule> schedules) throws ParseException {
    logSeed(models.Session.class);
    List<models.Session> sessions = Arrays.asList(
      new models.Session("Math", "Dupond Dupond", "F13", stringToDate("20-01-2020"), stringToTime("14:00"), stringToTime("15:00"), schedules.get(0)),
      new models.Session("Philosophie", "Loïc Steinmetz", "L32", stringToDate("20-01-2020"), stringToTime("16:00"), stringToTime("17:00"), schedules.get(0)),
      new models.Session("Anglais", "Marie Curie", "A12", stringToDate("22-01-2020"), stringToTime("11:00"), stringToTime("12:00"), schedules.get(1))
    );
    sessions.forEach(session::persist);
    return sessions;
  }

  private static <T> void logSeed(Class<T> c) {
    LOGGER.info("Seed des objets " + c.getSimpleName() + ".");
  }
}
