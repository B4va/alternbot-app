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
  private static final String URL_TEST = "url.com";
  private static final String PROMOTION_UPDATE = "Promotion mise à jour";
  private static final String URL_UPDATE = "url.update.com";

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
    SCHEDULE = new Schedule(PROMOTION_TEST, URL_TEST);
    ID = SCHEDULE.create();
    List<Schedule> schedules = Model.readAll(Schedule.class);
    assertTrue(schedules.stream().anyMatch(s -> s.getId() == ID));
  }

  @Test
  @Order(2)
  public void testCreate_promotion_null() {
    Schedule schedule = new Schedule();
    schedule.setUrl(URL_TEST);
    assertThrows(PersistenceException.class, schedule::create);
  }

  @Test
  @Order(2)
  public void testCreate_url_null() {
    Schedule schedule = new Schedule();
    schedule.setPromotion(PROMOTION_TEST);
    assertThrows(PersistenceException.class, schedule::create);
  }

  @Test
  @Order(3)
  @Override
  public void testRead() {
    Schedule schedule = Model.read(ID, Schedule.class);
    assertAll(
      () -> assertNotNull(schedule),
      () -> assertEquals(schedule.getId(), SCHEDULE.getId()),
      () -> assertEquals(schedule.getPromotion(), SCHEDULE.getPromotion()),
      () -> assertEquals(schedule.getUrl(), SCHEDULE.getUrl())
    );
  }

  @Test
  @Order(4)
  public void testUpdate_promotion_null() {
    Schedule schedule = new Schedule(null, URL_UPDATE);
    schedule.setId(SCHEDULE.getId());
    assertThrows(PersistenceException.class, schedule::update);
  }

  @Test
  @Order(4)
  public void testUpdate_url_null() {
    Schedule schedule = new Schedule(PROMOTION_UPDATE, null);
    schedule.setId(SCHEDULE.getId());
    assertThrows(PersistenceException.class, schedule::update);
  }

  @Test
  @Order(5)
  @Override
  public void testUpdate() {
    SCHEDULE.setPromotion(PROMOTION_UPDATE);
    SCHEDULE.setUrl(URL_UPDATE);
    SCHEDULE.update();
    SCHEDULE = Model.read(ID, Schedule.class);
    assertNotNull(SCHEDULE);
    assertAll(
      () -> assertEquals(SCHEDULE.getPromotion(), PROMOTION_UPDATE),
      () -> assertEquals(SCHEDULE.getUrl(), URL_UPDATE)
    );
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
