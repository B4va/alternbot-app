package process.commons;

import models.dao.Server;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.utils.AttachmentOption;
import org.apache.logging.log4j.Logger;
import utils.JDAUtils;
import utils.LoggerUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
    Guild guild = JDAUtils.getGuildFromServer(server);
    if (isNull(guild)) return false;

    TextChannel textChannel = JDAUtils.getOrCreateTextChannel(guild, channel);
    if (isNull(textChannel)) return false;

    if (message.length() > 2000) {
      for (String partieMessage : decomposerMessage(message))
        if (!doSendMessage(textChannel, partieMessage))
          return false;

      return true;
    } else {
      return doSendMessage(textChannel, message);
    }
  }

  /**
   * Poste un fichier sur un channel d'un serveur Discord.
   *
   * @param fileData  contenu du fichier à poster
   * @param fileName  nom du fichier tel qu'il apparaîtra sur Discord
   * @param isSpoiler indique si il faut marquer le fichier comme spoiler ou pas
   * @param server    serveur sur lequel le fichier sera posté
   * @param channel   nom du channel sur lequel le fichier sera posté
   * @return {@code true} si le fichier a été posté avec succès, {@code false} en cas d'erreur
   */
  protected boolean sendFile(byte[] fileData, String fileName, boolean isSpoiler, Server server, String channel) {
    Guild guild = JDAUtils.getGuildFromServer(server);
    if (isNull(guild)) return false;

    TextChannel textChannel = JDAUtils.getOrCreateTextChannel(guild, channel);
    return nonNull(textChannel) && doSendFile(textChannel, fileData, fileName, isSpoiler);
  }

  /**
   * Décompose le message en tenant compte des sauts de lignes et du
   * formattage en mode code (```).
   *
   * @param message à décomposer
   * @return le message sous forme de sous messages
   */
  private List<String> decomposerMessage(String message) {
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

  private boolean doSendMessage(TextChannel channel, String message) {
    try {
      return nonNull(channel.sendMessage(message).complete());
    } catch (RuntimeException e) {
      LOGGER.warn("Erreur lors de l'envoie d'un message - Longueur: {} - Serveur: {} ; Channel {}",
              message.length(), channel.getGuild().getId(), channel.getName());
      return false;
    }
  }

  private boolean doSendFile(TextChannel channel, byte[] fileData, String fileName, boolean isSpoiler) {
    try {
      MessageAction msg = isSpoiler ? channel.sendFile(fileData, fileName, AttachmentOption.SPOILER) : channel.sendFile(fileData, fileName);
      return nonNull(msg.complete());
    } catch (RuntimeException e) {
      LOGGER.warn("Erreur lors de l'envoie d'un fichier - Fichier: {} ; Serveur: {} ; Channel {}",
              fileName, channel.getGuild().getId(), channel.getName());
      return false;
    }
  }
}
