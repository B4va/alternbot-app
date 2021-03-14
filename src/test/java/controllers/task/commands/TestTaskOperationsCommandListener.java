package controllers.task.commands;

import controllers.task.commands.TaskOperationsCommandListener;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static controllers.task.commands.TaskOperationsCommandListener.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Classe de test de {@link TaskOperationsCommandListener}
 */
public class TestTaskOperationsCommandListener {

  @Test
  public void testParseMessage_create_message() {
    String message = "$tache -c [ test de création ] 01-01-2020 10:00";
    Map<String, String> map = parseMessage(Arrays.asList(message.split(" ")));
    assertAll(
      () -> assertEquals(map.get(MAP_DESCRIPTION), "test de création"),
      () -> assertEquals(map.get(MAP_DUE_DATE), "01-01-2020"),
      () -> assertEquals(map.get(MAP_DUE_TIME), "10:00")
    );
  }

  @Test
  public void testParseMessage_update_message() {
    String message = "$tache -u 1 [ test de création ] 01-01-2020 10:00";
    Map<String, String> map = parseMessage(Arrays.asList(message.split(" ")));
    assertAll(
      () -> assertEquals(map.get(MAP_DESCRIPTION), "test de création"),
      () -> assertEquals(map.get(MAP_DUE_DATE), "01-01-2020"),
      () -> assertEquals(map.get(MAP_DUE_TIME), "10:00")
    );
  }
}
