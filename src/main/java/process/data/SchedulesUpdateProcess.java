package process.data;

import models.Model;
import models.Schedule;
import models.Session;
import models.business.SessionChange;
import org.apache.logging.log4j.Logger;
import process.publication.ScheduleUpdatePublicationProcess;
import utils.LoggerUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

/**
 * Process de récupération des données IUT.
 */
public class SchedulesUpdateProcess {

  private static final Logger LOGGER = LoggerUtils.buildLogger(SchedulesUpdateProcess.class);

  /**
   * Met à jour les données de la base en récupérant les données de l'IUT.
   */
  public void update() {
    LOGGER.info("Lancement du process de mise à jour des données IUT.");
    List<Schedule> schedules = Model.readAll(Schedule.class);
    IutDataFetchingProcess iutDataFetchingProcess = new IutDataFetchingProcess();
    IcalMappingProcess icalMappingProcess = new IcalMappingProcess();
    SessionUpdateProcess sessionUpdateProcess = new SessionUpdateProcess();
    schedules.forEach(s -> {
      LOGGER.debug("Mise à jour des cours de l'edt #{} - promotion : {}", s.getId(), s.getPromotion());
      String data = iutDataFetchingProcess.fetch(s);
      List<SessionChange> changes = Collections.emptyList();
      if (nonNull(data)) {
        List<Session> newSessions = icalMappingProcess.map(data, s);
        Set<Session> oldSessions = s.getSessions();
        for (Session ns : newSessions) {
          changes = sessionUpdateProcess.update(ns, oldSessions, changes);
        }
      }
      new ScheduleUpdatePublicationProcess().sendPublication(changes, s);
      LOGGER.debug("EDT #{} : OK", s.getId());
    });
    LOGGER.debug("Purge des cours mis à jour.");
    new SessionsPurgeProcess().purge();
    LOGGER.debug("Purge OK.");
    LOGGER.info("Process terminé.");
  }
}
