package process.schedule.publication;

import models.dao.Server;
import models.dao.Session;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import java.util.Calendar;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Process permettant de formatter une liste de cours en un format iCal, destiné à l'export.
 */
public class ScheduleExportFormattingProcess {
  private static final Logger LOGGER = LoggerUtils.buildLogger(ScheduleExportFormattingProcess.class);
  private static final String ICAL_PRODUCT_AUTHOR = "Altern'Bot Dev Team";
  private static final String ICAL_PRODUCT_NAME = "Altern'Bot";

  private TimeZone icalTimezone;
  private final boolean isIcalTimezoneValid;

  /**
   * Constructeur par défaut.
   * Utilise la timezone "Europe/Paris".
   */
  public ScheduleExportFormattingProcess() {
    this("Europe/Paris");
  }

  /**
   * Constructeur qui utilise une timezone.
   */
  public ScheduleExportFormattingProcess(String timeZone) {
    // Init objets timezone
    this.icalTimezone = null;
    TimeZoneRegistry tzFactory = TimeZoneRegistryFactory.getInstance().createRegistry();
    if (nonNull(tzFactory) && nonNull(timeZone) && !timeZone.isEmpty()) {
      this.icalTimezone = tzFactory.getTimeZone(timeZone);
      this.isIcalTimezoneValid = nonNull(this.icalTimezone);
    } else {
      LOGGER.warn("Échec de la construction des objets timezone");
      this.isIcalTimezoneValid = false;
    }
  }

  /**
   * Formate une liste de cours donnée au format d'un calendrier iCal.
   *
   * @param server   Serveur qui contient les cours à formater.
   * @param sessions Liste de cours à formater.
   * @return Contenu du calendrier iCal obtenu ou {@code null} en cas d'erreur.
   */
  public String format(Server server, List<Session> sessions) {
    if (isNull(server) || isNull(sessions) || sessions.isEmpty()) {
      LOGGER.warn("Un ou plusieurs paramètres sont null ou la liste des cours est vide.");
      return null;
    }

    if (!this.isIcalTimezoneValid)
      LOGGER.warn("Création d'un calendrier iCal sans infos de timezone - Serveur : {}", server.getReference());
    net.fortuna.ical4j.model.Calendar ical = this.createIcalObject();
    for (Session session : sessions)
      if (!this.addSessionToIcal(session, ical))
        LOGGER.warn("Échec de l'ajout d'un cours au calendrier iCal - Serveur : {}", server.getReference());

    final String iCalContent = ical.toString();
    if (iCalContent.length() == 0) {
      LOGGER.warn("Échec du formatage des cours - Serveur : {}", server.getReference());
      return null;
    }

    return iCalContent;
  }

  /**
   * Crée et retourne un objet {@link net.fortuna.ical4j.model.Calendar} initialisé.
   */
  private net.fortuna.ical4j.model.Calendar createIcalObject() {
    net.fortuna.ical4j.model.Calendar ical = new net.fortuna.ical4j.model.Calendar();
    ical.getProperties().add(Version.VERSION_2_0);
    ical.getProperties().add(new ProdId("-//" + ICAL_PRODUCT_AUTHOR + "//" + ICAL_PRODUCT_NAME + "//EN"));
    if (this.isIcalTimezoneValid)
      ical.getComponents().add(this.icalTimezone.getVTimeZone());
    return ical;
  }

  /**
   * Utilise les données d'un objet {@link Session} pour créer et ajouter un event à un calendrier iCal.
   *
   * @return {@code true} en cas de succès, sinon {@code false}
   */
  private boolean addSessionToIcal(Session session, net.fortuna.ical4j.model.Calendar ical) {
    Calendar cal = Calendar.getInstance();

    // Récupère les heures de début et fin de cours
    cal.setTime(session.getStart());
    final int startHour = cal.get(Calendar.HOUR_OF_DAY);
    final int startMin = cal.get(Calendar.MINUTE);
    cal.clear();
    cal.setTime(session.getEnd());
    final int endHour = cal.get(Calendar.HOUR_OF_DAY);
    final int endMin = cal.get(Calendar.MINUTE);

    // Création des dates de début et fin de cours
    cal.setTime(session.getDate());
    cal.set(Calendar.HOUR_OF_DAY, startHour);
    cal.set(Calendar.MINUTE, startMin);
    final DateTime startDayTime = this.isIcalTimezoneValid ? new DateTime(cal.getTime(), this.icalTimezone)
      : new DateTime(cal.getTime());
    if (this.isIcalTimezoneValid)
      startDayTime.setTimeZone(this.icalTimezone);
    cal.set(Calendar.HOUR_OF_DAY, endHour);
    cal.set(Calendar.MINUTE, endMin);
    final DateTime endDayTime = this.isIcalTimezoneValid ? new DateTime(cal.getTime(), this.icalTimezone)
      : new DateTime(cal.getTime());

    final String eventName = session.getType() != null ? session.getName() + " - " + session.getType() : session.getName();
    VEvent event = new VEvent(startDayTime, endDayTime, eventName);
    event.getProperties().add(new Location(session.getLocation()));
    return ical.getComponents().add(event);
  }
}
