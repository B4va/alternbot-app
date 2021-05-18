package controllers.task.workers;

import controllers.commons.workers.DailyWorker;
import process.task.data.TaskPurgeProcess;

public class TaskDeleteWorker extends DailyWorker {
    public TaskDeleteWorker(int hour, int minute, long delay) {
        super(hour, minute, delay);
    }

    @Override
    protected void doRunOne() {
        new TaskPurgeProcess().purgePastDays();
    }

    @Override
    protected String getTask() {
        return "Suppression des taches passées";
    }
}
