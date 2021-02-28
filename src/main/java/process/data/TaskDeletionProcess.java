package process.data;

import exceptions.InvalidIdException;
import exceptions.MemberAccessException;
import exceptions.ServerAccessException;
import models.Model;
import models.Server;
import models.Task;
import net.dv8tion.jda.api.entities.Member;

import static java.util.Objects.isNull;

/**
 * Process de suppression d'une tâche par un utilisateur autorisé.
 */
public class TaskDeletionProcess extends TaskAccessor {

  /**
   * Supprime une tâche si l'utilisateur y est autorisé.
   *
   * @param taskId identifiant de la tâche à supprimer
   * @param server serveur à partir duquel est émise la demande de suppression
   * @param member utilisateur ayant initié l'opération
   * @return true si l'opération a pu être réalisée
   * @throws ServerAccessException la tâche n'est pas associée au serveur
   * @throws MemberAccessException l'utilisateur n'est pas autorisé à modifier les tâches
   */
  public boolean delete(int taskId, Server server, Member member) throws ServerAccessException, MemberAccessException, InvalidIdException {
    Task task = Model.read(taskId, Task.class);
    if (isNull(task)) throw new InvalidIdException();
    if (!isServerAuthorized(task, server)) throw new ServerAccessException();
    if (!isMemberAuthorized(member)) throw new MemberAccessException();
    task.delete();
    return true;
  }
}
