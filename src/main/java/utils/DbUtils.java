package utils;

import config.HibernateConfiguration;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Objects;

/**
 * Gestion des opérations sur la base de données.
 */
public abstract class DbUtils {

  private static SessionFactory sessionFactory;

  /**
   * Instancie et/ou retourne la {@link SessionFactory}.
   *
   * @return singleton SessionFactory
   */
  public static synchronized SessionFactory getSessionFactory() {
    Configuration configuration = HibernateConfiguration.getConfiguration();
    if (Objects.isNull(sessionFactory)) {
      sessionFactory = configuration.buildSessionFactory();
    }
    return sessionFactory;
  }
}
