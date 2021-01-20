package process;

import models.Server;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import javax.security.auth.login.LoginException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public abstract class Publication {

  private static final Logger LOGGER = LoggerUtils.buildLogger(Publication.class);

  /**
   * Envoie un message sur l'un des channel d'un serveur Discord.
   *
   * @param message à poster sur le server
   * @param server  qui reçoit le message
   * @throws LoginException       erreur de connexion au serveur discord
   * @throws InterruptedException connexion rompue
   */
  protected boolean sendMessage(String message, Server server, String channel) throws LoginException, InterruptedException {
    Message result = null;
    JDA jda = JDABuilder.createDefault(server.getReference()).build();
    jda.awaitReady();
    if (hasChannel(jda, channel)) {
      TextChannel textChannel = jda.getTextChannelsByName(channel, true).get(0);
      try {
        if (isNull(textChannel)) {
          LOGGER.warn(
            "Erreur lors de l'envoi d'un message dans un channel. Serveur : {}, Channel : {}",
            server.getReference(), channel);
        }
        result = textChannel.sendMessage(message).complete();
      } catch (RuntimeException e) {
        LOGGER.warn(
          "Erreur lors de l'envoi d'un message. Serveur : {}",
          server.getReference());
      }
    }
    return nonNull(result);
  }

  private boolean hasChannel(JDA jda, String channel) {
    return !jda.getTextChannelsByName(channel, true).isEmpty();
  }


}
