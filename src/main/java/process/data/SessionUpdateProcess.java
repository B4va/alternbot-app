package process.data;

import models.Session;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

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
  public void update(Session session, Set<Session> oldSessions) {
    if (!isSaved(session, oldSessions)) {
      // ScheduleUpdatePublicationProcess pub = new ScheduleUpdatePublicationProcess()
      List<Session> overlapped = oldSessions.stream()
        .filter(s -> isOverlapping(session, s))
        .collect(Collectors.toList());
      // pub.sendPublication(session, overlapped)
      overlapped.forEach(s -> {
        s.setUpdated(true);
        s.update();
      });
      session.create();
    }
  }

  private boolean isSaved(Session session, Set<Session> oldSessions) {
    return oldSessions.stream()
      .anyMatch(s -> session.getName().equals(s.getName()) &&
        ((isNull(session.getLocation()) && isNull(s.getLocation())) || session.getLocation().equals(s.getLocation())) &&
        ((isNull(session.getTeacher()) && isNull(s.getTeacher())) || session.getTeacher().equals(s.getTeacher())) &&
        session.getStart().equals(s.getStart()) &&
        session.getEnd().equals(s.getEnd()) &&
        session.getDate().equals(s.getDate()));
  }

  private boolean isOverlapping(Session newSession, Session oldSession) {
    return newSession.getDate().equals(oldSession.getDate()) &&
      (newSession.getStart().after(oldSession.getStart()) &&
        newSession.getStart().before(oldSession.getEnd())) ||
      (newSession.getEnd().after(oldSession.getDate()) &&
        newSession.getEnd().before(oldSession.getEnd()));
  }
}
