package controllers.commands;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.nonNull;

/**
 * Déclenche le process associé à une commande lorsque celle-ci est saisie par un utilisateur.
 */
public abstract class CommandListener implements Runnable {

  protected boolean isCommand(String message, String command) {
    return message.startsWith(command + " ") || message.equals(command);
  }

  protected List<String> parseCommand(String message) {
    return Arrays.asList(message.split(" "));
  }

  protected boolean hasParameter(List<String> message, String parameter) {
    return message.contains(parameter);
  }

  protected String getParameter(List<String> message, String parameter) {
    String param = message.stream()
      .filter(s -> s.startsWith(parameter))
      .findAny()
      .orElse(null);
    if (nonNull(param)) {
      return param.replace(parameter, "");
    } else {
      return null;
    }
  }
}
