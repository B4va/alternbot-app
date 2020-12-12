package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class TestTests {
    private models.Test instance;

    @BeforeEach
    void setUp() {
        this.instance = new models.Test("blabla");
    }

    @Test
    void getText() {
        Assertions.assertEquals("blabla", this.instance.getText());
    }
}
