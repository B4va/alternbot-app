package controllers.workers.schedule;

import controllers.workers.DailyWorker;

public class SchedulePublicationWorker extends DailyWorker {

  private static final String TASK = "Publication quotidienne EDT";

  public SchedulePublicationWorker(int hour, int minute, long delay) {
    super(hour, minute, delay);
  }

  @Override
  public void runOne() {
    // TODO
  }

  @Override
  protected String getTask() {
    return TASK;
  }
}
