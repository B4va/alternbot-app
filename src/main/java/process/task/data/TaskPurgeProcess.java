package process.task.data;

import models.dao.ModelDAO;
import models.dao.Task;
import org.apache.logging.log4j.Logger;
import process.schedule.data.SessionsPurgeProcess;
import utils.LoggerUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskPurgeProcess {
    private static final Logger LOGGER = LoggerUtils.buildLogger(SessionsPurgeProcess.class);




    //Supprime les taches considérés comme passé
    public int purgePastDays() {
        AtomicInteger counter = new AtomicInteger(0);
        List<Task> task = ModelDAO.readAll(Task.class);
        task.stream()
                .filter(s -> s.isPast())
                .forEach(s -> {
                    s.delete();
                    counter.getAndIncrement();
                });

        return counter.get();
    }
}
