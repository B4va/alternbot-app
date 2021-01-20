package process.publication;

import models.Session;
import models.business.SessionChange;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

/**
 * CLasse de test de {@link ScheduleChangeFormattingProcess}.
 */
public class TestScheduleChangeFormattingProcess {

  private static final ScheduleChangeFormattingProcess PROCESS = new ScheduleChangeFormattingProcess();
  private static final String MESSAGE_NEW_SESSION = "@everyone \nChangement d'emploi du temps :information_source:\n```\n"
    + "\nNOUVEAU COURS :"
    + "\nINFORMATIQUE - le 01-01-2020 de 14:00 à 15:00 (M. Prof) - A01"
    + "\n```";
  private static final String NS_NEW_SESSION_NAME = "Informatique";
  private static final String NS_NEW_SESSION_DATE = "01-01-2020";
  private static final String NS_NEW_SESSION_START = "14:00";
  private static final String NS_NEW_SESSION_END = "15:00";
  private static final String NS_NEW_SESSION_TEACHER = "M. Prof";
  private static final String NS_NEW_SESSION_LOCATION = "A01";

  @Test
  public void testFormat_new_session() {
    Session session = null;
    try {
      session = new Session(NS_NEW_SESSION_NAME, NS_NEW_SESSION_TEACHER, NS_NEW_SESSION_LOCATION,
        stringToDate(NS_NEW_SESSION_DATE), stringToTime(NS_NEW_SESSION_START), stringToTime(NS_NEW_SESSION_END), null);
    } catch (ParseException e) {
      e.printStackTrace();
      fail();
    }
    List<SessionChange> changes = Collections.singletonList(new SessionChange(session, Collections.emptyList()));
    String test = PROCESS.format(changes);
    assertEquals(MESSAGE_NEW_SESSION, test);
  }
}
