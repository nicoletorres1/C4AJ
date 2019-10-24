package c4.base;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import c4.model.Board;
import c4.model.Board.Place;

/**
 * A special panel class to display a connect-four grid modeled by the
 * {@link c4.model.Board} class.
 * 
 * @see c4.model.Board
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel {

    /** Provided interface to notify click events on this panel. */
    public interface ClickListener {
        
        /** Called when an open slot (column) of a board is clicked.
         * The 0-based index of the clicked slot is provided
         * as an argument. */
        void slotClicked(int slot);
    }
         
    /** Width and height of each place in pixels. */
    private final int placeSize = 30;

    /** Background color of the board. */
    private final Color boardColor = new Color(245, 184, 0);

    /** Color to draw an empty place. */
    private final Color placeColor = Color.WHITE;
    
    /** Color to highlight the checkers (discs) of the winning row. */
    private final Color winColor = Color.WHITE;
    
    /** Color to draw checkers to be dropped (the first row). */
    private Color dropColor = Color.BLUE;
    
    /** The listener observing this panel. */
    private ClickListener listener;

    /** Game board. */
    private final Board board;

    /** Number of slots (width) of the board. */
    private final int width;
    
    /** Number of rows (height) of the board. */
    private final int height;
        
    /** Constant denoting an invalid slot index. */
    private static final int NO_SLOT = -1;
    
    /** Create a new instance to display the given board. */
    public BoardPanel(Board board) {
        this.board = board;
        height = board.height();
        width = board.width();
        addMouseListener(mouseAdapter);
    }
    
    /** Register the given listener. */
    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    /** Set the color to draw the first row of checkers. */
    public void setDropColor(Color color) {
    	dropColor = color;
    }
    
    /** Handle a mouse event by reporting it to the listener 
     * if an open slot is clicked. */
    private final MouseAdapter mouseAdapter = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
        	int slot = locateSlot(e.getX(), e.getY());
        	if (slot >= 0 && board.isSlotOpen(slot)) {
        		// report a slot clicking event
        		listener.slotClicked(slot);
        	}
        }        
    };

    /**
     * Given a screen coordinate, determine the index of the corresponding
     * slot (column) of the board; return NO_SLOT if it doesn't correspond
     * to any slot.
     */
    private int locateSlot(int x, int y) {
        final int radius = (placeSize - 4) / 2;
        final int cy = radius + 2;
        for (int i = 0; i < width; i++) {
            int cx = (i * placeSize) + 2 + radius;
            if (isIn(x, y, cx, cy, radius)) {
                return i;
            }
        }
        return NO_SLOT;
    }
         
    /** Overridden here to draw the board along with checkers. */
    @Override
    public void paint(Graphics g) {
        super.paint(g); // clear the background
        drawGrid(g);
        drawDroppableCheckers(g);
        drawPlacedCheckers(g);
    }

    /** Draw the board grid. */ 
    private void drawGrid(Graphics g) {
        g.setColor(boardColor);
        g.fillRect(0, placeSize, placeSize * width, 
                placeSize * height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j <= height; j++) {
                drawChecker(g, placeColor, i, j - 1);
            }
        }
    }
    
    /** Draw the current player's drop-able checkers above the grid.
     * A user clicks these checkers to drop them into the corresponding
     * slots. */
    private void drawDroppableCheckers(Graphics g) {
        for (int i = 0; i < width; i++) {
        	if (board.isSlotOpen(i)) {
        		int margin = 2;
        		drawChecker(g, dropColor, i, -1, margin);
            }
        }
    }
    
    /** 
     * Draw checkers dropped (placed) in the grid. 
     * Checkers are displayed as filled circles. 
     * The last dropped checker, if exists, is highlighted.
     * If there is a winning row, their checkers are highlighted.
     */
    private void drawPlacedCheckers(Graphics g) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                ColorPlayer player = (ColorPlayer) board.playerAt(i, j);
                if (player != null) {
                    drawChecker(g, player.color(), i, j);
                }
            }
        }
        if (board.hasWinningRow()) {
            for (Place p: board.winningRow()) {
                ColorPlayer player = (ColorPlayer) board.playerAt(p.x, p.y);
                drawChecker(g, player.color(), p.x, p.y, true);
            }
        }
    }
    
    /** Draw a checker at the specified place in the given color. */
    private void drawChecker(Graphics g, Color color, int slot, int y) {
        drawChecker(g, color, slot, y, false);
    }
    
    /** Draw a checker at the specified place in the given color. */
    private void drawChecker(Graphics g, Color color, int slot, int y,
            boolean highlighted) {
        drawChecker(g, color, slot, y, 2);
        if (highlighted) {
            drawChecker(g, winColor, slot, y, 10);            
        }
    }
    
    /** Draw a checker at the specified place using the given margin. */
    private void drawChecker(Graphics g, Color color, 
            int slot, int y, int margin) {
        g.setColor(color); 
        int xx = slot * placeSize + margin;
        int yy = (y + 1) * placeSize + margin;
        g.fillOval(xx, yy, placeSize - margin * 2 , placeSize - margin * 2);
    }
        
    /** 
     * Return true if a given point (x, y) is inside a circle
     * specified by its center (cX, cY) and radius.
     */
    public static boolean isIn(int x, int y, int cX, int cY, int r) {
       int dx = x - cX;
       int dy = y - cY;
       return dx * dx + dy * dy <= r * r;
    }

}
