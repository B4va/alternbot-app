package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe utilitaire de gestion des dates.
 */
public class DateUtils {

  private static final String DATE_FORMAT = "dd-MM-yyyy";
  private static final String TIME_FORMAT = "HH:mm";
  private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
  private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat(TIME_FORMAT);

  /**
   * Formatte une date.
   *
   * @param date objet date
   * @return date au format dd-MM-yyyy
   */
  public static String dateToString(Date date) {
    return DATE_FORMATTER.format(date);
  }

  /**
   * Crée une date.
   *
   * @param date date au format dd-MM-yyyy
   * @return objet date
   * @throws ParseException erreur de parsing
   */
  public static Date stringToDate(String date) throws ParseException {
    return DATE_FORMATTER.parse(date);
  }

  /**
   * Formatte une heure.
   *
   * @param time objet date
   * @return heure au format HH-mm
   */
  public static String timeToString(Date time) {
    return TIME_FORMATTER.format(time);
  }

  /**
   * Crée une heure.
   *
   * @param time heure au format HH-mm
   * @return objet date
   * @throws ParseException erreur de parsing
   */
  public static Date stringToTime(String time) throws ParseException {
    return TIME_FORMATTER.parse(time);
  }
}
