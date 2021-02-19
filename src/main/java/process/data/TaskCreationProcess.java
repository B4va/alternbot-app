package process.data;

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
     * @param task tâche à créer
     * @param member utilisateur ayant initié l'opération
     * @param server serveur à partir duquel est émise la demande de création
     * @return true si l'opération a pu être réalisée
     * @throws MemberAccessException l'utilisateur n'est pas autorisé à créer les tâches
     */
    public boolean create(Task task,Member member, Server server) throws ServerAccessException, MemberAccessException {
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
}
