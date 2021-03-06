package programs;

import models.dao.ModelDAO;
import models.dao.Schedule;
import models.dao.Server;
import models.dao.Task;
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
import static utils.EnvironmentVariablesUtils.*;

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
    List<Server> servers = seedServers(session, schedules);
    seedSessions(session, schedules);
    seedTasks(session, servers);
  }

  private static void deleteAll() {
    ModelDAO.deleteAll(Task.class);
    ModelDAO.deleteAll(Server.class);
    ModelDAO.deleteAll(models.dao.Session.class);
    ModelDAO.deleteAll(Schedule.class);
  }

  private static List<Schedule> seedSchedules(Session session) {
    logSeed(Schedule.class);
    List<Schedule> schedules = Arrays.asList(
      new Schedule("LP Génie logiciel", EnvironmentVariablesUtils.getString(SCHEDULE_URL, "url1.com")),
      new Schedule("BUT Génie mécanique et productique", "url2.com"),
      new Schedule("LP Statistique et informatique décisionnelle", "url3.com"),
      new Schedule("BUT Techniques de commercialisation", "url4.com"),
      new Schedule("LP Acquisition de données, qualification d'appareillages en milieu industriel", "url5.com")
    );
    schedules.forEach(session::persist);
    return schedules;
  }

  private static List<Server> seedServers(Session session, List<Schedule> schedules) {
    logSeed(Server.class);
    String serveurTest1 = EnvironmentVariablesUtils.getString(SERVER_TEST, "0123456789");
    String serveurTest2 = EnvironmentVariablesUtils.getString(SERVER_TEST_2, "9876543210");
    List<Server> servers = Arrays.asList(
      new Server(serveurTest1, schedules.get(0)),
      new Server(serveurTest2, schedules.get(1))
    );
    servers.forEach(session::persist);
    return servers;
  }

  private static List<models.dao.Session> seedSessions(Session session, List<Schedule> schedules) throws ParseException {
    logSeed(models.dao.Session.class);
    List<models.dao.Session> sessions = Arrays.asList(
      new models.dao.Session("Math", "Dupond Dupond", "F13", stringToDate("20-01-2020"), stringToTime("14:00"), stringToTime("15:00"), schedules.get(0), "TD"),
      new models.dao.Session("Philosophie", "Loïc Steinmetz", "L32", stringToDate("20-01-2020"), stringToTime("16:00"), stringToTime("17:00"), schedules.get(0), "CM"),
      new models.dao.Session("Anglais", "Marie Curie", "A12", stringToDate("22-01-2020"), stringToTime("11:00"), stringToTime("12:00"), schedules.get(1), "EXAM")
    );
    sessions.forEach(session::persist);
    return sessions;
  }

  private static List<Task> seedTasks(Session session, List<Server> servers) throws ParseException {
    logSeed(Task.class);
    List<Task> tasks = Arrays.asList(
      new Task("TP GraphQL", stringToDate("16-01-2021"), stringToTime("08:00"), servers.get(0)),
      new Task("Projet POO", stringToDate("14-03-2021"), stringToTime("00:00"), servers.get(0)),
      new Task("Exos JaveEE", stringToDate("09-02-2021"), stringToTime("17:30"), servers.get(1)),
      new Task("Projet BDR", stringToDate("06-04-2021"), stringToTime("00:00"), servers.get(1))
    );
    tasks.forEach(session::persist);
    return tasks;
  }

  private static <T> void logSeed(Class<T> c) {
    LOGGER.info("Seed des objets " + c.getSimpleName() + ".");
  }
}
