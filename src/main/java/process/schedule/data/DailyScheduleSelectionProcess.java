package process.schedule.data;

import models.Schedule;
import models.Session;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * Process de sélection des cours d'un jour spécifique pour un emploi du temps donné.
 */
public class DailyScheduleSelectionProcess extends SessionSelection {

  /**
   * Sélectionne en bdd les cours d'un jour spécifique pour un edt donné.
   *
   * @param schedule emploi du temps
   * @param date     date des cours à récupérer
   * @return liste des cours du jour
   */
  public List<Session> select(Schedule schedule, Date date) {
    Calendar reqDate = Calendar.getInstance();
    reqDate.setTime(date);
    Calendar sessionDate = Calendar.getInstance();
    final List<Session> sessions = orderSessionsByDateAndStart(schedule.getSessions()
      .stream()
      .filter(session -> compareDates(reqDate, sessionDate, session))
      .collect(Collectors.toList()));
    return isNull(sessions) ? Collections.emptyList() : sessions;
  }

  private boolean compareDates(Calendar reqDate, Calendar sessionDate, Session session) {
    sessionDate.setTime(session.getDate());
    return reqDate.get(Calendar.DAY_OF_MONTH) == sessionDate.get(Calendar.DAY_OF_MONTH) &&
      reqDate.get(Calendar.MONTH) == sessionDate.get(Calendar.MONTH) &&
      reqDate.get(Calendar.YEAR) == sessionDate.get(Calendar.YEAR);
  }
}
