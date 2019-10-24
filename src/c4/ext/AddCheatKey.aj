 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import c4.base.BoardPanel;
import c4.base.C4Dialog;
import c4.base.ColorPlayer;

privileged aspect AddCheatKey {
	
        // Color to highlight the checkers (discs) of the possible winning row.
        private final Color cheatColorPlayer = Color.GREEN;

        // Color to highlight the checkers (discs) of the possible winning row. 
        private final Color cheatColorOpponent = Color.YELLOW;
        
        private final int numberCol = 7;
        
        private final int numberRows = 6;
        //Boolean corresponding to weather or not the cheat key is pressed
        private static boolean isCheatEnabled = true;

        pointcut highlightCheckers(BoardPanel bp, Graphics g):
        	execution (void BoardPanel.paint(Graphics)) && args(g) && this(bp);
        
        void around(BoardPanel boardPanel, Graphics g) : highlightCheckers(boardPanel, g){
                proceed(boardPanel, g);
                Color painToken = Color.BLACK; 
                ColorPlayer player = (ColorPlayer) boardPanel.board.playerAt(0, 0);
                if (isCheatEnabled) { 
                        for(int col = 0; col < numberCol; col++){
                                for(int row = 0; row < numberRows; row ++){
                                        player = (ColorPlayer) boardPanel.board.playerAt(col,row);
                                                if( player == boardPanel.board.playerAt(col,row)){
                                                painToken = cheatColorPlayer;
                                        }else{
                                                painToken = cheatColorOpponent;
                                        }if (player != null && !boardPanel.board.isGameOver()){
                                        drawVertical(row, col, boardPanel, g, painToken);
                                        drawHorizontal(row, col, boardPanel, g, painToken);
                                        diagonalBackslash(row, col, boardPanel, g, painToken);
                                        diagonalForwardslash(row, col, boardPanel, g, painToken);
                                        }
                                }
                        }
                }
                boardPanel.repaint();
        }
                void around(C4Dialog dialog) : execution(C4Dialog.new()) && this(dialog) {
                proceed(dialog);
                ActionMap actionMap = dialog.getRootPane().getActionMap();
                int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
                InputMap inputMap = dialog.getRootPane().getInputMap(condition);
                String cheat = "Hint";
                inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), cheat);
                actionMap.put(cheat, new KeyAction());
        }
        @SuppressWarnings("serial")
        private static class KeyAction extends AbstractAction {
                // Called when a cheat is requested.
                public void actionPerformed(ActionEvent event) {
                        isCheatEnabled = !isCheatEnabled;
                }
        }
        
        public void drawVertical(int row, int col, BoardPanel boardPanel, Graphics g, Color painToken){
        //if slot equals the two above it fill in all three slots
        if(row+2 < numberRows && row-1 >= 0){
                if((ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col,row+1) 
                &&(ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col,row+2)
                && (ColorPlayer) boardPanel.board.playerAt(col,row-1) == null){ 
                boardPanel.drawChecker(g, painToken, col, row, 10);
                boardPanel.drawChecker(g, painToken, col, row+1, 10);
                boardPanel.drawChecker(g, painToken, col, row+2, 10);
                }
        }
}

        public void drawHorizontal(int row, int col, BoardPanel boardPanel, Graphics g, Color painToken){
        if(col+2 < numberCol){
              //check uninterrupted 3 in a row and if right to leftmost checker is same color
              if((ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col+1,row)
              &&(ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col+2,row)
              && ((col - 1 >= 0 && ((ColorPlayer) boardPanel.board.playerAt(col-1,row) == null))
              || (col + 3 < numberCol && (ColorPlayer) boardPanel.board.playerAt(col+3,row) ==  null))){ 
              boardPanel.drawChecker(g, painToken, col, row, 10);
              boardPanel.drawChecker(g, painToken, col+1, row, 10);
              boardPanel.drawChecker(g, painToken, col+2, row, 10);
                }
                //the space next to THE LEFTMOST checker is blank
                if((ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col+2,row)
                //three right to leftmost checker is same color and within bounds
                && col + 3 < numberCol && (ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col+3,row)
                && (ColorPlayer) boardPanel.board.playerAt(col+1,row) ==  null){ 
                boardPanel.drawChecker(g, painToken, col, row, 10);
                boardPanel.drawChecker(g, painToken, col+2, row, 10);
                boardPanel.drawChecker(g, painToken, col+3, row, 10);
                }
                //two spaces away from THE LEFTMOST checker is blank
                //one right to leftmost checker  is same color
                if((ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col+1,row)
                //two right of the leftmost checker is same color
                && col + 3 < numberCol && (ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col+3,row)
                && (ColorPlayer) boardPanel.board.playerAt(col+2,row) ==  null){ 
                boardPanel.drawChecker(g, painToken, col, row, 10);
                boardPanel.drawChecker(g, painToken, col+1, row, 10);
                boardPanel.drawChecker(g, painToken, col+3, row, 10);
                }
        }
}
        
        public void diagonalBackslash(int row, int col, BoardPanel boardPanel, Graphics g, Color painToken){
        	if(col+2 < numberCol && row+2 < numberRows){
                //Uninterrupted 3 in a row
            	//one diagonally  to leftmost is same color
                if((ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col+1,row+1)
                &&(ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col+2,row+2)
                && (((col - 1 >= 0 &&  row-1 >= 0) && ((ColorPlayer) boardPanel.board.playerAt(col-1,row-1) == null))
                || ((col + 3 < numberCol && row + 3 < numberRows) && (ColorPlayer) boardPanel.board.playerAt(col+3,row+3) ==  null))){ 
                boardPanel.drawChecker(g, painToken, col, row, 10);
                boardPanel.drawChecker(g, painToken, col+1, row+1, 10);
                boardPanel.drawChecker(g, painToken, col+2, row+2, 10);
                }
                //the checker next to the leftmost is blank
                if((ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col+2,row+2)
                &&(col + 3 < numberCol && row + 3 < numberRows) && (ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col+3,row+3)
                && ((ColorPlayer) boardPanel.board.playerAt(col+1,row+1) ==  null)){ 
                boardPanel.drawChecker(g, painToken, col, row, 10);
                boardPanel.drawChecker(g, painToken, col+2, row+2, 10);
                boardPanel.drawChecker(g, painToken, col+3, row+3, 10);
                }
                //the checker two from the leftmost is blank
                if((ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col+1,row+1)
                &&(col + 3 < numberCol && row + 3 < numberRows) && (ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col+3,row+3)
                && ((ColorPlayer) boardPanel.board.playerAt(col+2,row+2) ==  null)){ 
                boardPanel.drawChecker(g, painToken, col, row, 10);
                boardPanel.drawChecker(g, painToken, col+1, row+1, 10);
                boardPanel.drawChecker(g, painToken, col+3, row+3, 10);
                }
        }
}

	public void diagonalForwardslash(int row, int col, BoardPanel boardPanel, Graphics g, Color painToken){
        if(col+2 < numberCol && row-2 >= 0){
                if((ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) 
                		boardPanel.board.playerAt(col+1,row-1)
                        &&(ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer)
                        boardPanel.board.playerAt(col+2,row-2)
                        && (((col - 1 >= 0 && row +1 < numberRows) && ((ColorPlayer)
                        boardPanel.board.playerAt(col-1,row+1) == null)) 
                        || ((col + 3 < numberCol && row - 3 > 0) && (ColorPlayer)
                            	//right side not blocked off and within bounds
                                boardPanel.board.playerAt(col+3,row-3) ==  null))){ 
                                boardPanel.drawChecker(g, painToken, col, row, 10);
                                boardPanel.drawChecker(g, painToken, col+1, row-1, 10);
                                boardPanel.drawChecker(g, painToken, col+2, row-2, 10);
                }
                //the checker next to the leftmost is blank
                if((ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer) 
                		boardPanel.board.playerAt(col+2,row-2)
                                && (col + 3 < numberCol && row - 3 >= 0) && (ColorPlayer) 
                                boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col+3,row-3)
                                && ((ColorPlayer) 
                                boardPanel.board.playerAt(col+1,row-1) ==  null)){
                                boardPanel.drawChecker(g, painToken, col, row, 10);
                                boardPanel.drawChecker(g, painToken, col+2, row-2, 10);
                                boardPanel.drawChecker(g, painToken, col+3, row-3, 10);
                }
                //the checker two from the leftmost is blank
                if((ColorPlayer) boardPanel.board.playerAt(col,row) == (ColorPlayer)
                		boardPanel.board.playerAt(col+1,row-1)
                        && (col + 3 < numberCol && row - 3 >= 0) && (ColorPlayer) 
                        boardPanel.board.playerAt(col,row) == (ColorPlayer) boardPanel.board.playerAt(col+3,row-3)
                        && ((ColorPlayer) 
                        		boardPanel.board.playerAt(col+2,row-2) ==  null)){ 
                                boardPanel.drawChecker(g, painToken, col, row, 10);
                                boardPanel.drawChecker(g, painToken, col+1, row-1, 10);
                                boardPanel.drawChecker(g, painToken, col+3, row-3, 10);
                }
               }
   }
}