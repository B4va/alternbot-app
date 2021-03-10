package process.schedule.publication;

import models.Schedule;
import models.Server;
import models.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * Classe de test de {@link ScheduleExportFormattingProcess}.
 */
public class TestScheduleExportFormattingProcess {
  private static final ScheduleExportFormattingProcess PROCESS = new ScheduleExportFormattingProcess();
  private static final ScheduleExportFormattingProcess PROCESS_NO_TZ = new ScheduleExportFormattingProcess("");

  private static final String SCHEDULE_PROMO = "LP Génie logiciel";
  private static final String SCHEDULE_URL = "url.test";
  private static final String SERVER_REF = "13371337";
  private static Server SERVER;
  private static List<Session> SESSIONS;
  private static final String ICAL_OK_FILENAME = "TestScheduleExportFormattingProcess_ok.ics";
  private static final String ICAL_NO_TZ_FILENAME = "TestScheduleExportFormattingProcess_noTimezone.ics";
  private static String ICAL_CONTENT_OK;
  private static String ICAL_CONTENT_NO_TZ;

  @BeforeAll
  public static void init() throws ParseException, URISyntaxException, IOException {
    Schedule schedule = new Schedule(SCHEDULE_PROMO, SCHEDULE_URL);
    SERVER = new Server(SERVER_REF, schedule);
    SESSIONS = Arrays.asList(
      new Session("Arts martiaux", "Pai Mei", "F13", stringToDate("18-01-2021"), stringToTime("08:00"), stringToTime("13:00"), schedule),
      new Session("Chimie organique", "Marie Curie", "R86", stringToDate("20-01-2021"), stringToTime("11:00"), stringToTime("18:30"), schedule),
      new Session("Escrime", "Tesshin", "F13", stringToDate("20-01-2021"), stringToTime("14:30"), stringToTime("17:00"), schedule),
      new Session("Physique nucléaire", "Emmett Brown", "F13", stringToDate("22-10-2080"), stringToTime("01:15"), stringToTime("13:00"), schedule)
    );

    ICAL_CONTENT_OK = new String(
      Files.readAllBytes(Paths.get(TestScheduleExportFormattingProcess.class.getResource(ICAL_OK_FILENAME).toURI())),
      StandardCharsets.UTF_8
    );
    ICAL_CONTENT_NO_TZ = new String(
      Files.readAllBytes(Paths.get(TestScheduleExportFormattingProcess.class.getResource(ICAL_NO_TZ_FILENAME).toURI())),
      StandardCharsets.UTF_8
    );
  }

  @Test
  public void testFormat_null_server() {
    assertNull(PROCESS.format(null, SESSIONS));
  }

  @Test
  public void testFormat_null_sessions() {
    assertNull(PROCESS.format(SERVER, null));
  }

  @Test
  public void testFormat_empty_sessions_list() {
    assertNull(PROCESS.format(SERVER, new ArrayList<>()));
  }

  @Test
  public void testFormat_ok() {
    String result = PROCESS.format(SERVER, SESSIONS);
    assertNotNull(result);
    assertTrue(compareIcalFiles(ICAL_CONTENT_OK, result));
  }

  @Test
  public void testFormat_ok_no_timezone() {
    String result = PROCESS_NO_TZ.format(SERVER, SESSIONS);
    assertNotNull(result);
    assertTrue(compareIcalFiles(ICAL_CONTENT_NO_TZ, result));
  }

  // Compare le contenu de 2 fichiers iCal sans prendre en compte la propriété DTSTAMP.
  private static boolean compareIcalFiles(String expected, String actual) {
    Scanner expectedScanner = new Scanner(expected);
    Scanner actualScanner = new Scanner(actual);
    boolean stop = false;
    while (!stop && (expectedScanner.hasNextLine() && actualScanner.hasNextLine())) {
      final String expectedLine = expectedScanner.nextLine(), actualLine = actualScanner.nextLine();

      if (expectedLine.startsWith("DTSTAMP"))
        if (!actualLine.startsWith("DTSTAMP"))
          stop = true;
        else
          continue;

      if (!stop && !expectedLine.equals(actualLine))
        stop = true;
    }

    expectedScanner.close();
    actualScanner.close();
    return !stop;
  }
}
