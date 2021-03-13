package models;

import models.dao.ModelDAO;

/**
 * Méthodes de test de l'implémentation du système de CRUD pour les {@link ModelDAO}.
 */
public interface TestModel {

  void testCreate();

  void testRead();

  void testUpdate();

  void testDelete();
}
