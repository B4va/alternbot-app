package process.task.data;

import exceptions.InvalidIdException;
import exceptions.MemberAccessException;
import exceptions.ServerAccessException;
import models.Model;
import models.Schedule;
import models.Server;
import models.Task;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.junit.jupiter.api.*;
import process.task.data.TaskAccessor;
import process.task.data.TaskDeletionProcess;

import java.text.ParseException;
import java.util.Collections;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Classe de test de {@link TaskDeletionProcess}.
 */
public class TestTaskDeletionProcess {

  private static final TaskDeletionProcess PROCESS = new TaskDeletionProcess();
  private static Task TASK;
  private static Server SERVER;
  private static Schedule SCHEDULE;

  @BeforeAll
  public static void init() {
    SCHEDULE = new Schedule("scheduleTest", "urlTest");
    SCHEDULE.setId(SCHEDULE.create());
    SERVER = new Server("refTest", SCHEDULE);
    SERVER.setId(SERVER.create());
  }

  @AfterAll
  public static void tearDown() {
    if (nonNull(Model.read(SERVER.getId(), Server.class))) SERVER.delete();
    if (nonNull(Model.read(SCHEDULE.getId(), Schedule.class))) SCHEDULE.delete();
  }

  @BeforeEach
  public void initEach() throws ParseException {
    TASK = new Task("Test", stringToDate("01-01-2000"), stringToTime("00:00"), SERVER);
    TASK.setId(TASK.create());
  }

  @AfterEach
  public void tearDownEach() {
    if (nonNull(Model.read(TASK.getId(), Task.class))) TASK.delete();
  }

  @Test
  public void testDelete_ok() {
    Server validServer = new Server();
    validServer.setId(SERVER.getId());
    Role validRole = mock(Role.class);
    when(validRole.getName()).thenReturn(TaskAccessor.TASK_ADMIN_ROLE);
    Member validMember = mock(Member.class);
    when(validMember.getRoles()).thenReturn(Collections.singletonList(validRole));
    assertAll(
      () -> assertDoesNotThrow(() -> PROCESS.delete(TASK.getId(), validServer, validMember)),
      () -> assertNull(Model.read(TASK.getId(), Task.class))
    );
  }

  @Test
  public void testDelete_no_task() {
    Server validServer = new Server();
    validServer.setId(SERVER.getId());
    Role validRole = mock(Role.class);
    when(validRole.getName()).thenReturn(TaskAccessor.TASK_ADMIN_ROLE);
    Member validMember = mock(Member.class);
    when(validMember.getRoles()).thenReturn(Collections.singletonList(validRole));
    assertThrows(InvalidIdException.class, () -> PROCESS.delete(TASK.getId() + 1, validServer, validMember));
  }

  @Test
  public void testDelete_server_not_authorized() {
    Server invalidServer = new Server();
    invalidServer.setId(SERVER.getId() + 1);
    Role validRole = mock(Role.class);
    when(validRole.getName()).thenReturn(TaskAccessor.TASK_ADMIN_ROLE);
    Member validMember = mock(Member.class);
    when(validMember.getRoles()).thenReturn(Collections.singletonList(validRole));
    assertAll(
      () -> assertThrows(ServerAccessException.class, () -> PROCESS.delete(TASK.getId(), invalidServer, validMember)),
      () -> assertNotNull(Model.read(TASK.getId(), Task.class))
    );
  }

  @Test
  public void testDelete_member_not_authorized() {
    Server validServer = new Server();
    validServer.setId(SERVER.getId());
    Role invalidRole = mock(Role.class);
    when(invalidRole.getName()).thenReturn("roleTest");
    Member invalidMember = mock(Member.class);
    when(invalidMember.getRoles()).thenReturn(Collections.singletonList(invalidRole));
    assertAll(
      () -> assertThrows(MemberAccessException.class, () -> PROCESS.delete(TASK.getId(), validServer, invalidMember)),
      () -> assertNotNull(Model.read(TASK.getId(), Task.class))
    );
  }
}
