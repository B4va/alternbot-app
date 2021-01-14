package process;

import models.Server;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.security.auth.login.LoginException;

public abstract class Publication {


    protected void sendMessage(String message, Server server, String channel) throws LoginException, InterruptedException {
        JDA jda = JDABuilder.createDefault(server.getReference()).build();
        jda.awaitReady();
        TextChannel textChannel = jda.getTextChannelsByName(channel,true).get(0);
        if(textChannel != null){
            textChannel.sendMessage(message).queue();
        }
    }
}
