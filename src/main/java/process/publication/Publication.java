package process.publication;

import models.Server;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static utils.JDAUtils.getJDAInstance;

/**
 * Gestion de la publication dans les différents serveurs Discord.
 */
public abstract class Publication {

  private static final Logger LOGGER = LoggerUtils.buildLogger(Publication.class);

  public static final String SCHEDULE_CHANNEL = "emploi-du-temps";

  /**
   * Envoie un message sur l'un des channel d'un serveur Discord.
   *
   * @param message à poster sur le server
   * @param server  qui reçoit le message
   */
  protected boolean sendMessage(String message, Server server, String channel) {
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
    try {
      return jda.getGuildById(server.getReference());
    } catch (NumberFormatException | NullPointerException e) {
      LOGGER.warn("Impossible de trouver le Serveur : {}", server.getReference());
      return null;
    }
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
