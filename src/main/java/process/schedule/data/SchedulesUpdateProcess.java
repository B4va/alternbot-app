package process.schedule.data;

import models.Model;
import models.Schedule;
import models.Session;
import models.business.SessionChange;
import process.schedule.publication.ScheduleUpdatePublicationProcess;

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
    schedules.forEach(s -> {
      String data = iutDataFetchingProcess.fetch(s);
      List<SessionChange> changes = new ArrayList<>();
      if (nonNull(data)) {
        List<Session> newSessions = icalMappingProcess.map(data, s);
        Set<Session> oldSessions = s.getSessions();
        changes = doUpdate(changes, newSessions, oldSessions);
      }
      if (!changes.isEmpty()) {
        new ScheduleUpdatePublicationProcess().sendPublication(changes, s);
      }
    });
    new SessionsPurgeProcess().purgeAllUpdated();
  }

  private List<SessionChange> doUpdate(List<SessionChange> changes, List<Session> newSessions, Set<Session> oldSessions) {
    SessionUpdateProcess sessionUpdateProcess = new SessionUpdateProcess();
    // Passe les nouveaux cours en revue pour les enregistrer et marquer les cours qu'ils remplacent potentiellement.
    for (Session ns : newSessions) {
      changes = sessionUpdateProcess.updateWithNew(ns, oldSessions, changes);
    }
    // Passe les anciens cours en revue pour marquer les cours supprimés.
    for (Session os : oldSessions) {
      changes = sessionUpdateProcess.updateFromOld(os, newSessions, changes);
    }
    return changes;
  }
}
