package process.publication;

import models.Server;
import models.businessmodels.SessionChange;

import javax.security.auth.login.LoginException;
import java.util.List;

/**
 * Process de notification des mises à jour de l'emploi du temps.
 */
public class ScheduleUpdatePublicationProcess extends Publication {

  private static final String CHANNEL = "emploi-du-temps";

  /**
   * Envoie une publication listant les mises à jour de l'emploi du temps
   * sur le serveur Discord concerné.
   *
   * @param changes modifications de l'emploi du temps
   * @param server  serveur Discord
   * @throws LoginException       erreur de connexion au serveur Discord
   * @throws InterruptedException connexion interrompue
   */
  public void sendPublication(List<SessionChange> changes, Server server) throws LoginException, InterruptedException {
    String message = new ScheduleChangeFormattingProcess().format(changes);
    sendMessage(message, server, CHANNEL);
  }
}
