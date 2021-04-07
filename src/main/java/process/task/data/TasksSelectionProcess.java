package process.task.data;

import models.dao.Server;
import models.dao.Task;

import java.util.List;

/**
 * Process de sélection des {@link Task} associées à un {@link Server}.
 */
public class TasksSelectionProcess {

  /**
   * Sélectionne les tâches associées à un serveur.
   *
   * @param server serveur associé
   * @param days   période à publier, en jours à partir de la date d'aujourd'hui
   * @return liste des tâches associées au serveur
   */
  public List<Task> select(Server server, int days) {
    return Task.getByServer(server, days);
  }

  /**
   * Sélectionne les tâches associées à un serveur.
   *
   * @param server serveur associé
   * @return liste des tâches associées au serveur
   */
  public List<Task> select(Server server) {
    return select(server, -1);
  }
}
