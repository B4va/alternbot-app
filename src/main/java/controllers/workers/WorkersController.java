package controllers.workers;

import controllers.Runner;
import controllers.workers.schedule.ScheduleWorkersHandler;

public class WorkersController extends Runner<WorkersHandler> {

  @Override
  protected void init() {
    runnables.add(new ScheduleWorkersHandler());
  }
}