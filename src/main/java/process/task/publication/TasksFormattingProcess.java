package process.task.publication;

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
    return format(tasks, -1);
  }

  /**
   * Formate une liste de tâches en indiquant la période concernée.
   *
   * @param tasks  liste de tâches à formater
   * @param nbDays nombre de jours à compter de la date actuelle
   * @return message créé à partir de la liste de tâches
   */
  public String format(List<Task> tasks, int nbDays) {
    if (tasks.isEmpty()) return "```\nAucune tâche en cours" + (nbDays > -1 ? " (J+" + nbDays+ ")" : "") + "\n```";
    StringBuilder sb = new StringBuilder("```\nLISTE DES TÂCHES EN COURS ");
    sb.append(nbDays > -1 ? "(J+" + nbDays + ") :" : ":");
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
