package controllers.commands;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.nonNull;

/**
 * Déclenche le process associé à une commande lorsque celle-ci est saisie par un utilisateur.
 */
public abstract class CommandListener implements Runnable {

  /**
   * Détermine si un message correspond à une commande.
   *
   * @param message message analysé
   * @param command commande correspondante
   * @return true si le message correspond à la commande
   */
  protected boolean isCommand(String message, String command) {
    return message.startsWith(command + " ") || message.equals(command);
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
