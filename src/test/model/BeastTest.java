package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BeastTest {
    private Beast beast;

    @BeforeEach
    public void setUp() {
        beast = new Beast();
    }

    @Test
    public void testGetBeast() {
        assertEquals(beast.getBeastPosition().getX(), 1);
    }
}
