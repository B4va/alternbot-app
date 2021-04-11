package process.schedule.publication;

import models.dao.Session;
import process.commons.Publication;

import java.util.Date;
import java.util.List;

import static java.util.Objects.nonNull;
import static utils.DateUtils.dateToString;
import static utils.DateUtils.timeToString;

/**
 * Process permettant de formatter la liste des cours d'un jour spécifique.
 */
public class DailyScheduleFormattingProcess extends Publication {

  /**
   * Formatte la liste des cours d'un jour spécifique.
   *
   * @param sessions liste des cours du jour
   * @param date     jour recherché
   * @return message formatté
   */
  public String format(List<Session> sessions, Date date) {
    StringBuilder message = new StringBuilder("```\nEmploi du temps du ");
    message
      .append(dateToString(date))
      .append("\n");
    if (sessions.isEmpty()) {
      message.append("\nAucun cours prévu ce jour.");
    } else {
      sessions.forEach(s -> formatSession(message, s));
    }
    message.append("\n```");
    return message.toString();
  }

  private void formatSession(StringBuilder message, Session s) {
    message
      .append("\n")
      .append(timeToString(s.getStart()))
      .append(" - ")
      .append(timeToString(s.getEnd()))
      .append(" : ")
      .append(s.getName());
    if (nonNull(s.getTeacher())) {
      message
        .append(" - ")
        .append(s.getTeacher());
    }
    if (nonNull(s.getLocation())) {
      message
        .append(" - ")
        .append(s.getLocation());
    }
    if (nonNull(s.getType())) {
      message
        .append(" - ")
        .append(s.getType());
    }
  }

}
