package utils;

import org.apache.logging.log4j.Logger;

import javax.naming.ConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import static utils.LoggerUtils.buildLogger;

/**
 * Accès aux variables d'environnement du programme.
 */
public abstract class EnvironmentVariablesUtils {

  public static final String LOG_LEVEL = "LOG_LEVEL";
  public static final String LOG_FORMAT = "LOG_FORMAT";
  public static final String DB_URL = "DB_URL";
  public static final String DB_USER = "DB_USER";
  public static final String DB_PASSWORD = "DB_PASSWORD";
  public static final String BOT_TOKEN = "BOT_TOKEN";
  public static final String ENVIRONMENT = "ENVIRONMENT";

  private static final Logger LOGGER = buildLogger(EnvironmentVariablesUtils.class);
  private static final String ENVIRONMENT_VARIABLES_FILE = "ENVIRONMENT.properties";

  private static final String ERR_GET_INT = "La variable d'environnement '{}' ne peut pas être convertie en Integer.";
  private static final String ERR_GET_BOOLEAN = "La variable d'environnement '{}' ne peut pas être convertie en Boolean.";
  private static final String ERR_READ_FILE = "La variable {} n'est pas configurée. Impossible de lire le fichier de variables d'environnement.";
  private static final String MSG_NO_VAR = "La variable d'environnement '{}' n'est pas configurée.";
  private static final String MSG_NO_FILE = "La variable {} n'est pas configurée. Aucun fichier de configuration des variables d'environnement.";
  public static final String MSG_DEFAULT_VALUE = "Valeur par défaut de '{}' : {}.";

  /**
   * Accès à une variable d'environnement de type {@link Integer} ou à sa valeur par défaut.
   *
   * @param variable     nom de la variable
   * @param defaultValue valeur par défaut
   * @return variable d'environnement ou variable par défaut
   */
  public static int getInt(String variable, int defaultValue) {
    String var = get(variable);
    if (Objects.isNull(var)) {
      LOGGER.warn(MSG_DEFAULT_VALUE, variable, defaultValue);
      return defaultValue;
    } else {
      try {
        return Integer.parseInt(get(variable));
      } catch (NumberFormatException e) {
        LOGGER.error(ERR_GET_INT, variable);
        LOGGER.warn(MSG_DEFAULT_VALUE, variable, defaultValue);
        return defaultValue;
      }
    }
  }

  /**
   * Accès à une variable d'environnement de type {@link Integer}.
   *
   * @param variable nom de la variable
   * @return variable d'environnement
   */
  public static int getInt(String variable) {
    return getInt(variable, 0);
  }

  /**
   * Accès à une variable d'environnement de type {@link Boolean} ou à sa valeur par défaut.
   *
   * @param variable     nom de la variable
   * @param defaultValue valeur par défaut
   * @return variable d'environnement ou variable par défaut
   */
  public static boolean getBoolean(String variable, boolean defaultValue) {
    String var = get(variable);
    if (Objects.isNull(var)) {
      LOGGER.warn(MSG_DEFAULT_VALUE, variable, defaultValue);
      return defaultValue;
    } else {
      var = var.toLowerCase().trim();
      if (var.equals("true")) {
        return true;
      } else if (var.equals("false")) {
        return false;
      } else {
        LOGGER.error(ERR_GET_BOOLEAN, variable);
        LOGGER.warn(MSG_DEFAULT_VALUE, variable, defaultValue);
        return defaultValue;
      }
    }
  }

  /**
   * Accès à une variable d'environnement de type {@link Boolean}.
   *
   * @param variable nom de la variable
   * @return variable d'environnement
   */
  public static boolean getBoolean(String variable) {
    return getBoolean(variable, false);
  }

  /**
   * Accès à une variable d'environnement de type {@link String} ou à sa valeur par défaut.
   *
   * @param variable     nom de la variable
   * @param defaultValue valeur par défaut
   * @return variable d'environnement ou variable par défaut
   */
  public static String getString(String variable, String defaultValue) {
    String var = get(variable);
    if (Objects.isNull(var)) {
      LOGGER.warn(MSG_DEFAULT_VALUE, variable, defaultValue);
      return defaultValue;
    } else {
      return var;
    }
  }

  /**
   * Accès à une variable d'environnement de type {@link String}.
   *
   * @param variable nom de la variable
   * @return variable d'environnement
   */
  public static String getString(String variable) {
    return getString(variable, null);
  }

  private static String get(String variable) {
    return Objects.nonNull(System.getenv(variable))
      ? System.getenv(variable)
      : getFromFile(variable);
  }

  private static String getFromFile(String variable) {
    try {
      try (InputStream inputStream = EnvironmentVariablesUtils.class.getResourceAsStream(ENVIRONMENT_VARIABLES_FILE)) {
        Properties prop = new Properties();
        prop.load(inputStream);
        return Optional.ofNullable(prop.getProperty(variable))
          .orElseThrow(ConfigurationException::new);
      }
    } catch (IOException ioException) {
      LOGGER.warn(ERR_READ_FILE, variable);
    } catch (NullPointerException nullPointerException) {
      LOGGER.warn(MSG_NO_FILE, variable);
    } catch (ConfigurationException configurationException) {
      LOGGER.warn(MSG_NO_VAR, variable);
    }
    return null;
  }
}
