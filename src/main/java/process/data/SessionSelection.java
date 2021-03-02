package process.data;

import models.Session;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * Process regroupant différentes méthodes se rapportant à la sélection/tri des cours.
 */
public abstract class SessionSelection {
  private static final Logger LOGGER = LoggerUtils.buildLogger(SessionSelection.class);

  /**
   * Retourne une liste de cours ordonnés chronologiquement à partir d'une liste de cours donnée.
   *
   * @param sessions Liste de cours à utiliser.
   * @return Nouvelle liste de cours ordonnée ou {@code null} en cas d'erreur.
   */
  public List<Session> orderSessionsByDateAndStart(List<Session> sessions) {
    if (isNull(sessions) || sessions.isEmpty()) {
      LOGGER.debug("La liste de cours donnée est null ou vide.");
      return null;
    }

    Comparator<Session> byDate = Comparator.comparing(Session::getDate, Comparator.nullsLast(Comparator.naturalOrder()));
    Comparator<Session> byStart = Comparator.comparing(Session::getStart, Comparator.nullsLast(Comparator.naturalOrder()));
    return sessions.stream()
      .sorted(byDate.thenComparing(byStart))
      .collect(Collectors.toList());
  }
}
