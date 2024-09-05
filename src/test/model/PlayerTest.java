package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerTest {
    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player();
    }

    @Test
    public void testFallBack() {
        assertEquals(player.getPlayerPosition().getX(), 7);
        player.fallBack();
        assertEquals(player.getPlayerPosition().getX(), 5);
        player.fallBack();
        assertEquals(player.getPlayerPosition().getX(), 3);
        player.fallBack();
        assertEquals(player.getPlayerPosition().getX(), 1);
        assertTrue(player.caught());
        player.getPlayerPosition().setX(1);
        assertEquals(player.getPlayerPosition().getX(), 0);
        assertTrue(player.caught());
    }
}