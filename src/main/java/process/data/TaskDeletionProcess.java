package process.data;

import exceptions.MemberAccessException;
import exceptions.ServerAccessException;
import models.Model;
import models.Server;
import models.Task;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import javax.security.sasl.AuthenticationException;
import java.security.AccessControlException;

import static java.util.Objects.nonNull;

public class TaskDeletionProcess extends TaskAccessor {

  public boolean delete(int taskId, Server server, Member member) throws ServerAccessException, MemberAccessException {
    Task task = Model.read(taskId, Task.class);
    if (nonNull(task)) {
      if (isServerAuthorized(task, server)) {
        if (isMemberAuthorized(member)) {
          task.delete();
          return true;
        } else {
          throw new MemberAccessException();
        }
      } else {
        throw new ServerAccessException();
      }
    }
    return false;
  }
}
