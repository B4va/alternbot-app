package controllers.task.commands;

import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.util.Arrays;
import java.util.Map;

import static controllers.task.commands.TaskOperationsCommandListener.*;
import static org.junit.jupiter.api.Assertions.*;

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

  @Test
  public void testParseMessage_error_time_format() {
    String message1 = "$tache -u 1 [ test de création ] 01-01-2020 -1:00";
    String message2 = "$tache -u 1 [ test de création ] 01-01-2020 26:00";
    String message3 = "$tache -u 1 [ test de création ] 01-01-2020 10:-5";
    String message4 = "$tache -u 1 [ test de création ] 01-01-2020 10:77";
    assertAll(
      () -> assertThrows(DateTimeException.class, () -> parseMessage(Arrays.asList(message1.split(" ")))),
      () -> assertThrows(DateTimeException.class, () -> parseMessage(Arrays.asList(message2.split(" ")))),
      () -> assertThrows(DateTimeException.class, () -> parseMessage(Arrays.asList(message3.split(" ")))),
      () -> assertThrows(DateTimeException.class, () -> parseMessage(Arrays.asList(message4.split(" "))))
    );
  }
}
