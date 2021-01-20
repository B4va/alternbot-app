package process;

import models.Server;
import models.Session;
import models.businessmodels.SessionChange;

import javax.security.auth.login.LoginException;
import java.util.List;

import static java.util.Objects.nonNull;
import static utils.DateUtils.dateToString;
import static utils.DateUtils.timeToString;

/**
 * Process de notification des mises à jour de l'emploi du temps.
 */
public class ScheduleUpdatePublicationProcess extends Publication {

  private static final String CHANNEL = "emploi-du-temps";

  /**
   * Envoie une publication listant les mises à jour de l'emploi du temps
   * sur le serveur Discord concerné.
   *
   * @param changes modifications de l'emploi du temps
   * @param server  serveur Discord
   * @throws LoginException       erreur de connexion au serveur Discord
   * @throws InterruptedException connexion interrompue
   */
  public void sendPublication(List<SessionChange> changes, Server server) throws LoginException, InterruptedException {
    StringBuilder message = new StringBuilder("@everyone \nChangement d'emploi du temps :information_source:\n```\n");
    fillMessage(changes, message);
    message.append("\n```");
    sendMessage(message.toString(), server, CHANNEL);
  }

  private void fillMessage(List<SessionChange> changes, StringBuilder message) {
    for (SessionChange c : changes) {
      fillMessageWithNewSession(message, c);
      fillMessageWithReplacedSessions(message, c);
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

  private void fillMessageWithNewSession(StringBuilder message, SessionChange c) {
    Session ns = c.getNewSession();
    message.append(c.getReplacedSessions().isEmpty() ? "\nNOUVEAU COURS :\n" : "\nMODIFICATION :\n");
    message
      .append(c.getNewSession().getName().toUpperCase())
      .append(" - le ").append(dateToString(ns.getDate()))
      .append(" de ").append(timeToString(ns.getStart()))
      .append(" à ").append(timeToString(ns.getStart()));
    if (nonNull(ns.getTeacher())) {
      message.append(" (").append(ns.getTeacher()).append(")");
    }
    if (nonNull(ns.getLocation())) {
      message.append(" - ").append(ns.getLocation());
    }
  }
}
