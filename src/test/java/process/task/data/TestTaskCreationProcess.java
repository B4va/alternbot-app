package process.task.data;

import exceptions.InvalidDataException;
import exceptions.MemberAccessException;
import models.Model;
import models.Schedule;
import models.Server;
import models.Task;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import process.task.data.TaskAccessor;
import process.task.data.TaskCreationProcess;

import java.text.ParseException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Classe de test de {@link TaskCreationProcess}.
 */
public class TestTaskCreationProcess {

  private static final TaskCreationProcess PROCESS = new TaskCreationProcess();
  private static Server SERVER;
  private static Schedule SCHEDULE;
  private static final String DESCRIPTION = "test task creation";

  @BeforeAll
  public static void init() throws ParseException {
    SCHEDULE = new Schedule("scheduleTest", "urlTest");
    SCHEDULE.setId(SCHEDULE.create());
    SERVER = new Server("refTest", SCHEDULE);
    SERVER.setId(SERVER.create());
  }

  @AfterAll
  public static void tearDown() {
      Model.readAll(Task.class).stream()
        .filter(t -> t.getDescription().equals(DESCRIPTION))
        .forEach(Model::delete);
      SERVER.delete();
      SCHEDULE.delete();
  }

  @Test
  public void testCreation_ok() throws InvalidDataException, MemberAccessException, ParseException {
    Server validServer = new Server();
    validServer.setId(SERVER.getId());
    Role validRole = mock(Role.class);
    when(validRole.getName()).thenReturn(TaskAccessor.TASK_ADMIN_ROLE);
    Member validMember = mock(Member.class);
    when(validMember.getRoles()).thenReturn(Collections.singletonList(validRole));
    final String dueDate = "01-01-2020";
    final String dueTime = "10:00";
    PROCESS.create(DESCRIPTION, dueDate, dueTime, validMember, validServer);
    Task task = Model.readAll(Task.class)
      .stream().filter(t -> t.getDescription().equals(DESCRIPTION))
      .findFirst()
      .orElse(null);
    assertNotNull(task);
    assertAll(
      () -> assertEquals(task.getDescription(), DESCRIPTION),
      () -> assertEquals(task.getDueDate(), stringToDate(dueDate)),
      () -> assertEquals(task.getDueTime(), stringToTime(dueTime))
    );
  }

  @Test
  public void testCreation_invalid_member() {
    Server validServer = new Server();
    validServer.setId(SERVER.getId());
    Role validRole = mock(Role.class);
    when(validRole.getName()).thenReturn("invalide");
    Member invalidMember = mock(Member.class);
    when(invalidMember.getRoles()).thenReturn(Collections.singletonList(validRole));
    final String dueDate = "01-01-2020";
    final String dueTime = "10:00";
    assertThrows(MemberAccessException.class, () -> PROCESS.create(DESCRIPTION, dueDate, dueTime, invalidMember, validServer));
  }

  @Test
  public void testCreation_invalid_data() {
    Server validServer = new Server();
    validServer.setId(SERVER.getId());
    Role validRole = mock(Role.class);
    when(validRole.getName()).thenReturn(TaskAccessor.TASK_ADMIN_ROLE);
    Member validMember = mock(Member.class);
    when(validMember.getRoles()).thenReturn(Collections.singletonList(validRole));
    assertThrows(InvalidDataException.class, () -> PROCESS.create(DESCRIPTION, null, null, validMember, validServer));
  }
}
