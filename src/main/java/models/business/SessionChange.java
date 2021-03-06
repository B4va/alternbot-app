package models.business;

import models.dao.Session;

import java.util.List;

/**
 * Modification de l'emploi du temps.
 */
public class SessionChange {

  private Session newSession;
  private List<Session> replacedSessions;

  /**
   * @param newSession nouvelle session
   * @param replacedSessions sessions remplacées
   */
  public SessionChange(Session newSession, List<Session> replacedSessions) {
    this.newSession = newSession;
    this.replacedSessions = replacedSessions;
  }

  public Session getNewSession() {
    return newSession;
  }

  public void setNewSession(Session newSession) {
    this.newSession = newSession;
  }

  public List<Session> getReplacedSessions() {
    return replacedSessions;
  }

  public void setReplacedSessions(List<Session> replacedSessions) {
    this.replacedSessions = replacedSessions;
  }
}
