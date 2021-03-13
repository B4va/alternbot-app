package process.task.data;

import exceptions.InvalidDataException;
import exceptions.InvalidIdException;
import exceptions.MemberAccessException;
import exceptions.ServerAccessException;
import models.dao.ModelDAO;
import models.dao.Schedule;
import models.dao.Server;
import models.dao.Task;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Collections;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Classe de test de {@link TaskUpdateProcess}.
 */
public class TestTaskUpdateProcess {

  private static final TaskUpdateProcess PROCESS = new TaskUpdateProcess();
  private static final String VALID_DESCRIPTION = "description modifiée";
  private static final String VALID_DUE_DATE = "01-01-2000";
  private static final String VALID_DUE_TIME = "12:00";
  private static Task TASK;
  private static Server SERVER;
  private static Schedule SCHEDULE;
  private static Member VALID_MEMBER;

  @BeforeAll
  public static void init() throws ParseException {
    SCHEDULE = new Schedule("scheduleTest", "urlTest");
    SCHEDULE.setId(SCHEDULE.create());
    SERVER = new Server("refTest", SCHEDULE);
    SERVER.setId(SERVER.create());
    TASK = new Task("test", stringToDate("01-01-2020"), stringToTime("10:00"), SERVER);
    TASK.setId(TASK.create());
    Server validServer = new Server();
    validServer.setId(SERVER.getId());
    Role validRole = mock(Role.class);
    when(validRole.getName()).thenReturn(TaskAccessor.TASK_ADMIN_ROLE);
    VALID_MEMBER = mock(Member.class);
    when(VALID_MEMBER.getRoles()).thenReturn(Collections.singletonList(validRole));
  }

  @Test
  public void testUpdate_ok() throws ServerAccessException, InvalidDataException, ParseException, MemberAccessException, InvalidIdException {
    PROCESS.update(TASK.getId(), VALID_DESCRIPTION, VALID_DUE_DATE, VALID_DUE_TIME, VALID_MEMBER, SERVER);
    Task task = ModelDAO.read(TASK.getId(), Task.class);
    assertAll(
      () -> assertEquals(task.getDescription(), VALID_DESCRIPTION),
      () -> assertEquals(task.getDueDate(), stringToDate(VALID_DUE_DATE)),
      () -> assertEquals(task.getDueTime(), stringToTime(VALID_DUE_TIME))
    );
  }

  @Test
  public void testUpdate_invalid_server() {
    Server invalidServer = new Server();
    invalidServer.setId(SERVER.getId() + 1);
    assertThrows(ServerAccessException.class,
      () -> PROCESS.update(TASK.getId(), VALID_DESCRIPTION, VALID_DUE_DATE, VALID_DUE_TIME, VALID_MEMBER, invalidServer));
  }

  @Test
  public void testUpdate_invalid_member() {
    Role invalidRole = mock(Role.class);
    when(invalidRole.getName()).thenReturn("test");
    Member invalidMember = mock(Member.class);
    when(invalidMember.getRoles()).thenReturn(Collections.singletonList(invalidRole));
    assertThrows(MemberAccessException.class,
      () -> PROCESS.update(TASK.getId(), VALID_DESCRIPTION, VALID_DUE_DATE, VALID_DUE_TIME, invalidMember, SERVER));
  }

  @Test
  public void testUpdate_invalid_description() {
    String description = "";
    assertThrows(InvalidDataException.class,
      () -> PROCESS.update(TASK.getId(), description, VALID_DUE_DATE, VALID_DUE_TIME, VALID_MEMBER, SERVER));
  }

  @Test
  public void testUpdate_invalid_dueDate() {
    String dueDate = "abcde";
    assertThrows(InvalidDataException.class,
      () -> PROCESS.update(TASK.getId(), VALID_DESCRIPTION, dueDate, VALID_DUE_TIME, VALID_MEMBER, SERVER));
  }

  @Test
  public void testUpdate_invalid_dueTime() {
    String dueTime = "abcde";
    assertThrows(InvalidDataException.class,
      () -> PROCESS.update(TASK.getId(), VALID_DESCRIPTION, VALID_DUE_DATE, dueTime, VALID_MEMBER, SERVER));
  }

  @Test
  public void testUpdate_invalid_id() {
    int id = 999;
    assertThrows(InvalidIdException.class,
      () -> PROCESS.update(id, VALID_DESCRIPTION, VALID_DUE_DATE, VALID_DUE_TIME, VALID_MEMBER, SERVER));
  }

  @AfterAll
  public static void tearDown() {
    if (nonNull(ModelDAO.read(TASK.getId(), Task.class))) TASK.delete();
    if (nonNull(ModelDAO.read(SERVER.getId(), Server.class))) SERVER.delete();
    if (nonNull(ModelDAO.read(SCHEDULE.getId(), Schedule.class))) SCHEDULE.delete();
  }
}
