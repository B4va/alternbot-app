package process.schedule.publication;

import models.Schedule;
import models.business.SessionChange;
import process.commons.Publication;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Process de notification des mises à jour de l'emploi du temps.
 */
public class ScheduleUpdatePublicationProcess extends Publication {

  /**
   * Envoie une publication listant les mises à jour de l'emploi du temps
   * sur le serveur Discord concerné.
   *
   * @param changes modifications de l'emploi du temps
   * @param schedule emploi du temps concerné par les modifications
   */
  public boolean sendPublication(List<SessionChange> changes, Schedule schedule) {
    AtomicBoolean res = new AtomicBoolean(true);
    String message = new ScheduleChangeFormattingProcess().format(changes);
    schedule.getServers().forEach(s -> {
      if (!sendMessage(message, s, SCHEDULE_CHANNEL)) res.set(false);
    });
    return res.get();
  }
}
