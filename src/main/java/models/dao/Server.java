package models.dao;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;
import utils.DbUtils;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Set;

/**
 * Serveur Discord ayant intégré Altern'Bot.
 */
@Entity
@Table(name = "servers")
public class Server extends ModelDAO {

  @Id
  @Generated(GenerationTime.INSERT)
  @GenericGenerator(name = "generator", strategy = "increment")
  @GeneratedValue(generator = "generator")
  @Column(name = "id")
  private int id;

  @Column(name = "reference", nullable = false)
  private String reference;

  @ManyToOne
  @JoinColumn(name = "schedule_id", nullable = false)
  private Schedule schedule;

  @OneToMany(mappedBy = "server")
  private Set<Task> tasks;

  public Server() {
  }

  /**
   * @param reference identifiant du serveur Discord
   * @param schedule  emploi du temps rattaché au serveur
   */
  public Server(String reference, Schedule schedule) {
    this.reference = reference;
    this.schedule = schedule;
  }

  /**
   * Récupère un serveur à partir de sa référence.
   *
   * @param reference référence du serveur
   * @return serveur associé à la référence
   */
  public static Server getByReference(String reference) {
    EntityManager entityManager = DbUtils.getSessionFactory().createEntityManager();
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Server> criteria = builder.createQuery(Server.class);
    Root<Server> root = criteria.from(Server.class);
    criteria.select(root);
    criteria.where(builder.equal(root.get("reference"), reference));
    return entityManager.createQuery(criteria).getResultList().get(0);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }

  public Set<Task> getTasks() {
    return this.tasks;
  }

  public void setTasks(Set<Task> tasks) {
    this.tasks = tasks;
  }
}
