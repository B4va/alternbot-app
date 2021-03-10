package process.schedule.data;

import models.Schedule;
import models.Session;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * Process permettant de sélectionner des cours d'un emploi du temps ayant lieu à une date donnée et après cette date.
 */
public class ScheduleExportSessionSelectionProcess extends SessionSelection {
  private static final Logger LOGGER = LoggerUtils.buildLogger(ScheduleExportSessionSelectionProcess.class);

  /**
   * Sélectionne et retourne une liste de cours ayant lieu à une date donnée ou après cette date depuis
   * un emploi du temps donné.
   *
   * @param schedule Emploi du temps contenant les cours à extraire.
   * @param from     Date à utiliser pour la sélection.
   * @return Liste de cours, liste vide si aucun résultat ou {@code null} en cas d'erreur de traitement.
   */
  public List<Session> select(Schedule schedule, Date from) {
    if (isNull(schedule) || isNull(from)) {
      LOGGER.warn("Un ou plusieurs paramètres sont null");
      return null;
    }

    Set<Session> sessions = schedule.getSessions();
    if (isNull(sessions) || sessions.isEmpty()) {
      LOGGER.warn("Aucun cours dans l'emploi du temps donné");
      return Collections.emptyList();
    }

    sessions = sessions.stream()
      .filter(sess -> sess.getDate().equals(from) || sess.getDate().after(from))
      .collect(Collectors.toSet());
    return sessions.isEmpty() ? Collections.emptyList() : orderSessionsByDateAndStart(new ArrayList<>(sessions));
  }
}
