package c4.base;

import java.awt.*;

import c4.model.Player;

/** An player who knows his or her checker (disck) color. */
public class ColorPlayer extends Player {

    /** Color of this player's checkers. */
    private final Color color;

    /** Create a new player of the given name and checker color. */
    public ColorPlayer(String name, Color color) {
        super(name);
        this.color = color;
    }

    /** Return the color of this player's checkers. */
    public Color color() {
        return color;
    }
    
}
