package models;

import models.Server;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Classe de test de {@link Server}.
 */
public class TestServer {

    private Server server;
    private Schedule schedule;

    @Before
    public void init(){
        this.server = new Server();
        this.schedule = new Schedule();
        schedule.setId(1);
        schedule.setPromotion("test");
    }

    @Test
    public void TestSetId(){
        server.setId(1);
        assertEquals("L'id devrait être 1, or id="+server.getId(),1,server.getId());
    }

    @Test
    public void TestSetReference(){
        server.setReference("ref");
        assertEquals("La référence devrait être 'ref', or réference="+server.getReference(),"ref",server.getReference());
    }

    @Test
    public void TestSetSchedule(){
        server.setSchedule(schedule);
        assertEquals("L'id du schedule devrait être 1, or id="+server.getSchedule().getId(),server.getSchedule().getId(),1);
        assertEquals("La promotion du schedule devrait être 'test', or promotion="+server.getSchedule().getPromotion(),server.getSchedule().getPromotion(),"test");
    }

}
