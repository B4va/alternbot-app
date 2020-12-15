package app.database;

import models.Schedule;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.DbUtils;
import utils.LoggerUtils;

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
    seedSchedules(session);
  }

  private static void seedSchedules(Session session) {
    logSeed(Schedule.class);
    DbUtils.deleteAll(session, Schedule.class);
    session.persist(new Schedule("LP Génie logiciel"));
    session.persist(new Schedule("BUT Génie mécanique et productique"));
    session.persist(new Schedule("LP Statistique et informatique décisionnelle"));
    session.persist(new Schedule("BUT Techniques de commercialisation"));
    session.persist(new Schedule("LP Acquisition de données, qualification d'appareillages en milieu industriel"));
  }

  private static <T> void logSeed(Class<T> c) {
    LOGGER.info("Seed des objets " + c.getSimpleName() + ".");
  }
}
