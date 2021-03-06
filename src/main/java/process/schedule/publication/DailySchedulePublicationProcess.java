package process.schedule.publication;

import models.dao.ModelDAO;
import models.dao.Schedule;
import models.dao.Server;
import models.dao.Session;
import org.apache.logging.log4j.Logger;
import process.commons.Publication;
import process.schedule.data.DailyScheduleSelectionProcess;
import utils.LoggerUtils;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.nonNull;

/**
 * Process de publication de la liste des cours d'un jour spécifique.
 */
public class DailySchedulePublicationProcess extends Publication {

  private static final Logger LOGGER = LoggerUtils.buildLogger(DailySchedulePublicationProcess.class);

  /**
   * Envoi la liste des cours d'un jour spécifique à tous les serveur,
   * selon leur emploi du temps.
   *
   * @param date    date des cours à envoyer
   */
  public boolean sendPublication(Date date) {
    AtomicBoolean res = new AtomicBoolean(true);
    List<Schedule> schedules = ModelDAO.readAll(Schedule.class);
    schedules.forEach(schedule -> {
      List<Session> sessions = new DailyScheduleSelectionProcess().select(schedule, date);
      String message = new DailyScheduleFormattingProcess().format(sessions, date);
      schedule.getServers()
        .forEach(server -> {
          if (!sendMessage(message, server, SCHEDULE_CHANNEL)) res.set(false);
        });
    });
    return res.get();
  }

  /**
   * Envoi la liste des cours d'un jour spécifique à un serveur donné.
   *
   * @param date      date des cours à envoyer
   * @param serverRef référence du serveur concerné
   * @param channel   channel dans lequel publier l'edt
   */
  public void sendPublication(Date date, String serverRef, String channel) {
    try {
      Server server = Server.getByReference(serverRef);
      if (nonNull(server.getSchedule())) {
        List<Session> sessions = new DailyScheduleSelectionProcess().select(server.getSchedule(), date);
        String message = new DailyScheduleFormattingProcess().format(sessions, date);
        sendMessage(message, server, channel);
      }
    } catch (NoResultException e) {
      LOGGER.warn("Le serveur n'est pas référencé : {}.", serverRef);
    }
  }
}
