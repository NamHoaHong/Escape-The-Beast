package model;

// x position of the player
public class Position {
    private int xpos;

    public Position(int xpos) {
        this.xpos = xpos;
    }

    public int getX() {
        return xpos;
    }

    public void setX(int i) {
        xpos += i;
    }

    public void setFixedX(int i) {
        xpos = i;
    }
}
