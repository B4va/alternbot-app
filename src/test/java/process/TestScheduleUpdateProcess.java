package process;

import models.Schedule;
import models.Server;
import models.Session;
import org.junit.jupiter.api.*;

import javax.security.auth.login.LoginException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;

import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Classe de test de {@link ScheduleUpdatePublicationProcess}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestScheduleUpdateProcess {

  private Server s;
  private Schedule schedule;
  private HashSet<Server> servers;
  private Session old,nouv;
  private Date debut,fin,d;
  private ScheduleUpdatePublicationProcess scheduleUpdateProcess;

  @BeforeAll
  public void init() throws ParseException, LoginException, InterruptedException {
    s = new Server();
    s.setReference("Nzg1NDk4ODI0Njg0NzMyNDE2.X84uyQ.-dmoFryuzXjTOSryYL9LoR3t2J4");
    schedule = new Schedule();
    servers = new HashSet<Server>();
    servers.add(s);
    schedule.setServers(servers);
    old = new Session();
    debut = stringToTime("08:00");
    fin = stringToTime("10:00");
    d = stringToDate("16-01-2021");
    old.setDate(d);
    old.setStart(debut);
    old.setEnd(fin);
    old.setTeacher("M.JOYEUX");
    old.setName("Tests");
    old.setSchedule(schedule);
    nouv = new Session();
    d = stringToDate("18-01-2021");
    nouv.setDate(d);
    nouv.setStart(debut);
    nouv.setEnd(fin);
    scheduleUpdateProcess = new ScheduleUpdatePublicationProcess();
  }

  @Order(1)
  @Test
  public void test_Formatter_Envoi_NewNonNull() throws LoginException, InterruptedException {

  }

}
