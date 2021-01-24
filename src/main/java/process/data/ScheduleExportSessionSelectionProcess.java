package process.data;

import models.Schedule;
import models.Session;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * Process permettant de sélectionner des cours d'un emploi du temps ayant lieu à une date donnée et après cette date.
 */
public class ScheduleExportSessionSelectionProcess {
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

    final Set<Session> sessions = schedule.getSessions();
    if (isNull(sessions) || sessions.isEmpty()) {
      LOGGER.warn("Aucun cours dans l'emploi du temps donné");
      return Collections.emptyList();
    }

    // Cours sélectionnés puis triés.
    // Le tri se fait d'abord sur la date, par ordre croissant. Si 2 cours ont la même date, le tri se fait sur l'heure
    // de début du cours.
    return sessions.stream()
      .filter(sess -> sess.getDate().equals(from) || sess.getDate().after(from))
      .sorted((s1, s2) -> {
        if (s1.getDate().compareTo(s2.getDate()) < 0)
          return -1;
        else if (s1.getDate().compareTo(s2.getDate()) > 0)
          return 1;
        else
          return Integer.compare(s1.getStart().compareTo(s2.getStart()), 0);
      })
      .collect(Collectors.toList());
  }
}
