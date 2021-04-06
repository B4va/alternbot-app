package controllers.task.commands;

import controllers.commons.commands.CommandListener;
import exceptions.InvalidDataException;
import exceptions.InvalidIdException;
import exceptions.MemberAccessException;
import exceptions.ServerAccessException;
import models.dao.ModelDAO;
import models.dao.Server;
import models.dao.Task;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import process.task.data.TaskCreationProcess;
import process.task.data.TaskDeletionProcess;
import process.task.data.TaskUpdateProcess;
import process.task.publication.TasksPublicationProcess;

import java.text.ParseException;
import java.time.DateTimeException;
import java.util.*;

import static controllers.task.commands.TaskOperationsCommandListener.ParsingState.*;
import static java.lang.Integer.parseInt;

/**
 * Gère les opérations de CRUD réalisées sur les {@link Task}.
 */
public class TaskOperationsCommandListener extends CommandListener {

  private static final String COMMAND = "$tache";
  private static final String CREATE_PARAMETER = "-c";
  private static final String UPDATE_PARAMETER = "-u";
  private static final String DELETE_PARAMETER = "-d";
  private static final String ERROR_SERVER = " Erreur serveur...";
  private static final String ERROR_FORMAT = " La commande n'est pas correctement formatée. Vous pouvez consulter la documentation via la commande `$help`.";
  private static final String ERROR_SERVER_ACCESS = " Votre serveur n'a pas accès à cette tâche.";
  private static final String ERROR_NO_TASK = " Cette tâche n'existe pas.";
  public static final String MAP_DESCRIPTION = "MAP_DESCRIPTION";
  public static final String MAP_DUE_DATE = "MAP_DUE_DATE";
  public static final String MAP_DUE_TIME = "MAP_DUE_TIME";

  enum ParsingState {
    MESSAGE_START, TASK_NAME, MESSAGE_END
  }

  @Override
  protected String getCommand() {
    return COMMAND;
  }

  @Override
  protected void handleCommand(GuildMessageReceivedEvent event, List<String> message) {
    Server server = ModelDAO.readAll(Server.class).stream()
      .filter(s -> s.getReference().equals(event.getGuild().getId()))
      .findFirst()
      .orElse(null);
    Member member = event.getMember();
    if (hasParameter(message, CREATE_PARAMETER)) {
      runTaskCreation(message, server, member, event);
    } else if (hasParameter(message, UPDATE_PARAMETER)) {
      runTaskUpdate(message, server, member, event);
    } else if (hasParameter(message, DELETE_PARAMETER)) {
      runTaskDeletion(message, server, member, event);
    } else {
      runTaskPublication(event.getGuild().getId(), event.getChannel().getName());
    }
  }

  private void runTaskCreation(List<String> message, Server server, Member member, GuildMessageReceivedEvent event) {
    try {
      Map<String, String> parsedMessage = parseMessage(message);
      if (new TaskCreationProcess().create(
        parsedMessage.get(MAP_DESCRIPTION),
        parsedMessage.get(MAP_DUE_DATE),
        parsedMessage.get(MAP_DUE_TIME),
        member, server)) {
        event.getChannel()
          .sendMessage(member.getAsMention() + " La tâche a bien été créée !")
          .queue();
      } else {
        event.getChannel()
          .sendMessage(member.getAsMention() + ERROR_SERVER)
          .queue();
      }
    } catch (MemberAccessException e) {
      event.getChannel()
        .sendMessage(member.getAsMention() + " Vous n'être pas autorisé à créer une tâche.")
        .queue();
    } catch (InvalidDataException | ArrayIndexOutOfBoundsException | ParseException | DateTimeException e) {
      event.getChannel()
        .sendMessage(member.getAsMention() + ERROR_FORMAT)
        .queue();
    }
  }

  private void runTaskUpdate(List<String> message, Server server, Member member, GuildMessageReceivedEvent event) {
    try {
      int taskId = parseInt(message.get(2));
      Map<String, String> parsedMessage = parseMessage(message);
      if (new TaskUpdateProcess().update(taskId,
        parsedMessage.get(MAP_DESCRIPTION),
        parsedMessage.get(MAP_DUE_DATE),
        parsedMessage.get(MAP_DUE_TIME),
        member, server)) {
        event.getChannel()
          .sendMessage(member.getAsMention() + " La tâche a bien été modifiée !")
          .queue();
      } else {
        event.getChannel()
          .sendMessage(member.getAsMention() + ERROR_SERVER)
          .queue();
      }
    } catch (ServerAccessException e) {
      event.getChannel()
        .sendMessage(member.getAsMention() + ERROR_SERVER_ACCESS)
        .queue();
    } catch (InvalidIdException e) {
      event.getChannel()
        .sendMessage(member.getAsMention() + ERROR_NO_TASK)
        .queue();
    } catch (MemberAccessException e) {
      event.getChannel()
        .sendMessage(member.getAsMention() + " Vous n'être pas autorisé à modifier une tâche.")
        .queue();
    } catch (InvalidDataException | ArrayIndexOutOfBoundsException | NumberFormatException | ParseException | DateTimeException e) {
      event.getChannel()
        .sendMessage(member.getAsMention() + ERROR_FORMAT)
        .queue();
    }
  }

  private void runTaskDeletion(List<String> message, Server server, Member member, GuildMessageReceivedEvent event) {
    try {
      int taskId = parseInt(message.get(2));
      if (new TaskDeletionProcess().delete(taskId, server, member)) {
        event.getChannel()
          .sendMessage(member.getAsMention() + " La tâche a bien été supprimée !")
          .queue();
      } else {
        event.getChannel()
          .sendMessage(member.getAsMention() + ERROR_SERVER)
          .queue();
      }
    } catch (ServerAccessException e) {
      event.getChannel()
        .sendMessage(member.getAsMention() + ERROR_SERVER_ACCESS)
        .queue();
    } catch (InvalidIdException e) {
      event.getChannel()
        .sendMessage(member.getAsMention() + ERROR_NO_TASK)
        .queue();
    } catch (MemberAccessException e) {
      event.getChannel()
        .sendMessage(member.getAsMention() + " Vous n'être pas autorisé à supprimer une tâche.")
        .queue();
    }
  }

  private void runTaskPublication(String serverId, String channel) {
    new TasksPublicationProcess().sendPublication(channel, serverId);
  }

  public static Map<String, String> parseMessage(List<String> message) {
    Map<String, String> res = new HashMap<>();
    ParsingState parsingState = MESSAGE_START;
    List<String> description = new ArrayList<>();
    Iterator<String> iterator = message.iterator();
    List<String> date = new ArrayList<>();
    while (iterator.hasNext()) {
      String s = iterator.next();
      if (s.equals("[")) {
        parsingState = TASK_NAME;
      } else if (s.equals("]")) {
        parsingState = MESSAGE_END;
      } else if (parsingState == TASK_NAME) {
        description.add(s);
      } else if (parsingState == MESSAGE_END) {
        date.add(s);
      }
    }
    res.put(MAP_DESCRIPTION, String.join(" ", description));
    res.put(MAP_DUE_DATE, date.get(0));
    res.put(MAP_DUE_TIME, verifyTime(date.get(1)));
    return res;
  }

  private static String verifyTime(String time) {
    if (time.contains(":")) {
      String[] split = time.split(":");
      if (split.length == 2) {
        int hours = parseInt(split[0]);
        int minutes = parseInt(split[1]);
        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
          throw new DateTimeException("L'horaire doit avoir le format HH:MM");
        }
      }
    }
    return time;
  }
}
