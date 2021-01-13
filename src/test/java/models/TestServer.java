package models;

import models.Server;
import org.junit.*;
import org.junit.jupiter.api.AfterAll;

import static org.junit.Assert.*;

/**
 * Classe de test de {@link Server}.
 */
public class TestServer implements TestModel{

    private int ID;
    private Server server;
    private Schedule schedule;

    @BeforeAll
    public void init(){
        this.server = new Server();
        this.schedule = new Schedule();
        schedule.setId(1);
        schedule.setPromotion("test");
        this.server.setSchedule(schedule);
    }

    @AfterAll
    public void tearDown(){
        this.server = Model.read(ID, Server.class);
        if(nonNull(this.server)){
            this.server.delete();
        }
        this.schedule = Model.read(ID, Schedule.class);
        if(nonNull(this.schedule)){
            this.server.delete();
        }
    }

    @Test
    @Order(1)
    @Override
    public void testCreate(){
        this.server.setReference("ref");
        this.server.setId(1);
        this.server.setSchedule(schedule);
        this.schedule.create();
        this.server.create();
        List<Server> servers = Model.readAll(Server.class);
        Server serv = servers.stream().filter(s -> s.getReference().equals("ref")).findFirst().orElse(null);
        if (nonNull(server)) ID = server.getId();
        assertNotNull(server);
    }

    @Test
    @Order(2)
    public void testCreate_server_null() {
        Server s = new Server();
        assertThrows(PersistenceException.class, s::create);
    }

    @Test
    @Order(3)
    @Override
    public void testRead() {
        Server s = Model.read(ID, Server.class);
        assertNotNull(s);
        assertAll(
                () -> assertNotNull(s),
                () -> assertEquals(s.getId(), this.server.getId()),
                () -> assertEquals(s.getReference(), this.server.getReference()),
                () -> assertEquals(s.getSchedule(),this.server.getSchedule())
        );
    }

    @Test
    @Order(4)
    public void testUpdate_Schedule_null() {
        this.server.setSchedule(null);
        assertThrows(PersistenceException.class, this.server::update);
    }

    @Test
    @Order(5)
    @Override
    public void testUpdate() {
        this.server.setReference("UPDATED");
        this.server.setSchedule(this.schedule);
        this.server.update();
        this.server = Model.read(ID, Server.class);
        assertNotNull(this.server);
        assertEquals(this.server.getReference(), "UPDATED");
    }

    @Test
    @Order(6)
    @Override
    public void testDelete() {
        this.server.delete();
        Server s = Model.read(ID, Server.class);
        assertNull(s);
    }

}
