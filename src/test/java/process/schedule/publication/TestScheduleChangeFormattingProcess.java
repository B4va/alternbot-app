package process.schedule.publication;

import models.Session;
import models.business.SessionChange;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import process.schedule.publication.ScheduleChangeFormattingProcess;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * CLasse de test de {@link ScheduleChangeFormattingProcess}.
 */
public class TestScheduleChangeFormattingProcess {

  private static final ScheduleChangeFormattingProcess PROCESS = new ScheduleChangeFormattingProcess();
  private static final String MESSAGE_NEW_SESSION =
    "@everyone \nChangement d'emploi du temps :information_source:\n```"
    + "\n\nNOUVEAU COURS :"
    + "\nINFORMATIQUE - le 01-01-2020 de 14:00 à 15:00 (M. Prof) - A01"
    + "\n```";
  private static final String MESSAGE_UPDATED_SESSION =
    "@everyone \nChangement d'emploi du temps :information_source:\n```"
    + "\n\nMODIFICATION :"
    + "\nINFORMATIQUE - le 01-01-2020 de 14:00 à 15:00 (M. Prof) - A01"
    + "\n> Cours supprimés/modifiés :"
    + "\n    - Informatique (14:00 - 15:00)"
    + "\n```";
  private static final String MESSAGE_MULTIPLE_NEW_SESSIONS =
    "@everyone \nChangement d'emploi du temps :information_source:\n```"
      + "\n\nNOUVEAU COURS :"
      + "\nINFORMATIQUE - le 01-01-2020 de 14:00 à 15:00 (M. Prof) - A01"
      + "\n\nNOUVEAU COURS :"
      + "\nINFORMATIQUE - le 01-01-2020 de 14:00 à 15:00 (M. Prof) - A01"
      + "\n```";
  private static final String MESSAGE_MULTIPLE_UPDATED_SESSIONS =
    "@everyone \nChangement d'emploi du temps :information_source:\n```"
      + "\n\nMODIFICATION :"
      + "\nINFORMATIQUE - le 01-01-2020 de 14:00 à 15:00 (M. Prof) - A01"
      + "\n> Cours supprimés/modifiés :"
      + "\n    - Informatique (14:00 - 15:00)"
      + "\n    - Informatique (14:00 - 15:00)"
      + "\n```";
  private static final String MESSAGE_SESSION_NO_TEACHER =
    "@everyone \nChangement d'emploi du temps :information_source:\n```"
      + "\n\nNOUVEAU COURS :"
      + "\nINFORMATIQUE - le 01-01-2020 de 14:00 à 15:00 - A01"
      + "\n```";
  private static final String MESSAGE_SESSION_NO_LOCATION =
    "@everyone \nChangement d'emploi du temps :information_source:\n```"
      + "\n\nNOUVEAU COURS :"
      + "\nINFORMATIQUE - le 01-01-2020 de 14:00 à 15:00 (M. Prof)"
      + "\n```";
  private static final String MESSAGE_DELETED_SESSION =
    "@everyone \nChangement d'emploi du temps :information_source:\n```"
      + "\n\nCOURS SUPPRIME :"
      + "\nINFORMATIQUE - le 01-01-2020 de 14:00 à 15:00 (M. Prof)"
      + "\n```";
  private static Session SESSION;
  private static final String SESSION_NAME = "Informatique";
  private static final String SESSION_DATE = "01-01-2020";
  private static final String SESSION_START = "14:00";
  private static final String SESSION_END = "15:00";
  private static final String SESSION_TEACHER = "M. Prof";
  private static final String SESSION_LOCATION = "A01";

  @BeforeAll
  public static void init() throws ParseException {
      SESSION = new Session(SESSION_NAME, SESSION_TEACHER, SESSION_LOCATION,
        stringToDate(SESSION_DATE), stringToTime(SESSION_START), stringToTime(SESSION_END), null);
  }

  @Test
  public void testFormat_new_session() {
    List<SessionChange> changes = singletonList(new SessionChange(SESSION, Collections.emptyList()));
    String test = PROCESS.format(changes);
    assertEquals(MESSAGE_NEW_SESSION, test);
  }

  @Test
  public void testFormat_updated_session() {
    List<SessionChange> changes = singletonList(new SessionChange(SESSION, singletonList(SESSION)));
    String test = PROCESS.format(changes);
    assertEquals(MESSAGE_UPDATED_SESSION, test);
  }

  @Test
  public void testFormat_multiple_new_sessions() {
    List<SessionChange> changes = Arrays.asList(
      new SessionChange(SESSION, Collections.emptyList()),
      new SessionChange(SESSION, Collections.emptyList()));
    String test = PROCESS.format(changes);
    assertEquals(MESSAGE_MULTIPLE_NEW_SESSIONS, test);
  }

  @Test
  public void testFormat_multiple_updated_sessions() {
    List<SessionChange> changes = singletonList(new SessionChange(SESSION, Arrays.asList(SESSION, SESSION)));
    String test = PROCESS.format(changes);
    assertEquals(MESSAGE_MULTIPLE_UPDATED_SESSIONS, test);
  }

  @Test
  public void testFormat_no_teacher() throws ParseException {
    Session session = new Session(SESSION_NAME, null, SESSION_LOCATION, stringToDate(SESSION_DATE),
        stringToTime(SESSION_START), stringToTime(SESSION_END), null);
    List<SessionChange> changes = singletonList(new SessionChange(session, Collections.emptyList()));
    String test = PROCESS.format(changes);
    assertEquals(MESSAGE_SESSION_NO_TEACHER, test);
  }

  @Test
  public void testFormat_no_location() throws ParseException {
    Session session = new Session(SESSION_NAME, SESSION_TEACHER, null, stringToDate(SESSION_DATE),
        stringToTime(SESSION_START), stringToTime(SESSION_END), null);
    List<SessionChange> changes = singletonList(new SessionChange(session, Collections.emptyList()));
    String test = PROCESS.format(changes);
    assertEquals(MESSAGE_SESSION_NO_LOCATION, test);
  }

  @Test
  public void testFormat_deleted_session() {
    List<SessionChange> changes = singletonList(new SessionChange(null, Collections.singletonList(SESSION)));
    String test = PROCESS.format(changes);
    assertEquals(MESSAGE_DELETED_SESSION, test);
  }
}
