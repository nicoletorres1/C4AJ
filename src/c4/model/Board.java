package c4.model;

import java.util.ArrayList;
import java.util.List;

import c4.model.Player;

/**
 * Abstraction of a Connect Four game board. A game board consists 
 * of seven slots (columns), and each slot has six places where a
 * player's checker (disc) can be placed by dropping it in the slot. 
 * A place of a board is denoted by a pair of a slot (column) index
 * and a place (row) index; both are 0-based.
 * The slot index increases to the right, and the row index increases
 * to the bottom; for example, the top left place of a board is denoted
 * by (0, 0) and the bottom right place by (6, 5).
 */
public class Board {

    /** Number of slots (columns). */
    private static int WIDTH = 7;
    
    /** Number of places (rows) in each slot. */
    private static int HEIGHT = 6;

    /** Provided interface to notify changes of this board. */
    public interface ChangeListener {
        
        /** Called when a checker is dropped. The indices of 
         * the place and the player are provided as arguments. */
        void checkerDropped(int slot, int row, Player player);
    }
    
    /** Places of this board. Stored are the players who own the 
     * checkers placed on them; null if no checker is placed. */
    private Player[][] places;
    // TODO: refactor to Place[][] places or List<Place] places;

    /** Observe state changes of this board. */
    private ChangeListener changeListener;
    
    /** Winning row of places if not empty. */
    private List<Place> winningRow = new ArrayList<>();

    /** Create a new board. */
    public Board() {
        places = new Player[WIDTH][HEIGHT];
    }
    
    /** Register the given listener. */
    public void setChangeListener(ChangeListener listener) {
        this.changeListener = listener;
    }
    
    /** Clear this board by removing all checkers. */
    public void clear() {
        winningRow.clear();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                places[i][j] = null;
            }
        }
    }
    
    /** Return the number of slots (columns) in this board. */
    public int width() {
        return WIDTH;
    }
    
    /** Return the height of slots in this board. */
    public int height() {
        return HEIGHT;
    }
    
    /** 
     * Return true if a checker can be dropped in the specified slot.
     * 
     * @param i 0-based slot index
     */
    public boolean isSlotOpen(int i) {
        return places[i][0] == null;
    }
    
    /** 
     * Return true if no more checker can be dropped in the specified slot. 
     * 
     * @param i 0-based slot index
     */
    public boolean isSlotFull(int i) {
        return places[i][0] != null;
    }

    /** Are all places occupied? */
    public boolean isFull() {
        for (int i = 0; i < WIDTH; i++) {
            if (isSlotOpen(i)) {
                return false;
            }
        }
        return true;
    }

    /** Is the game over? */
    public boolean isGameOver() {
        return winningRow.size() > 0 || isFull();
    }
    
    /**
     * Slide a checker of the given player in the specified slot
     * and return the place (row) index of the placed checker.
     * If the given slot is full, this method has no effect and
     * -1 is returned.
     *
     * @param slot 0-based slot index
     * @param player Player whose checker is to be dropped
     */
    public int dropInSlot(int slot, Player player) {
        for (int y = HEIGHT - 1; y >= 0; y--) {
            if (places[slot][y] == null) {
                places[slot][y] = player;
                if (changeListener != null) {
                    changeListener.checkerDropped(slot, y, player);
                }
                return y;
            }
        }
        return -1;
    }
    
    /**
     * Is the specified place empty? 
     *
     * @param x 0-based slot (column) index
     * @param y 0-based place (row) index
     */
    public boolean isEmpty(int x, int y) {
        return places[x][y] == null;
    }

    /**
     * Is the specified place occupied? 
     *
     * @param x 0-based slot (column) index
     * @param y 0-based place (row) index
     */
    public boolean isOccupied(int x, int y) {
        return places[x][y] != null;
    }
    
    /** Is the given place occupied? */
    public boolean isOccupied(Place place) {
        return isOccupied(place.x, place.y);
    }

    /**
     * Is the specified place occupied by the given player?
     *
     * @param x 0-based slot (column) index
     * @param y 0-based place (row) index
     */
    public boolean isOccupiedBy(int x, int y, Player player) {
        return places[x][y] == player;
    }
    
    /**
     * Return the player occupying the specified place; 
     * null if the place is empty. 
     *
     * @param x 0-based slot (column) index
     * @param y 0-based place (row) index
     */
    public Player playerAt(int x, int y) {
        return places[x][y];
    }
   
    /** Return true if the given player has a winning row. */
    public boolean isWonBy(Player player) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                boolean won = isWonBy(i, j, 1, 0, player); // horizontal
                won = won || isWonBy(i, j, 0, 1, player);  // vertical
                won = won || isWonBy(i, j, 1, 1, player);  // diagonal(\)
                won = won || isWonBy(i, j, 1, -1, player); // diagonal(/)
                if (won) {
                    return won;
                }
            }
        }
        return false;
    }

    /** Return the winning row. */
    public Iterable<Place> winningRow() {
        return winningRow;
    }
    
    /** Return true if this board has a winning row. */
    public boolean hasWinningRow() {
        return !winningRow.isEmpty();
    }

    /** 
     * Return true if the given player has a winning row containing 
     * the specified place (x, y) in the specified direction (dx, dy),
     * where the direction is horizontal (1,0), vertical (0,1),
     * diagonal \ (1,1), and diagonal / (1, -1).
     */
    private boolean isWonBy(int x, int y, int dx, int dy, Player player) {
        final int FOUR = 4;
        final int[] row = new int[FOUR]; // winning row, encoded: x*100 + y
        
        // check and expand to the left/below of (x, y)
        int cnt = 0;
        int sx = x; // start x
        int sy = y; // start y
        while (!(dx > 0 && sx < 0) && !(dx < 0 && sx >= WIDTH) 
                && !(dy > 0 && sy < 0) && !(dy < 0 && sy >= HEIGHT) 
                && isOccupiedBy(sx, sy, player) && cnt < FOUR) {
            row[cnt++] = sx * 100 + sy; // encode: x * 100 + y
            sx = sx - dx;
            sy = sy - dy;
        }
        
        // check and expand to the right/above of (x, y)
        int ex = x + dx; // end x
        int ey = y + dy; // end y
        while (!(dx > 0 && ex >= WIDTH) && !(dx < 0 && ex < 0) 
                && !(dy > 0 && ey >= HEIGHT) && !(dy < 0 && ey < 0) 
                && isOccupiedBy(ex, ey, player) && cnt < FOUR) {
            row[cnt++] = ex * 100 + ey; // encode: x * 100 + y
            ex = ex + dx;
            ey = ey + dy;
        }
        if (cnt >= FOUR) {
            for (int xy: row) {
                winningRow.add(new Place(xy / 100, xy % 100));
            }
        }
        return cnt >= FOUR;
    }

    /** Clear the given place. */
    protected void clearPlace(int slot, int y) {
        places[slot][y] = null;
    }

    /**
     * Denote a place on a board by specifying its 0-based slot (x) 
     * and place (y) indices.
     */
    public static class Place {
        /** 0-based slot index of this place. */
        public final int x;

        /** 0-based place index of this place. */
        public final int y;

        /** Create a new place of the given indices. 
         * 
         * @param x 0-based slot (vertical) index
         * @param y 0-based place (horizontal) index
         */
        public Place(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
