package models;

import org.junit.jupiter.api.*;

import javax.persistence.PersistenceException;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test de {@link Schedule}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestSchedule implements TestModel {

  private static int ID;
  private static Schedule SCHEDULE;
  private static final String PROMOTION_TEST = "Promotion test";
  private static final String PROMOTION_UPDATE = "Promotion mise à jour";

  @AfterAll
  public static void tearDown() {
    SCHEDULE = Model.read(ID, Schedule.class);
    if (nonNull(SCHEDULE)) {
      SCHEDULE.delete();
    }
  }

  @Test
  @Order(1)
  @Override
  public void testCreate() {
    SCHEDULE = new Schedule(PROMOTION_TEST);
    SCHEDULE.create();
    List<Schedule> schedules = Model.readAll(Schedule.class);
    Schedule schedule = schedules.stream().filter(s -> s.getPromotion().equals(PROMOTION_TEST)).findFirst().orElse(null);
    if (nonNull(schedule)) ID = schedule.getId();
    assertNotNull(schedule);
  }

  @Test
  @Order(2)
  public void testCreate_promotion_null() {
    Schedule schedule = new Schedule();
    assertThrows(PersistenceException.class, schedule::create);
  }

  @Test
  @Order(3)
  @Override
  public void testRead() {
    Schedule schedule = Model.read(ID, Schedule.class);
    assertNotNull(schedule);
    assertAll(
      () -> assertNotNull(schedule),
      () -> assertEquals(schedule.getId(), SCHEDULE.getId()),
      () -> assertEquals(schedule.getPromotion(), SCHEDULE.getPromotion())
    );
  }

  @Test
  @Order(4)
  public void testUpdate_promotion_null() {
    SCHEDULE.setPromotion(null);
    assertThrows(PersistenceException.class, SCHEDULE::update);
  }

  @Test
  @Order(5)
  @Override
  public void testUpdate() {
    SCHEDULE.setPromotion(PROMOTION_UPDATE);
    SCHEDULE.update();
    SCHEDULE = Model.read(ID, Schedule.class);
    assertNotNull(SCHEDULE);
    assertEquals(SCHEDULE.getPromotion(), PROMOTION_UPDATE);
  }

  @Test
  @Order(6)
  @Override
  public void testDelete() {
    SCHEDULE.delete();
    Schedule schedule = Model.read(ID, Schedule.class);
    assertNull(schedule);
  }
}
