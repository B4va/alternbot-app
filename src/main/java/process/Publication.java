package process;

import models.Server;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.security.auth.login.LoginException;

public abstract class Publication {

    /**
     *
     * @param jda, Altern'bot qui est connecté sur le serveur
     * @return true si le channel existe sur le server, sinon false
     */
    protected boolean check_If_Channel_Exists(JDA jda){
        if(jda.getTextChannelsByName("emplois-du-temps",true).size() > 0) return true;
        else return false;
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
        if(check_If_Channel_Exists(jda)) {
            TextChannel textChannel = jda.getTextChannelsByName("emplois-du-temps", true).get(0);
            if (textChannel != null) {
                textChannel.sendMessage(message).queue();
            }
        }
    }


}
