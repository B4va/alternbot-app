package controllers.commands.general;

import controllers.commands.CommandListener;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import process.data.DocumentationParsingProcess;

import static process.data.DocumentationParsingProcess.DOCUMENTATION_FILE;
import static utils.JDAUtils.getJDAInstance;

/**
 * Gère la commande permettant d'obtenir la documentation du bot.
 */
public class HelpCommandListener extends CommandListener {

  private static final String COMMAND = "$help";

  @Override
  public void run() {
    getJDAInstance().addEventListener(new ListenerAdapter() {
      @Override
      public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (isCommand(message, COMMAND)) {
          try {
            String doc = new DocumentationParsingProcess().parse(DOCUMENTATION_FILE);
            event.getChannel().sendMessage(doc).queue();
          } catch (Exception e) {
            event.getChannel().sendMessage("Une erreur a eut lieu sur le serveur...").queue();
          }
        }
      }
    });
  }
}
