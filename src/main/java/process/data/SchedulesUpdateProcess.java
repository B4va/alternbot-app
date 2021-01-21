package process.data;

import models.Model;
import models.Schedule;
import models.Session;
import models.business.SessionChange;
import process.publication.ScheduleUpdatePublicationProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

/**
 * Process de récupération des données IUT.
 */
public class SchedulesUpdateProcess {

  /**
   * Met à jour les données de la base en récupérant les données de l'IUT.
   */
  public void update() {
    List<Schedule> schedules = Model.readAll(Schedule.class);
    IutDataFetchingProcess iutDataFetchingProcess = new IutDataFetchingProcess();
    IcalMappingProcess icalMappingProcess = new IcalMappingProcess();
    SessionUpdateProcess sessionUpdateProcess = new SessionUpdateProcess();
    schedules.forEach(s -> {
      String data = iutDataFetchingProcess.fetch(s);
      List<SessionChange> changes = new ArrayList<>();
      if (nonNull(data)) {
        List<Session> newSessions = icalMappingProcess.map(data, s);
        Set<Session> oldSessions = s.getSessions();
        for (Session ns : newSessions) {
          changes = sessionUpdateProcess.update(ns, oldSessions, changes);
        }
      }
      if (!changes.isEmpty()) {
        new ScheduleUpdatePublicationProcess().sendPublication(changes, s);
      }
    });
    new SessionsPurgeProcess().purge();
  }
}
