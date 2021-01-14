package models;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sessions")
public class Session {

  @Id
  @Generated(GenerationTime.INSERT)
  @GenericGenerator(name = "generator", strategy = "increment")
  @GeneratedValue(generator = "generator")
  @Column(name = "id")
  private int id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "teacher", nullable = false)
  private String teacher;

  @Column(name = "location", nullable = false)
  private String location;

  @Column(name = "start_date", nullable = false)
  private Date start;

  @Column(name = "end_date", nullable = false)
  private Date end;

  @ManyToOne
  @JoinColumn(name = "schedule_id", nullable = false)
  private Schedule schedule;

  public Session() {

  }

  public Session(String name, String teacher, String location, Date start, Date end, Schedule schedule) {
    this.name = name;
    this.teacher = teacher;
    this.location = location;
    this.start = start;
    this.end = end;
    this.schedule = schedule;
  }

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
    return this.teacher;
  }

  public void setTeacher(String teacher) {
    this.teacher = teacher;
  }


  public String getLocation() {
    return this.location;
  }

  public void setLocation(String location) {
    this.location = location;
  }


  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }


  public Date getEnd() {
    return this.end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }


  public Schedule getSchedule() {
    return schedule;
  }

  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }


}
