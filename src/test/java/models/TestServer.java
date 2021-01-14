package models;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import javax.persistence.PersistenceException;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test de {@link Server}.
 */
public class TestServer implements TestModel {

  private static int ID;
  private static Server SERVER;
  private static Schedule SCHEDULE;
  private static final String PROMOTION_TEST = "test";
  private static final String REFERENCE_TEST = "ref";
  private static final String UPDATED_REFERENCE = "updated_ref";

  @BeforeAll
  public static void init() {
    SERVER = new Server();
    SCHEDULE = new Schedule();
    SCHEDULE.setId(1);
    SCHEDULE.setPromotion(PROMOTION_TEST);
    SERVER.setSchedule(SCHEDULE);
  }

  @AfterAll
  public static void tearDown() {
    SERVER = Model.read(ID, Server.class);
    if (nonNull(SERVER)) {
      SERVER.delete();
    }
    SCHEDULE = Model.read(ID, Schedule.class);
    if (nonNull(SCHEDULE)) {
      SERVER.delete();
    }
  }

  @Test
  @Order(1)
  @Override
  public void testCreate() {
    SERVER.setReference(REFERENCE_TEST);
    SERVER.setId(1);
    SERVER.setSchedule(SCHEDULE);
    SCHEDULE.create();
    SERVER.create();
    List<Server> servers = Model.readAll(Server.class);
    Server serv = servers.stream()
      .filter(s -> s.getReference().equals(REFERENCE_TEST))
      .findFirst()
      .orElse(null);
    if (nonNull(serv)) ID = serv.getId();
    assertNotNull(serv);
  }

  @Test
  @Order(2)
  public void testCreate_server_null() {
    Server s = new Server();
    assertThrows(PersistenceException.class, s::create);
  }

  @Test
  @Order(3)
  @Override
  public void testRead() {
    Server s = Model.read(ID, Server.class);
    assertNotNull(s);
    assertAll(
      () -> assertNotNull(s),
      () -> assertEquals(s.getId(), SERVER.getId()),
      () -> assertEquals(s.getReference(), SERVER.getReference()),
      () -> assertEquals(s.getSchedule(), SERVER.getSchedule())
    );
  }

  @Test
  @Order(4)
  public void testUpdate_Schedule_null() {
    SERVER.setSchedule(null);
    assertThrows(PersistenceException.class, SERVER::update);
  }

  @Test
  @Order(5)
  @Override
  public void testUpdate() {
    SERVER.setReference(UPDATED_REFERENCE);
    SERVER.setSchedule(SCHEDULE);
    SERVER.update();
    SERVER = Model.read(ID, Server.class);
    assertNotNull(SERVER);
    assertEquals(SERVER.getReference(), UPDATED_REFERENCE);
  }

  @Test
  @Order(6)
  @Override
  public void testDelete() {
    SERVER.delete();
    Server s = Model.read(ID, Server.class);
    assertNull(s);
  }

}
