package models.dao;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "sessions")
public class Session extends ModelDAO {

  @Id
  @Generated(GenerationTime.INSERT)
  @GenericGenerator(name = "generator", strategy = "increment")
  @GeneratedValue(generator = "generator")
  @Column(name = "id")
  private int id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "teacher")
  private String teacher;

  @Column(name = "location")
  private String location;

  @Column(name = "date_session", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date date;

  @Column(name = "start_time", nullable = false)
  @Temporal(TemporalType.TIME)
  private Date start;

  @Column(name = "end_time", nullable = false)
  @Temporal(TemporalType.TIME)
  private Date end;

  @Column(name = "updated")
  private boolean updated;

  @Column(name = "type")
  private String type;

  @ManyToOne
  @JoinColumn(name = "schedule_id", nullable = false)
  private Schedule schedule;

  public Session() {

  }

  public Session(String name, String teacher, String location, Date date, Date start, Date end, Schedule schedule, String type) {
    this.name = name;
    this.teacher = teacher;
    this.location = location;
    this.date = date;
    this.start = start;
    this.end = end;
    this.schedule = schedule;
    this.type = type;
  }

  @Override
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTeacher() {
    return teacher;
  }

  public void setTeacher(String teacher) {
    this.teacher = teacher;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public boolean isUpdated() {
    return updated;
  }

  public void setUpdated(boolean updated) {
    this.updated = updated;
  }

  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean equals(Session session) {
    return name.equals(session.getName()) &&
      start.equals(session.getStart()) &&
      end.equals(session.getEnd()) &&
      date.equals(session.getDate());
  }

  /**
   * Indique si la date du cours est passée d'au moins le nombre de jours donné.
   *
   * @param nbDays Nombre de jours.
   * @return true si la date du cours est passée d'au moins le nombre de jours donné (sans prise en compte de l'horaire)
   */
  public boolean isPast(int nbDays) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -nbDays);
    return date.equals(calendar.getTime()) || date.before(calendar.getTime());
  }

  /**
   * Indique si le cours est considéré comme passé.
   *
   * @return true si la date du cours précède le jour actuel (sans prise en compte de l'horaire)
   */
  public boolean isPast() {
    return this.isPast(-1);
  }
}
