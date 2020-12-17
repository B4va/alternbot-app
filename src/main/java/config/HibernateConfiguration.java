package config;

import models.Schedule;
import models.Server;
import org.hibernate.cfg.Configuration;
import utils.EnvironmentVariablesUtils;

/**
 * Configuration d'Hibernate permettant d'ouvrir une session. {@link utils.DbUtils}
 * TODO : Gérer pool de connexion - voir c3p0
 */
public class HibernateConfiguration {

  private HibernateConfiguration() {
  }

  /**
   * Paramètre la configuration Hibernate.
   *
   * @return configuration
   */
  public static Configuration getConfiguration() {
    Configuration configuration = new Configuration();
    setDatabaseConfiguration(configuration);
    registerEntities(configuration);
    return configuration;
  }

  /**
   * Référence les modèles mappés par Hibernate.
   */
  private static void registerEntities(Configuration configuration) {
    configuration.addAnnotatedClass(Schedule.class);
    configuration.addAnnotatedClass(Server.class);
  }

  private static void setDatabaseConfiguration(Configuration configuration) {
    final String url = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.DB_URL);
    final String user = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.DB_USER);
    final String password = EnvironmentVariablesUtils.getString(EnvironmentVariablesUtils.DB_PASSWORD);
    configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
    configuration.setProperty("hibernate.connection.url", url);
    configuration.setProperty("hibernate.connection.username", user);
    configuration.setProperty("hibernate.connection.password", password);
    configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
  }
}
