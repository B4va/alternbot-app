package process.publication;

import models.Task;

import java.util.List;

import static utils.DateUtils.dateToString;
import static utils.DateUtils.timeToString;

/**
 * Process permettant de formater une liste de tâche pour sa publication.
 */
public class TasksFormattingProcess {

  /**
   * Formate une liste de tâches.
   *
   * @param tasks liste de tâches à formater
   * @return message créé à partir de la liste de tâches
   */
  public String format(List<Task> tasks) {
    if (tasks.isEmpty()) return "```\nAucune tâche en cours\n```";
    StringBuilder sb = new StringBuilder("```\nLISTE DES TÂCHES EN COURS :");
    tasks.forEach(t -> formatTask(t, sb));
    sb.append("\n```");
    return sb.toString();
  }

  private void formatTask(Task task, StringBuilder sb) {
    sb.append("\n   - ")
      .append("[").append(task.getId()).append("] ")
      .append(task.getDescription()).append(" : ")
      .append(dateToString(task.getDueDate())).append(" - ")
      .append(timeToString(task.getDueTime()));
  }
}
