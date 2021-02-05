package process.data;

import models.Server;
import net.dv8tion.jda.api.entities.Member;

public abstract class TaskAccessor {

  private static final String TASK_ADMIN_ROLE = "TasksAdmin";

  protected boolean isServerAuthorized(/*Task task,*/ Server server) {
    // return task.getServer().getId() == server.getId();
    return false;
  }

  protected boolean isMemberAuthorized(Member member) {
    return member.getRoles().stream()
      .anyMatch(role -> role.getName().equals(TASK_ADMIN_ROLE));
  }
}
