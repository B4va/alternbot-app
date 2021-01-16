package process;

import models.Schedule;
import models.Server;
import models.Session;

import javax.security.auth.login.LoginException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import static utils.DateUtils.stringToDate;
import static utils.DateUtils.stringToTime;

public class ScheduleUpdateProcess extends Publication{

    /**
     *
     * @param oldSession, session qui est updated
     * @param newSession, nouvelle session si le cours est reporté, si null est supprimé
     * @throws LoginException
     * @throws InterruptedException
     */
    public static void formatter(Session oldSession, Session newSession) throws LoginException, InterruptedException {
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

    public static void main(String[] args) throws LoginException, InterruptedException, ParseException {
        Server s = new Server();
        s.setReference("Nzg1NDk4ODI0Njg0NzMyNDE2.X84uyQ.-dmoFryuzXjTOSryYL9LoR3t2J4");
        Schedule schedule = new Schedule();
        HashSet<Server> servers = new HashSet<Server>();
        servers.add(s);
        schedule.setServers(servers);
        Session old = new Session();
        Date debut = stringToTime("08:00");
        Date fin = stringToTime("10:00");
        Date d = stringToDate("16-01-2021");
        old.setDate(d);
        old.setStart(debut);
        old.setEnd(fin);
        old.setTeacher("M.JOYEUX");
        old.setName("Tests");
        old.setSchedule(schedule);
        Session nouv = new Session();
        d = stringToDate("18-01-2021");
        nouv.setDate(d);
        nouv.setStart(debut);
        nouv.setEnd(fin);
        formatter(old,nouv);
    }

}
