package process.publication;

import models.Schedule;
import models.Server;
import models.business.SessionChange;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import javax.security.auth.login.LoginException;
import java.util.List;

/**
 * Process de notification des mises à jour de l'emploi du temps.
 */
public class ScheduleUpdatePublicationProcess extends Publication {

  private static final Logger LOGGER = LoggerUtils.buildLogger(ScheduleUpdatePublicationProcess.class);

  /**
   * Envoie une publication listant les mises à jour de l'emploi du temps
   * sur le serveur Discord concerné.
   *
   * @param changes modifications de l'emploi du temps
   * @param schedule emploi du temps concerné par les modifications
   */
  public void sendPublication(List<SessionChange> changes, Schedule schedule) {
    String message = new ScheduleChangeFormattingProcess().format(changes);
    schedule.getServers().forEach(s -> sendMessage(message, s, SCHEDULE_CHANNEL));
  }
}
