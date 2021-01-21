package process.data;

import models.Session;
import models.business.SessionChange;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Process de mise à jour d'un cours.
 */
public class SessionUpdateProcess {

  /**
   * Met à jour un cours dont les données ont été récupérées.
   *
   * @param session     cours récupéré
   * @param oldSessions ensemble des cours déjà enregistrés liés au même emploi du temps
   */
  // todo : finaliser après merge du process d'envoi de l'alerte en cas de modif de l'edt
  public List<SessionChange> update(Session session, Set<Session> oldSessions, List<SessionChange> changes) {
    if (!isSaved(session, oldSessions)) {
      session.create();
      List<Session> overlapped = oldSessions.stream()
        .filter(s -> isOverlapping(session, s))
        .collect(Collectors.toList());
      SessionChange change = new SessionChange(session, overlapped);
      changes.add(change);
      overlapped.forEach(s -> {
        s.setUpdated(true);
        s.update();
      });
    }
    return changes;
  }

  private boolean isSaved(Session session, Set<Session> oldSessions) {
    return oldSessions.stream()
      .anyMatch(s -> session.getName().equals(s.getName()) &&
        session.getStart().equals(s.getStart()) &&
        session.getEnd().equals(s.getEnd()) &&
        session.getDate().equals(s.getDate()));
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
