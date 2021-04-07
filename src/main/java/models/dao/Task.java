package models.dao;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.criterion.Restrictions;
import utils.DbUtils;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

  public static List<Task> getByServer(Server server, int days) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -1);
    Date after = calendar.getTime();
    calendar.add(Calendar.DAY_OF_MONTH, days);
    Date before = calendar.getTime();
    EntityManager entityManager = DbUtils.getSessionFactory().createEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Task> criteria = builder.createQuery(Task.class);
    Root<Task> root = criteria.from(Task.class);
    criteria.select(root);
    List<Predicate> predicates = new ArrayList<>();
    predicates.add((builder.equal(root.get("server"), server.getId())));
    predicates.add(builder.greaterThan(root.get("dueDate"), after));
    if (days > 0) {
      predicates.add(builder.lessThan(root.get("dueDate"), before));
    }
    criteria.where(predicates.toArray(new Predicate[]{}));
    criteria.orderBy(builder.asc(root.get("dueDate")), builder.asc(root.get("dueTime")));
    return entityManager.createQuery(criteria).getResultList();
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
}
