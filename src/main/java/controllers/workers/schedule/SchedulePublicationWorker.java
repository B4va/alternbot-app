package controllers.workers.schedule;

import controllers.workers.DailyWorker;
import process.publication.DailySchedulePublicationProcess;

import java.util.Calendar;

/**
 * Gère la publication régulière de l'emploi du temps sur l'ensemble des serveurs.
 */
public class SchedulePublicationWorker extends DailyWorker {

  public SchedulePublicationWorker(int hour, int minute, long delay) {
    super(hour, minute, delay);
  }

  @Override
  public void runOne() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, 1);
    new DailySchedulePublicationProcess().sendPublication(calendar.getTime());
  }

  @Override
  protected String getTask() {
    return "Publication quotidienne de l'emploi du temps";
  }
}
