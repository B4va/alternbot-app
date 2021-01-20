package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Gestion du {@link Logger}.
 */
public abstract class LoggerUtils {

  private static final String DEFAULT_LOGGER_NAME = "/DEFAULT/";

  /**
   * Construit une instance du {@link Logger}.
   *
   * @param c classe concernée par l'instance
   * @return logger
   */
  public static <T> Logger buildLogger(Class<T> c) {
    try {
      return LogManager.getLogger(c.getSimpleName());
    } catch (Exception e) {
      return LogManager.getLogger(DEFAULT_LOGGER_NAME);
    }
  }

}