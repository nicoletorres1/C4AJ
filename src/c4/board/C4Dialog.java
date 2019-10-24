package c4.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import c4.model.Board;
 
/** The main game class including the UI and control. */
@SuppressWarnings("serial")
public class C4Dialog extends JDialog {
	
    private final static Dimension DIMENSION = new Dimension(265, 340);
    
    /** The game board. */
    private Board board;

    /** The player. */
    private ColorPlayer player;
  
    /** To start a new game. */
    private final JButton newButton = new JButton("New");

    /** Message bar to display various messages. */
    private JLabel msgBar = new JLabel();
    
    public C4Dialog() {
        super((JFrame) null, "Adrian Gomez R & Stephanie Medina");
        setSize(DIMENSION);
        
        board = new Board();
        player = new ColorPlayer("Blue", Color.BLUE);
        configureUI();
        
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }
    
    /** Configure UI. */
    private void configureUI() {
        setLayout(new BorderLayout());
        add(makeControlPanel(), BorderLayout.NORTH);
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));
        panel.setLayout(new GridLayout(1,1));
        panel.add(makeBoardPanel(board));
        add(panel, BorderLayout.CENTER);
    }
    
    /* Create a control panel consisting of a new button and
     * a message bar. */
    private JPanel makeControlPanel() {
        JPanel content = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.setBorder(BorderFactory.createEmptyBorder(5,15,0,0));
        buttons.add(newButton);
        newButton.setFocusPainted(false);
        newButton.addActionListener(this::newButtonClicked);
        content.add(buttons, BorderLayout.NORTH);
        msgBar.setText(player.name() + "' turn.");
        msgBar.setBorder(BorderFactory.createEmptyBorder(5,20,0,0));
        content.add(msgBar, BorderLayout.SOUTH);
        return content;
    }
    
    /** Create a special panel to display the given board. */
    private BoardPanel makeBoardPanel(Board board) {
    	BoardPanel panel = new BoardPanel(board);
    	panel.setDropColor(player.color());
    	panel.setClickListener(this::makeMove);
        return panel;
    }
        
    /** Show the given string on the message bar. */
    private void showMessage(String msg) {
        msgBar.setText(msg);
    }
    
    /** To be called when the new button is clicked. If the board is full,
     * start a new game; otherwise, prompt the user for a confirmation 
     * and then proceed accordingly. */
    private void newButtonClicked(ActionEvent event) {
    	if (board.isFull() || JOptionPane.showConfirmDialog(C4Dialog.this, "Play a new game?", 
    			"Connect Four", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
    		startNewGame();
    	}
    }    

    /** Start a new game. */
    private void startNewGame() {
        board.clear();
        showMessage(player.name() + "' turn.");
        repaint();
    }
    
    /** Drop a checker into the specified slot. */
    private void makeMove(int slot) {
        board.dropInSlot(slot, player);
        repaint();
    }    
    
    public static void main(String[] args) {
        new C4Dialog();
    }
}
