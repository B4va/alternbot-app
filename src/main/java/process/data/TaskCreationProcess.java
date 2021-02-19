package process.data;

import com.sun.media.sound.InvalidDataException;
import exceptions.MemberAccessException;
import exceptions.ServerAccessException;
import models.Server;
import models.Task;
import net.dv8tion.jda.api.entities.Member;

import java.util.Date;

import static java.util.Objects.nonNull;

/**
 * Process de création d'une tâche par un utilisateur autorisé.
 */
public class TaskCreationProcess extends TaskAccessor{

    /**
     * Création d'une tâche si l'utilisateur y est autorisé.
     *
     * @param description de la tâche
     * @param dueDate date de rendu de la tâche(jour)
     * @param dueTime date de rendu de la tâche(heure)
     * @param member utilisateur ayant initié l'opération
     * @param server serveur à partir duquel est émise la demande de création
     * @return true si l'opération a pu être réalisée
     * @throws MemberAccessException l'utilisateur n'est pas autorisé à créer les tâches
     */
    public boolean create(String description,Date dueDate,Date dueTime,Member member, Server server) throws ServerAccessException, MemberAccessException, InvalidDataException {
        Task task = validateTask(description,dueDate,dueTime);
        task.setServer(server);
        if (nonNull(task)) {
            if (isMemberAuthorized(member)) {
                task.create();
                return true;
            } else {
                throw new MemberAccessException();
            }
        }
        return false;
    }

    /**
     * Vérification de la validité des attributs de la tâche
     * @param description de la tâche
     * @param dueDate date de rendu de la tâche(jour)
     * @param dueTime date de rendu de la tâche(heure)
     * @return Task l'instance de la tâche si elle est valide
     * @throws InvalidDataException
     */
    private Task validateTask(String description,Date dueDate,Date dueTime) throws InvalidDataException{
        if(description == null || dueDate == null || dueTime == null){
            throw new InvalidDataException();
        }else{
            return new Task(description,dueDate,dueTime,null);
        }
    }
}
