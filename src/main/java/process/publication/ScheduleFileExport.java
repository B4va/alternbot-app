package process.publication;

import models.Model;
import models.Server;
import models.Session;
import org.apache.logging.log4j.Logger;
import process.data.ScheduleExportSessionSelectionProcess;
import utils.DateUtils;
import utils.LoggerUtils;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * Process permettant l'export et la publication de l'emploi du temps sous forme d'un fichier iCal.
 */
public class ScheduleFileExport extends Publication {
  private static final Logger LOGGER = LoggerUtils.buildLogger(ScheduleFileExport.class);
  private static final String ICAL_FILE_PREFIX = "edt_";

  /**
   * Publie sur un serveur et un channel donné une sélection de cours sous la forme d'un fichier iCal (.ics).
   * Les cours retenus pour export seront ceux qui ont lieu à la date d'exécution de la fonction et après.
   *
   * @param serverRef   Réf. du serveur sur lequel faire la publication.
   * @param channelName Nom du channel sur lequel le fichier obtenu sera publié.
   * @return {@code true} si la publication s'est faite avec succès, {@code false} si un problème a empêché la publication.
   */
  public boolean export(String serverRef, String channelName) {
    return this.export(serverRef, channelName, Calendar.getInstance().getTime());
  }

  /**
   * Publie sur un serveur et un channel donné une sélection de cours sous la forme d'un fichier iCal (.ics).
   * Les cours retenus pour export seront ceux qui ont lieu à la date donnée et après.
   *
   * @param serverRef   Réf. du serveur sur lequel faire la publication.
   * @param channelName Nom du channel sur lequel le fichier obtenu sera publié.
   * @param from        Date à laquelle l'emploi du temps publiée commencera.
   * @return {@code true} si la publication s'est faite avec succès, {@code false} si un problème a empêché la publication.
   */
  public boolean export(String serverRef, String channelName, Date from) {
    if (isNull(serverRef) || isNull(channelName) || isNull(from)) {
      LOGGER.warn("Un ou plusieurs paramètres sont null");
      return false;
    }

    final Server server = Model.readAll(Server.class).stream()
      .filter(srv -> srv.getReference().equals(serverRef))
      .findAny().orElse(null);
    if (isNull(server) || isNull(server.getSchedule())) {
      LOGGER.warn("Aucun serveur correspondant à la ref \"{}\" ou pas d'EDT rattaché au serveur", serverRef);
      return false;
    }

    final List<Session> sessions = new ScheduleExportSessionSelectionProcess().select(server.getSchedule(), from);
    if (isNull(sessions) || sessions.isEmpty()) {
      LOGGER.warn("Échec du processus de sélection des cours - Serveur: {} - Date: {}", serverRef, from);
      return false;
    }

    final byte[] fileContent = new ScheduleExportFormattingProcess().format(server, sessions).getBytes(StandardCharsets.UTF_8);
    if (isNull(fileContent)) {
      LOGGER.warn("Échec du processus de formatage de l'emploi du temps - Serveur: {} - Date: {}", serverRef, from);
      return false;
    }

    final String fileName = ICAL_FILE_PREFIX + DateUtils.dateToString(from) + ".ics";
    return this.sendFile(fileContent, fileName, false, server, channelName);
  }
}
