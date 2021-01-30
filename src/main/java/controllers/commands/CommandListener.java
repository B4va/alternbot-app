package controllers.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import utils.LoggerUtils;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.nonNull;
import static utils.JDAUtils.getJDAInstance;

/**
 * Déclenche le process associé à une commande lorsque celle-ci est saisie par un utilisateur.
 */
public abstract class CommandListener implements Runnable {

  private static final Logger LOGGER = LoggerUtils.buildLogger(CommandListener.class);

  /**
   * Lance le listener à partir du {@link net.dv8tion.jda.api.JDA}. Les traitements associés à la
   * commande sont gérés dans les classes filles via {@link CommandListener::handleCommand}.
   */
  @Override
  public void run() {
    getJDAInstance().addEventListener(new ListenerAdapter() {
      @Override
      public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (isCommand(message)) {
          LOGGER.debug("Commande reçue : {} ; Serveur : {}", getCommand(), event.getGuild().getId());
          List<String> parsedMessage = parseCommand(message);
          handleCommand(event, parsedMessage);
          LOGGER.debug("Commande traitée : {} ; Serveur : {}", getCommand(), event.getGuild().getId());
        }
      }
    });
  }

  /**
   * Retourne la commande gérée.
   *
   * @return commande
   */
  protected abstract String getCommand();

  /**
   * Traitement associé à la commande.
   *
   * @param event   événement détecté
   * @param message message parsé
   */
  protected abstract void handleCommand(GuildMessageReceivedEvent event, List<String> message);

  /**
   * Détermine si un message correspond à une commande.
   *
   * @param message message analysé
   * @return true si le message correspond à la commande
   */
  protected boolean isCommand(String message) {
    return message.startsWith(getCommand() + " ") || message.equals(getCommand());
  }

  /**
   * Sépare les différents éléments de la commande dans une {@link List<String>}.
   *
   * @param message message à parser
   * @return liste des éléments de la commande
   */
  protected List<String> parseCommand(String message) {
    return Arrays.asList(message.split(" "));
  }

  /**
   * Détermine si un message contient un paramètre.
   *
   * @param message   message parsé sous forme de {@link List<String>}
   * @param parameter paramètre
   * @return true si la commande contient le paramètre
   */
  protected boolean hasParameter(List<String> message, String parameter) {
    return message.contains(parameter);
  }

  /**
   * Récupère un paramètre dans une commande.
   *
   * @param message   message parsé sous forme de {@link List<String>}
   * @param parameter paramètre à récupérer
   * @return valeur du paramètre
   */
  protected String getParameter(List<String> message, String parameter) {
    String param = message.stream()
      .filter(s -> s.startsWith(parameter))
      .findAny()
      .orElse(null);
    if (nonNull(param)) {
      try {
        return message.get(message.indexOf(param) + 1);
      } catch (IndexOutOfBoundsException e) {
        return null;
      }
    } else {
      return null;
    }
  }
}
