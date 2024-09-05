package model;

// Player
public class Player {
    private Position playerPosition;

    public Player() {
        this.playerPosition = new Position(35);
    }

    // EFFECT: get player position
    public Position getPlayerPosition() {
        return playerPosition;
    }

    // EFFECT: the player falling back as time pass
    public void fallBack() {
        playerPosition.setX(-5);
    }

    //EFFECT: Advance the player away from the beast
    public void advance() {
        playerPosition.setX(1);
    }

    // EFFECT: checks whether player has been caught by the beast
    public boolean caught() {
        return (playerPosition.getX() <= 1);
    }

    public void reset() {
        playerPosition.setFixedX(7);
    }
}
