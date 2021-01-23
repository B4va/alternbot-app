package process.publication;

import models.Server;
import models.Session;

import javax.security.auth.login.LoginException;
import java.text.SimpleDateFormat;
import java.util.List;

public class SchedulePublicationProcess extends Publication{

        /**
         *
         * @param edt, liste des cours du jour
         * @throws LoginException
         * @throws InterruptedException
         */
        public void formatter (List<Session> edt) throws LoginException, InterruptedException {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE dd MMMMM");
            String message = "@everyone \nEmploi du temps du jour: ```" + "Le " + sdf2.format(edt.get(0).getDate());
            for(Session s: edt){
                message+= s.getName()+" de "+sdf.format(s.getStart())+ " à " + sdf.format(s.getEnd())+"\n";
            }
           for(Server s: edt.get(0).getSchedule().getServers()){
               sendMessage(message,s,"emplois-du-temps");
           }
        }

}
