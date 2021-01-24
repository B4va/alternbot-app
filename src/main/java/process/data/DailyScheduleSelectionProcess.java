package process.data;

import models.Schedule;
import models.Session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Process de sélection des cours d'un jour spécifique pour un emploi du temps donné.
 */
public class DailyScheduleSelectionProcess {

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
    return new ArrayList<>(schedule.getSessions())
      .stream()
      .filter(session -> compareDates(reqDate, sessionDate, session))
      .collect(Collectors.toList());
  }

  private boolean compareDates(Calendar reqDate, Calendar sessionDate, Session session) {
    sessionDate.setTime(session.getDate());
    return reqDate.get(Calendar.DAY_OF_MONTH) == sessionDate.get(Calendar.DAY_OF_MONTH) &&
      reqDate.get(Calendar.MONTH) == sessionDate.get(Calendar.MONTH) &&
      reqDate.get(Calendar.YEAR) == sessionDate.get(Calendar.YEAR);
  }
}
