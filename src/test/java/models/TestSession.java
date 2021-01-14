package models;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import javax.persistence.PersistenceException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestSession implements TestModel {

  private static int ID_SESSION;
  private static int ID_SCHEDULE;
  private static Session SESSION;
  private static Schedule SCHEDULE;
  private static final String PROMOTION_TEST = "promotion test";

  @Test
  @Order(1)
  @Override
  public void testCreate() {
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
    SCHEDULE = new Schedule(PROMOTION_TEST);
    ID_SCHEDULE = SCHEDULE.create();
    try {
      SESSION = new Session("info", "M. Prof", "A01", formatter.parse("01-01-2021 14:00"),
        formatter.parse("01-01-2021 15:00"), Schedule.read(ID_SCHEDULE, Schedule.class));
    } catch (ParseException e) {
      fail();
    }
    ID_SESSION = SESSION.create();
    List<Session> sessions = Model.readAll(Session.class);
    assertTrue(sessions.stream().anyMatch(s -> s.getId() == ID_SESSION));
  }

  @Test
  @Order(2)
  public void testCreate_name_null() {
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
    Session session = null;
    try {
      session = new Session("info", "M. Prof", "A01", formatter.parse("01-01-2021 14:00"),
        formatter.parse("01-01-2021 15:00"), Schedule.read(ID_SCHEDULE, Schedule.class));
    } catch (ParseException e) {
      fail();
    }
    session.setName(null);
    assertThrows(PersistenceException.class, session::create);
  }

  @Override
  public void testRead() {

  }

  @Override
  public void testUpdate() {

  }

  @Override
  public void testDelete() {

  }
}
