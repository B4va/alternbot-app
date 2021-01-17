package process;

import models.Model;
import models.Schedule;
import models.Session;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import java.util.*;

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
      if (nonNull(data)) {
        List<Session> newSessions = icalMappingProcess.map(data, s);
        Set<Session> oldSessions = s.getSessions();
        newSessions.forEach(ns -> sessionUpdateProcess.update(ns, oldSessions));
      }
      LOGGER.debug("EDT #{} : OK", s.getId());
    });
    LOGGER.debug("Purge des cours mis à jour.");
    new SessionsPurgeProcess().purge();
    LOGGER.debug("Purge OK.");
    LOGGER.info("Process terminé.");
  }

  public static void main(String[] args) {
//    new Schedule("Test", "https://dptinfo.iutmetz.univ-lorraine.fr/lna/agendas/ical.php?ical=c3459ed54a02149").create();
    new SchedulesUpdateProcess().update();
  }
}
