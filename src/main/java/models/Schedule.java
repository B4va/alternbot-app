package models;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

/**
 * Emploi du temps d'une promotion.
 */
@Entity
@Table(name = "schedules")
public class Schedule {

  @Id
  @Generated(GenerationTime.INSERT)
  @GenericGenerator(name = "generator", strategy = "increment")
  @GeneratedValue(generator = "generator")
  @Column(name = "id")
  private int id;

  @Column(name = "promotion", nullable = false)
  private String promotion;

  @OneToMany(mappedBy = "schedule")
  private Set<Server> servers;

  @OneToMany(mappedBy = "schedule")
  private Set<Session> sessions;

  public Schedule() {
  }

  public Schedule(String promotion) {
    this.promotion = promotion;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPromotion() {
    return promotion;
  }

  public void setPromotion(String promotion) {
    this.promotion = promotion;
  }

  public Set<Server> getServers() {
    return servers;
  }

  public void setServers(Set<Server> servers) {
    this.servers = servers;
  }

  public Set<Session> getSessions() {
    return sessions;
  }

  public void setSessions(Set<Session> sessions) {
    this.sessions = sessions;
  }

}
