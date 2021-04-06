package process.schedule.data;

import models.dao.ModelDAO;
import models.dao.Session;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Process permettant la supression des cours selon différents critères.
 */
public class SessionsPurgeProcess {
  private static final Logger LOGGER = LoggerUtils.buildLogger(SessionsPurgeProcess.class);

  /**
   * Supprime les cours ayant le statut 'mis à jour'.
   */
  public void purgeAllUpdated() {
    // todo
    List<Session> sessions = ModelDAO.readAll(Session.class);
    sessions.stream()
      .filter(Session::isUpdated)
      .forEach(Session::delete);
  }

  /**
   * Supprime les cours passés d'au moins le nombre de jours de donné.
   *
   * @param daysThreshold Nombre de jours, doit être strictement positif.
   * @return Nombre de cours supprimés.
   */
  public int purgePastDaysThreshold(int daysThreshold) {
    if (daysThreshold < 1) {
      LOGGER.warn("Paramètre incorrect, \"daysThreshold\" doit être strictement positif.");
      return 0;
    }

    AtomicInteger counter = new AtomicInteger(0);
    List<Session> sessions = ModelDAO.readAll(Session.class);
    sessions.stream()
      .filter(s -> s.isPast(daysThreshold))
      .forEach(s -> {
        s.delete();
        counter.getAndIncrement();
      });

    return counter.get();
  }
}
