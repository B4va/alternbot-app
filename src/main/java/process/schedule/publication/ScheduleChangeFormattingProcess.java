package process.schedule.publication;

import models.dao.Session;
import models.business.SessionChange;

import java.util.List;

import static java.util.Objects.nonNull;
import static utils.DateUtils.dateToString;
import static utils.DateUtils.timeToString;

/**
 * Process permettant de formatter une alerte de mise à jour de l'emploi du temps.
 */
public class ScheduleChangeFormattingProcess {

  /**
   * Formatte un message d'alerte d'une mise à jour de l'emploi du temps.
   *
   * @param changes modifications de l'emploi du temps
   * @return message
   */
  public String format(List<SessionChange> changes) {
    StringBuilder message = new StringBuilder("@everyone \nChangement d'emploi du temps :information_source:\n```");
    fillMessage(changes, message);
    message.append("\n```");
    return message.toString();
  }

  /**
   *
   * @param changes
   * @param message
   */
  private void fillMessage(List<SessionChange> changes, StringBuilder message) {
    for (SessionChange c : changes) {
      if (isNewSession(c)) {
        fillMessageWithNewSession(message, c);
        fillMessageWithReplacedSessions(message, c);
      } else {
        fillMessageWithDeletedSession(message, c);
      }
    }
  }

  private boolean isNewSession(SessionChange change) {
    return nonNull(change.getNewSession());
  }

  /**
   *
   * @param message
   * @param c
   */
  private void fillMessageWithNewSession(StringBuilder message, SessionChange c) {
    Session ns = c.getNewSession();
    message.append(c.getReplacedSessions().isEmpty() ? "\n\nNOUVEAU COURS :\n" : "\n\nMODIFICATION :\n");
    message
      .append(c.getNewSession().getName().toUpperCase())
      .append(" - le ").append(dateToString(ns.getDate()))
      .append(" de ").append(timeToString(ns.getStart()))
      .append(" à ").append(timeToString(ns.getEnd()));
    if (nonNull(ns.getTeacher())) {
      message.append(" (").append(ns.getTeacher()).append(")");
    }
    if (nonNull(ns.getLocation())) {
      message.append(" - ").append(ns.getLocation());
    }
  }

  private void fillMessageWithReplacedSessions(StringBuilder message, SessionChange c) {
    if (!c.getReplacedSessions().isEmpty()) {
      message.append("\n> Cours supprimés/modifiés :");
      for (Session s : c.getReplacedSessions()) {
        message
          .append("\n    - ")
          .append(s.getName())
          .append(" (")
          .append(timeToString(s.getStart()))
          .append(" - ")
          .append(timeToString(s.getEnd()))
          .append(")");
      }
    }
  }


  private void fillMessageWithDeletedSession(StringBuilder message, SessionChange c) {
    Session deletedSession = c.getReplacedSessions().get(0);
    message.append("\n\nCOURS SUPPRIME :\n");
    message
      .append(deletedSession.getName().toUpperCase())
      .append(" - le ").append(dateToString(deletedSession.getDate()))
      .append(" de ").append(timeToString(deletedSession.getStart()))
      .append(" à ").append(timeToString(deletedSession.getEnd()));
    if (nonNull(deletedSession.getTeacher())) {
      message.append(" (").append(deletedSession.getTeacher()).append(")");
    }
  }
}
