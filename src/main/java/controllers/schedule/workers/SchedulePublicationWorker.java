package controllers.schedule.workers;

import controllers.commons.workers.DailyWorker;
import process.schedule.publication.DailySchedulePublicationProcess;

import java.util.Calendar;

/**
 * Gère la publication régulière de l'emploi du temps sur l'ensemble des serveurs.
 */
public class SchedulePublicationWorker extends DailyWorker {

  public SchedulePublicationWorker(int hour, int minute, long delay) {
    super(hour, minute, delay);
  }

  public SchedulePublicationWorker(int hour, int minute, long delay, boolean skipSunday) {
    super(hour, minute, delay, skipSunday);
  }

  @Override
  public void doRunOne() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, 1);
    new DailySchedulePublicationProcess().sendPublication(calendar.getTime());
  }

  @Override
  protected String getTask() {
    return "Publication quotidienne de l'emploi du temps";
  }
}
