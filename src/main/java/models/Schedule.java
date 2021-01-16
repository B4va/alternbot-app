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
public class Schedule extends Model {

  @Id
  @Generated(GenerationTime.INSERT)
  @GenericGenerator(name = "generator", strategy = "increment")
  @GeneratedValue(generator = "generator")
  @Column(name = "id")
  private int id;

  @Column(name = "promotion", nullable = false)
  private String promotion;

  @Column(name = "url", nullable = false)
  private String url;

  @OneToMany(mappedBy = "schedule")
  private Set<Server> servers;

  @OneToMany(mappedBy = "schedule")
  private Set<Session> sessions;

  public Schedule() {
  }

  public Schedule(String promotion, String url) {
    this.promotion = promotion;
    this.url = url;
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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
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
