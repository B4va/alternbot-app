package models;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Serveur Discord ayant intégré Altern'Bot.
 */
@Entity
@Table(name = "servers")
public class Server extends Model {

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

  public Server() {
  }

  public Server(String reference, Schedule schedule) {
    this.reference = reference;
    this.schedule = schedule;
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
}
