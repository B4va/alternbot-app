package utils;

import models.dao.Server;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static utils.EnvironmentVariablesUtils.BOT_TOKEN;

/**
 * Collection d'outils visant à simplifier l'utilisation de la {@link JDA}.
 */
public class JDAUtils {
  private static final Logger LOGGER = LoggerUtils.buildLogger(JDAUtils.class);
  private static JDA JDA;

  /**
   * Initialise la connexion au bot.
   *
   * @throws LoginException       erreur de token
   * @throws InterruptedException interruption de la connexion
   */
  public static void initializeJDA() throws LoginException, InterruptedException {
    if (isNull(JDA)) {
      JDA = JDABuilder.createDefault(EnvironmentVariablesUtils.getString(BOT_TOKEN)).build();
      JDA.awaitReady();
    }
  }

  /**
   * Renvoie l'instance de connexion au bot.
   *
   * @return instance de connexion
   */
  public static JDA getJDAInstance() {
    return JDA;
  }

  /**
   * Retourne l'objet Guild correspondant à un serveur.
   * Note: {@link Guild} est la représentation faite d'un serveur Discord par la {@link JDA}.
   *
   * @param server Modèle de serveur
   * @return Instance de {@link Guild} correspondant au serveur donné ou `null` en cas d'erreur.
   */
  public static Guild getGuildFromServer(Server server) {
    try {
      return JDA.getGuildById(server.getReference());
    } catch (NumberFormatException | NullPointerException e) {
      LOGGER.warn("Impossible de récupérer un objet Guild correspondant au serveur '{}'", server.getReference());
      return null;
    }
  }

  /**
   * Retourne un channel (salon textuel) d'un serveur Discord.
   *
   * @param guild   Serveur Discord duquel on souhaite récupérer un channel
   * @param channel Nom du channel textuel à récupérer
   * @return Instance de {@link TextChannel} ou `null` en cas d'erreur.
   */
  public static TextChannel getTextChannel(Guild guild, String channel) {
    try {
      return guild.getTextChannelsByName(channel, true).get(0);
    } catch (IndexOutOfBoundsException | NullPointerException e) {
      LOGGER.warn("Le channel '{}' n'existe pas sur le serveur '{}'", channel, guild.getId());
      return null;
    }
  }

  /**
   * Crée et retourne un channel (salon textuel) sur un serveur Discord.
   *
   * @param guild   Serveur Discord surlequel on souhaite créer un channel
   * @param channel Nom du channel textuel à créer
   * @return Instance de {@link TextChannel} ou `null` en cas d'erreur.
   */
  public static TextChannel createTextChannel(Guild guild, String channel) {
    try {
      final TextChannel textChannel = guild.createTextChannel(channel).complete();
      LOGGER.debug("Channel '{}' créé sur le serveur '{}'", textChannel.getName(), guild.getId());
      return textChannel;
    } catch (InsufficientPermissionException e) {
      LOGGER.warn("Échec de la création du channel '{}' sur le serveur '{}' - Permission refusée", channel, guild.getId());
    } catch (NullPointerException e) {
      LOGGER.warn("Échec de la création du channel '{}'", channel);
    }

    return null;
  }

  /**
   * Tente de récupérer un channel d'un serveur Discord, si il n'existe pas il sera créé.
   *
   * @param guild   Serveur Discord
   * @param channel Nom du channel textuel à récupérer
   * @return Instance de {@link TextChannel} ou `null` en cas d'erreur.
   */
  public static TextChannel getOrCreateTextChannel(Guild guild, String channel) {
    final TextChannel textChannel = getTextChannel(guild, channel);
    return nonNull(textChannel) ? textChannel : createTextChannel(guild, channel);
  }
}
