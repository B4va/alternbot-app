package process.task.data;

import models.dao.Server;
import models.dao.Task;
import net.dv8tion.jda.api.entities.Member;
import utils.DateUtils;

import java.text.ParseException;

/**
 * Gère l'accès aux différentes tâches via commande utilisateur.
 */
public abstract class TaskAccessor {

  public static final String TASK_ADMIN_ROLE = "TasksAdmin";

  /**
   * Valide l'accès d'un serveur à une tâche.
   *
   * @param task   tâche à laquelle un membre du serveur cherche à accéder
   * @param server serveur à partir duquel un membre veut accéder à la tâche
   * @return true si le serveur est autorisé à accéder à la tâche
   */
  protected boolean isServerAuthorized(Task task, Server server) {
    return task.getServer().getId() == server.getId();
  }

  /**
   * Valide l'accès d'un utilisateur à une tâche.
   *
   * @param member utilisateur cherchant à accéder à la tâche
   * @return true si l'utilisateur est autorisé à accéder à la tâche
   */
  protected boolean isMemberAuthorized(Member member) {
    return member.getRoles().stream()
      .anyMatch(role -> role.getName().equals(TASK_ADMIN_ROLE));
  }

  /**
   *
   * @param description
   * @param dueDate
   * @param dueTime
   * @return true si valide, sinon faux
   */
  protected boolean isValid(String description, String dueDate, String dueTime) {
    if (description.equals("")) return false;
    try {
      DateUtils.stringToDate(dueDate);
      DateUtils.stringToTime(dueTime);
    } catch (ParseException e) {
      return false;
    }
    return true;
  }
}
