package models;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "sessions")
public class Session extends Model {

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

  @ManyToOne
  @JoinColumn(name = "schedule_id", nullable = false)
  private Schedule schedule;

  public Session() {

  }

  public Session(String name, String teacher, String location, Date date, Date start, Date end, Schedule schedule) {
    this.name = name;
    this.teacher = teacher;
    this.location = location;
    this.date = date;
    this.start = start;
    this.end = end;
    this.schedule = schedule;
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

  public boolean equals(Session session) {
    return name.equals(session.getName()) &&
      start.equals(session.getStart()) &&
      end.equals(session.getEnd()) &&
      date.equals(session.getDate());
  }

  public boolean isPast() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_MONTH, -1);
    return date.before(calendar.getTime());
  }
}
