package process.data;

import exceptions.MemberAccessException;
import exceptions.ServerAccessException;
import models.Model;
import models.Schedule;
import models.Server;
import models.Task;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.junit.jupiter.api.*;

import java.text.ParseException;
import java.util.Collections;

import static java.util.Objects.nonNull;
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
    private static Task TASK;
    private static Server SERVER;
    private static Schedule SCHEDULE;

    @BeforeAll
    public static void init() throws ParseException {
        SCHEDULE = new Schedule("scheduleTest", "urlTest");
        SCHEDULE.setId(SCHEDULE.create());
        SERVER = new Server("refTest", SCHEDULE);
        SERVER.setId(SERVER.create());
        TASK = new Task("Test", stringToDate("01-01-2000"), stringToTime("00:00"), SERVER);
    }

    @Test
    public void testCreation_ok() {
        Server validServer = new Server();
        validServer.setId(SERVER.getId());
        Role validRole = mock(Role.class);
        when(validRole.getName()).thenReturn(TaskAccessor.TASK_ADMIN_ROLE);
        Member validMember = mock(Member.class);
        when(validMember.getRoles()).thenReturn(Collections.singletonList(validRole));
        assertAll(
                () -> assertDoesNotThrow(() -> PROCESS.create(TASK.getDescription(), TASK.getDueDate(), TASK.getDueTime(),validMember, validServer)),
                () -> assertNotNull(Model.read(TASK.getId(), Task.class))
        );
    }
}
