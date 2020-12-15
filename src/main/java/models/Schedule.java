package models;

import com.sun.istack.NotNull;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Emploi du temps d'une promotion.
 */
@Entity
@Table(name = "schedules")
public class Schedule {

  public static String TABLE = "schedules";

  @Id
  @Generated(GenerationTime.INSERT)
  @GenericGenerator(name = "generator", strategy = "increment")
  @GeneratedValue(generator = "generator")
  private int id;
  @NotNull
  private String promotion;

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
}
