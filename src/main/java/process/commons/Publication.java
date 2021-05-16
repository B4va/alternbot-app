package process.commons;

import models.dao.Server;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.utils.AttachmentOption;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import java.util.ArrayList;
import java.util.List;

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
    Guild guild = getGuild(server);
    if (isNull(guild))
      return false;
    if (!hasChannel(guild, channel))
      return false;

    if(message.contains("Aucun cours prévu ce jour.") || message.contains("Aucune tâche en cours")){
      return false;
    }

    if (message.length() > 2000) {
      List<String> messages = decomposerMessage(message);
      sendLongMessage(messages, server, channel);
      return true;
    } else {
      return doSendMessage(message, server, channel);
    }
  }

  /**
   * Décompose le message en tenant compte des sauts de lignes et du
   * formattage en mode code (```).
   *
   * @param message à décomposer
   * @return le message sous forme de sous messages
   */
  protected List<String> decomposerMessage(String message) {
    String[] splited = message.split("\n");
    int i = 0;
    int codeMarker = 0;
    List<String> messages = new ArrayList<>();
    StringBuilder stringBuilder = null;
    while (i < splited.length) {
      if (isNull(stringBuilder)) {
        stringBuilder = new StringBuilder();
        if (codeMarker % 2 != 0) stringBuilder.append("```\n");
      } else if ((stringBuilder.toString() + splited[i]).length() > 1900) {
        if (codeMarker % 2 != 0) stringBuilder.append("\n```");
        messages.add(stringBuilder.toString());
        stringBuilder = null;
      } else {
        stringBuilder.append(splited[i]).append("\n");
        if (splited[i].equals("```")) codeMarker++;
        i++;
      }
    }
    if (nonNull(stringBuilder)) messages.add(stringBuilder.toString());
    return messages;
  }

  /**
   * @param messages qui sont décomposés du message original en une liste
   * @param server   qui reçoit le message
   * @param channel  qui reçoit le message
   */
  protected void sendLongMessage(List<String> messages, Server server, String channel) {
    messages.forEach(m -> doSendMessage(m, server, channel));
  }

  /**
   * Poste un fichier sur l'un des channel d'un serveur Discord.
   *
   * @param fileData  contenu du fichier à poster
   * @param fileName  nom du fichier tel qu'il apparaîtra sur Discord
   * @param isSpoiler indique si il faut marquer le fichier comme spoiler ou pas
   * @param server    serveur sur lequel le fichier sera posté
   * @param channel   nom du channel sur lequel le fichier sera posté
   * @return {@code true} si le fichier a été posté avec succès, {@code false} en cas d'erreur
   */
  protected boolean sendFile(byte[] fileData, String fileName, boolean isSpoiler, Server server, String channel) {
    Guild guild = getGuild(server);
    if (isNull(guild)) return false;
    if (hasChannel(guild, channel)) {
      return doSendFile(fileData, fileName, isSpoiler, server, channel);
    } else {
      return false;
    }
  }

  private boolean hasChannel(Guild guild, String channel) {
    boolean b = !guild.getTextChannelsByName(channel, true).isEmpty();
    if (!b) {
      LOGGER.debug("Le channel '{}' n'existe pas sur le serveur : {}", channel, guild.getName());
      return createChannel(guild, channel);
    }

    return true;
  }

  private boolean createChannel(Guild guild, String channel) {
    try {
      return !guild.createTextChannel(channel).complete().getId().isEmpty();
    } catch (InsufficientPermissionException e) {
      LOGGER.warn("Impossible de créer le channel '{}' sur le serveur '{}' - Permission refusée", channel, guild.getName());
    } catch (NullPointerException e) {
      LOGGER.warn("Impossible de créer le channel '{}'", channel);
    }

    return false;
  }

  private Guild getGuild(Server server) {
    try {
      return getJDAInstance().getGuildById(server.getReference());
    } catch (NumberFormatException | NullPointerException e) {
      LOGGER.warn("Impossible de trouver le Serveur : {}", server.getReference());
      return null;
    }
  }

  private TextChannel getChannel(Guild guild, String channel) {
    try {
      return guild.getTextChannelsByName(channel, true).get(0);
    } catch (IndexOutOfBoundsException | NullPointerException e) {
      LOGGER.debug("Le channel '{}' n'existe pas sur le serveur : {}", channel, guild.getId());
      return null;
    }
  }

  private boolean doSendMessage(String message, Server server, String channel) {
    Guild guild = getGuild(server);
    if (nonNull(guild)) {
      TextChannel textChannel = getChannel(guild, channel);
      if (nonNull(textChannel)) {
        try {
          return nonNull(textChannel.sendMessage(message).complete());
        } catch (RuntimeException e) {
          LOGGER.warn(
            "Erreur lors de l'envoi d'un message. Serveur : {} ; longueur msg : {}",
            server.getReference(), message.length());
          return false;
        }
      }
    }
    return false;
  }

  private boolean doSendFile(byte[] fileData, String fileName, boolean isSpoiler, Server server, String channel) {
    Guild guild = getGuild(server);
    if (nonNull(guild)) {
      TextChannel textChannel = getChannel(guild, channel);
      if (nonNull(textChannel)) {
        try {
          MessageAction msg = isSpoiler ? textChannel.sendFile(fileData, fileName, AttachmentOption.SPOILER) : textChannel.sendFile(fileData, fileName);
          return nonNull(msg.complete());
        } catch (RuntimeException e) {
          LOGGER.warn(
            "Erreur lors de l'envoi d'un fichier. Serveur : {} ; Fichier : {}",
            server.getReference(), fileName);
          return false;
        }
      }
    }
    return false;
  }
}
