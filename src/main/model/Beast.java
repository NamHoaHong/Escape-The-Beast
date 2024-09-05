package model;

// The beast that spawns in the left of the screen
public class Beast {
    private Position beastPosition;

    public Beast() {
        this.beastPosition = new Position(1);
    }

    // EFFECT: returns the beast's position
    public Position getBeastPosition() {
        return beastPosition;
    }
}