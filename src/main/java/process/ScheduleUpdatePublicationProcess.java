package process;


import models.Server;
import models.Session;

import javax.security.auth.login.LoginException;
import java.text.SimpleDateFormat;


public class ScheduleUpdatePublicationProcess extends Publication{

    /**
     *
     * @param oldSession, session qui est updated
     * @param newSession, nouvelle session si le cours est reporté, si null est supprimé
     * @throws LoginException
     * @throws InterruptedException
     */
    public void sendPublication(Session oldSession, Session newSession) throws LoginException, InterruptedException {
        String message = "@everyone \nChangement d'emploi du temps :information_source:```";
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE dd MMMMM");
        message += "Cours du " + sdf2.format(oldSession.getDate()) + " de " + oldSession.getName() + " (" + sdf.format(oldSession.getStart()) + " - " + sdf.format(oldSession.getEnd()) + ") avec " + oldSession.getTeacher();
        if(newSession != null){
            message += " est reporté au " + sdf2.format(newSession.getDate()) + " (" + sdf.format(newSession.getStart()) + " - " + sdf.format(newSession.getEnd()) + ")";
        }else{
            message += " est supprimé";
        }
        message += "```";
        for(Server s: oldSession.getSchedule().getServers()) {
            sendMessage(message,s);
        }
    }



}
