package process.publication;

import models.Server;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.utils.AttachmentOption;
import org.apache.logging.log4j.Logger;
import utils.LoggerUtils;

import java.util.ArrayList;

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
    if(message.length() > 2000){
      ArrayList<String> messages = decomposerMessage(message);
      sendLongMessage(messages,server,channel);
      return false;
    }
    if (isNull(guild)) return false;
    if (hasChannel(guild, channel)) {
      return doSendMessage(message, server, channel);
    } else {
      return false;
    }
  }

  /**
   *
   * @param message à décomposer
   * @return le message sous forme de sous messages
   */
  protected ArrayList<String> decomposerMessage(String message){
      ArrayList<String> res = new ArrayList<String>();
      int nbDecomposition = message.length()/2000 + 1;
      res.add("(1/"+nbDecomposition+") "+message.substring(0,1999));
      for(int i = 1 ; i<nbDecomposition + 1;i++){
        boolean espaceTrouve = false;
        int j = 2000 * i;
        while(!espaceTrouve && j < message.length()){
          if(message.charAt(j) == ' ') espaceTrouve = true;
          j++;
        }
        if (2000 * i != j) {
          res.add("(" + i + "/" + nbDecomposition + ") " + message.substring(2000 * i, j));
        }
      }
      return res;
  }

  /**
   *
   * @param messages qui sont décomposés du message original en une liste
   * @param server qui reçoit le message
   * @param channel qui reçoit le message
   */
  protected void sendLongMessage(ArrayList<String> messages, Server server, String channel){
    for(String s:messages){
      sendMessage(s,server,channel);
    }
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
    if (!b) LOGGER.debug("Le channel '{}' n'existe pas sur le serveur : {}", channel, guild.getId());
    return b;
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
    } catch (NullPointerException e) {
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
