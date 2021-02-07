package process.data;

import models.Server;
import models.Task;
import net.dv8tion.jda.api.entities.Member;

public abstract class TaskAccessor {

  public static final String TASK_ADMIN_ROLE = "TasksAdmin";

  protected boolean isServerAuthorized(Task task, Server server) {
    return task.getServer().getId() == server.getId();
  }

  protected boolean isMemberAuthorized(Member member) {
    return member.getRoles().stream()
      .anyMatch(role -> role.getName().equals(TASK_ADMIN_ROLE));
  }
}
