package controllers.task.workers;

import controllers.commons.workers.DailyWorker;
import models.Model;
import models.Server;
import process.task.publication.TasksPublicationProcess;

/**
 * Gère la publication régulière de la liste des tâches en cours sur l'ensemble des serveurs.
 */
public class TasksPublicationWorker extends DailyWorker {

  private static final int DAYS = 3;
  private static final String CHANNEL = "tasks";

  public TasksPublicationWorker(int hour, int minute, long delay) {
    super(hour, minute, delay);
  }

  @Override
  protected void doRunOne() {
    Model.readAll(Server.class)
      .forEach(s -> new TasksPublicationProcess().sendPublication(CHANNEL, s.getReference(), DAYS));
  }

  @Override
  protected String getTask() {
    return "Publication quotidienne des tâches en cours";
  }
}
