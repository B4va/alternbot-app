package utils;

import javax.naming.ConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

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
  public static final String SERVEUR_TEST = "SERVEUR_TEST";
  public static final String SERVEUR_TEST_2 = "SERVEUR_TEST_2";
  public static final String SCHEDULE_URL = "SCHEDULE_URL";

  private static final String ENVIRONMENT_VARIABLES_FILE = "ENVIRONMENT.properties";

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
      return defaultValue;
    } else {
      try {
        return Integer.parseInt(get(variable));
      } catch (NumberFormatException e) {
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
      return defaultValue;
    } else {
      var = var.toLowerCase().trim();
      if (var.equals("true")) {
        return true;
      } else if (var.equals("false")) {
        return false;
      } else {
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
    } catch (IOException | ConfigurationException ioException) {
      return null;
    }
  }
}
