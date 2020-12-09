package utils;

import config.HibernateConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
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

  /**
   * Supprime toutes les instances d'un objet en base de données.
   *
   * @param session session en cours
   * @param c       classe des objets à supprimer
   */
  public static <T> void deleteAll(Session session, Class<T> c) {
    session.createQuery("delete from " + c.getSimpleName()).executeUpdate();
  }

  /**
   * Récupère toutes les instances d'un objet en base de données.
   *
   * @param session session en cours
   * @param c       classe des objets à supprimer
   */
  public static <T> List<T> getAll(Session session, Class<T> c) {
    CriteriaBuilder builder = session.getCriteriaBuilder();
    CriteriaQuery<T> criteria = builder.createQuery(c);
    criteria.from(c);
    return session.createQuery(criteria).getResultList();
  }
}
