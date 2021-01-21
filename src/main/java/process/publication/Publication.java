package process.publication;

import models.Server;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import javax.security.auth.login.LoginException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static utils.JDAUtils.getJDAInstance;

/**
 * Gestion de la publication dans les différents serveurs Discord.
 */
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
    Guild guild = getGuild(server, getJDAInstance());
    if (isNull(guild)) return false;
    if (hasChannel(guild, channel)) {
      return doSendMessage(message, server, channel);
    } else {
      return false;
    }
  }

  private boolean hasChannel(Guild guild, String channel) {
    boolean b = !guild.getTextChannelsByName(channel, true).isEmpty();
    if (!b) LOGGER.debug("Le channel '{}' n'existe pas sur le serveur : {}", channel, guild.getId());
    return b;
  }

  private Guild getGuild(Server server, JDA jda) {
    Guild guild = null;
    try {
      guild = jda.getGuildById(server.getReference());
    } catch (NumberFormatException e) {
      LOGGER.warn("Référence du serveur au mauvais format ; Serveur : {}", server.getReference());
    }
    if (isNull(guild)) {
      LOGGER.warn("Référence du serveur incorrecte ; Serveur : {}", server.getReference());
      return null;
    }
    return guild;
  }

  private boolean doSendMessage(String message, Server server, String channel) {
    TextChannel textChannel = getJDAInstance().getTextChannelsByName(channel, true).get(0);
    try {
      if (isNull(textChannel)) {
        LOGGER.warn(
          "Erreur lors de l'envoi d'un message dans un channel. Serveur : {}, Channel : {}",
          server.getReference(), channel);
        return false;
      }
      return nonNull(textChannel.sendMessage(message).complete());
    } catch (RuntimeException e) {
      LOGGER.warn(
        "Erreur lors de l'envoi d'un message. Serveur : {}",
        server.getReference());
      return false;
    }
  }
}
