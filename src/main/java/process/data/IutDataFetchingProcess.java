package process.data;

import models.Schedule;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Process de récupération des données IUT.
 */
public class IutDataFetchingProcess {

  private static final Logger LOGGER = LoggerUtils.buildLogger(IutDataFetchingProcess.class);

  /**
   * Récupére les données des cours associés à un emploi du temps.
   *
   * @param schedule emploi du temps
   * @return données ical
   */
  public String fetch(Schedule schedule) {
    String data = null;
    try {
      URL url = new URL(schedule.getUrl());
      try (Scanner sc = new Scanner(url.openStream())) {
        StringBuilder sb = new StringBuilder();
        while (sc.hasNext()) {
          sb.append(sc.nextLine()).append("\n");
        }
        data = sb.toString();
      } catch (IOException e) {
        LOGGER.warn("Impossible d'accéder à l'URL : {}", url);
      }
    } catch (MalformedURLException e) {
      LOGGER.warn("Mauvais format de l'URL : {}", schedule.getUrl());
    }
    return data;
  }
}
