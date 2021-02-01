package process.data;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.Collections;
import models.Session;
import models.business.SessionChange;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Process de mise à jour d'un cours.
 */
public class SessionUpdateProcess {

  /**
   * Met à jour les anciens cours enregistrés à partir d'un nouveau cours récupéré.
   *
   * @param session     cours récupéré
   * @param oldSessions ensemble des cours déjà enregistrés liés au même emploi du temps
   */
  public List<SessionChange> updateWithNew(Session session, Set<Session> oldSessions, List<SessionChange> changes) {
    if (oldSessions.stream().noneMatch(s -> s.equals(session))) {
      session.create();
      List<Session> overlapped = oldSessions.stream()
        .filter(s -> isOverlapping(session, s))
        .collect(Collectors.toList());
      changes.add(new SessionChange(session, overlapped));
      overlapped.forEach(s -> {
        s.setUpdated(true);
        s.update();
      });
    }
    return changes;
  }

  /**
   * Met à jour un cours à partir de l'ancien cours : détecte son éventuelle suppression.
   *
   * @param session     ancien cours
   * @param newSessions ensemble des cours récupérés
   */
  public List<SessionChange> updateFromOld(Session session, List<Session> newSessions, List<SessionChange> changes) {
    if (newSessions.stream().noneMatch(s -> s.equals(session)) && !isAlreadyChanged(session, changes)) {
      List<Session> deleted = new ArrayList<>();
      deleted.add(session);
      deleted.forEach(s -> {
        s.setUpdated(true);
        s.update();
      });
      changes.add(new SessionChange(null, deleted));
    }
    return changes;
  }

  private boolean isAlreadyChanged(Session session, List<SessionChange> changes) {
    return changes.stream()
      .anyMatch(c -> c.getReplacedSessions().stream().anyMatch(s -> s.equals(session)));
  }

  private boolean isOverlapping(Session newSession, Session oldSession) {
    if (!newSession.getDate().equals(oldSession.getDate())) return false;
    boolean overlappWithEnd = newSession.getEnd().after(oldSession.getStart()) &&
      newSession.getEnd().before(oldSession.getEnd());
    boolean overappWithStart = newSession.getStart().after(oldSession.getStart()) &&
      newSession.getStart().before(oldSession.getEnd());
    return overlappWithEnd || overappWithStart;
  }
}
