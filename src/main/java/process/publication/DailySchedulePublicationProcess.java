package process.publication;

import models.Model;
import models.Schedule;
import models.Server;
import models.Session;
import process.data.DailyScheduleSelectionProcess;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Objects.nonNull;

/**
 * Process de publication de la liste des cours d'un jour spécifique.
 */
public class DailySchedulePublicationProcess extends Publication {


  /**
   * Envoi la liste des cours d'un jour spécifique à tous les serveur,
   * selon leur emploi du temps.
   *
   * @param date    date des cours à envoyer
   */
  public void sendPublication(Date date) {
    List<Schedule> schedules = Model.readAll(Schedule.class);
    schedules.forEach(schedule -> {
      List<Session> sessions = new DailyScheduleSelectionProcess().select(schedule, date);
      String message = new DailyScheduleFormattingProcess().format(sessions, date);
      schedule.getServers().forEach(server -> sendMessage(message, server, SCHEDULE_CHANNEL));
    });
  }

  /**
   * Envoi la liste des cours d'un jour spécifique à un serveur donné.
   *
   * @param date      date des cours à envoyer
   * @param serverRef référence du serveur concerné
   * @param channel   channel dans lequel publier l'edt
   */
  public void sendPublication(Date date, String serverRef, String channel) {
    Server server = Model.readAll(Server.class).stream()
      .filter(s -> s.getReference().equals(serverRef))
      .findAny()
      .orElse(null);
    if (nonNull(server) && nonNull(server.getSchedule())) {
      List<Session> sessions = new DailyScheduleSelectionProcess().select(server.getSchedule(), date);
      String message = new DailyScheduleFormattingProcess().format(sessions, date);
      sendMessage(message, server, channel);
    }
  }
}
