package controllers.general.commands;

import controllers.commons.commands.CommandListener;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import process.general.data.DocumentationParsingProcess;

import java.util.List;

import static process.general.data.DocumentationParsingProcess.DOCUMENTATION_FILE;

/**
 * Gère la commande permettant d'obtenir la documentation du bot.
 */
public class HelpCommandListener extends CommandListener {

  private static final String COMMAND = "$help";

  @Override
  protected String getCommand() {
    return COMMAND;
  }

  @Override
  public void handleCommand(GuildMessageReceivedEvent event, List<String> message) {
    try {
      String doc = new DocumentationParsingProcess().parse(DOCUMENTATION_FILE);
      event.getChannel().sendMessage(doc).queue();
    } catch (Exception e) {
      event.getChannel().sendMessage("Une erreur a eut lieu sur le serveur...").queue();
    }
  }
}
