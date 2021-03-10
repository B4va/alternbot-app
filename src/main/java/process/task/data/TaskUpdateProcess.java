package process.task.data;

import exceptions.InvalidDataException;
import exceptions.InvalidIdException;
import exceptions.MemberAccessException;
import exceptions.ServerAccessException;
import models.Model;
import models.Server;
import models.Task;
import net.dv8tion.jda.api.entities.Member;

import java.text.ParseException;

import static java.util.Objects.isNull;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Process de mise à jour d'une tâche par un utilisateur autorisé.
 */
public class TaskUpdateProcess extends TaskAccessor {

  /**
   * Met à jour une tâche si l'utilisateur y est autorisé et que le format des données
   * est valide.
   *
   * @param taskId      identifiant de la tâche à modifier
   * @param description nouvelle description de la tâche
   * @param dueDate     nouvelle date de la tâche
   * @param dueTime     nouvel horaire de la tâche
   * @param member      utilisateur à l'origine de l'opération
   * @param server      serveur à partir duquel l'opération a été initiée
   * @return true si l'opération a pu être réalisée
   * @throws MemberAccessException l'utilisateur n'est pas autorisé à modifier les tâches
   * @throws ServerAccessException la tâche n'est pas associé au serveur
   * @throws InvalidDataException  les informations saisie ne sont pas au bon format
   * @throws ParseException        les dates saisies ne sont pas au bon format
   */
  public boolean update(int taskId, String description, String dueDate, String dueTime, Member member, Server server) throws MemberAccessException, ServerAccessException, InvalidDataException, ParseException, InvalidIdException {
    Task task = Model.read(taskId, Task.class);
    if (isNull(task)) throw new InvalidIdException();
    if (!isServerAuthorized(task, server)) throw new ServerAccessException();
    if (!isMemberAuthorized(member)) throw new MemberAccessException();
    if (!isValid(description, dueDate, dueTime)) throw new InvalidDataException();
    task.setDescription(description);
    task.setDueDate(stringToDate(dueDate));
    task.setDueTime(stringToTime(dueTime));
    task.update();
    return true;
  }
}
