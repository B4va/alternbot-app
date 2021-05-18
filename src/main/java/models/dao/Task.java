package models.dao;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Tâche (contrôle, devoir à rendre, ...).
 */
@Entity
@Table(name = "tasks")
public class Task extends ModelDAO {

  @Id
  @Generated(GenerationTime.INSERT)
  @GenericGenerator(name = "generator", strategy = "increment")
  @GeneratedValue(generator = "generator")
  @Column(name = "id")
  private int id;

  @ManyToOne
  @JoinColumn(name = "server_id", nullable = false)
  private Server server;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "due_date")
  @Temporal(TemporalType.DATE)
  private Date dueDate;

  @Column(name = "due_time")
  @Temporal(TemporalType.TIME)
  private Date dueTime;

  public Task() {
  }

  public Task(String description, Date dueDate, Date dueTime, Server server) {
    setDescription(description);
    setDueDate(dueDate);
    setDueTime(dueTime);
    setServer(server);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Server getServer() {
    return this.server;
  }

  public void setServer(Server server) {
    this.server = server;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getDueDate() {
    return this.dueDate;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  public Date getDueTime() {
    return this.dueTime;
  }

  public void setDueTime(Date dueTime) {
    this.dueTime = dueTime;
  }

  /**
   * Indique si la date de la tache est passée d'au moins le nombre de jours donné.
   * @param nbDays Nombre de jours.
   * @return true si la date de la tache est passée d'au moins le nombre de jours donné (sans prise en compte de l'horaire)
   */
  public boolean isPast(int nbDays) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -nbDays);
    return dueDate.equals(calendar.getTime()) || dueDate.before(calendar.getTime());
  }

  /**
   * Indique si la tache est considéré comme passé.
   * @return true si la date de la tache précède le jour actuel (sans prise en compte de l'horaire)
   */
  public boolean isPast() {
    return this.isPast(1);
  }
}
