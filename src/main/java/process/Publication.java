package process;

import models.Server;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.security.auth.login.LoginException;

public abstract class Publication {

    /**
     *
     * @param jda instance de connexion au serveur Discord
     * @return true si le channel existe sur le server, sinon false
     */
    protected boolean hasChannel(JDA jda){
        return !jda.getTextChannelsByName("emplois-du-temps", true).isEmpty();
    }

    /**
     *
     * @param message à poster sur le server
     * @param server qui reçoit le message
     * @throws LoginException
     * @throws InterruptedException
     */
    protected void sendMessage(String message, Server server) throws LoginException, InterruptedException {
        JDA jda = JDABuilder.createDefault(server.getReference()).build();
        jda.awaitReady();
        if(hasChannel(jda)) {
            TextChannel textChannel = jda.getTextChannelsByName("emplois-du-temps", true).get(0);
            if (textChannel != null) {
                textChannel.sendMessage(message).queue();
            }
        }
    }


}
