package controllers.commands.schedule;

import controllers.commands.CommandListener;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import process.publication.DailySchedulePublicationProcess;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Objects.nonNull;
import static utils.DateUtils.stringToDate;
import static utils.JDAUtils.getJDAInstance;

/**
 * Gère la publication de l'emploi du temps via commande utilisateur.
 */
public class SchedulePublicationCommandListener extends CommandListener {

  private static final String COMMAND = "$edt";
  private static final String DATE_PARAMETER = "-d";
  private static final String EXPORT_PARAMETER = "-e";
  private static final String PLUS_PARAMETER = "-p";

  @Override
  public void run() {
    getJDAInstance().addEventListener(new ListenerAdapter() {
      @Override
      public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (isCommand(message, COMMAND)) {
          List<String> m = parseCommand(message);
          Date reqDate = new Date();
          String serverRef = event.getGuild().getId();
          String channel = event.getChannel().getName();
          try {
            reqDate = handlePlusParameter(m, reqDate);
            reqDate = handleDateParameter(m, reqDate);
            if (hasParameter(m, EXPORT_PARAMETER)) {
              // TODO : process export edt (fichier)
            } else {
              new DailySchedulePublicationProcess().sendPublication(reqDate, serverRef, channel);
            }
          } catch (Exception e) {
            event.getChannel().sendMessage(e.getMessage()).queue();
          }
        }
      }
    });
  }

  private Date handlePlusParameter(List<String> m, Date reqDate) throws Exception {
    String plus = getParameter(m, PLUS_PARAMETER);
    if (nonNull(plus)) {
      try {
        int addDays = Integer.parseInt(plus);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, addDays);
        return calendar.getTime();
      } catch (NumberFormatException e) {
        throw new Exception("Mauvais format du paramètre : -p <nb_jours>.");
      }
    }
    return reqDate;
  }

  private Date handleDateParameter(List<String> m, Date reqDate) throws Exception {
    String date = getParameter(m, DATE_PARAMETER);
    if (nonNull(date)) {
      try {
        return stringToDate(date);
      } catch (ParseException e) {
        throw new Exception("Mauvais format du paramètre : -d <JJ-MM-AAAA>.");
      }
    }
    return reqDate;
  }

}

