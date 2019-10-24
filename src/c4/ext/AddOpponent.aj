

package c4.ext;
import java.awt.Color;
import c4.base.ColorPlayer;
import c4.base.C4Dialog;
import c4.base.BoardPanel;


public privileged aspect AddOpponent {

	private BoardPanel C4Dialog.boardPanel;
	
	ColorPlayer Blue = new ColorPlayer("Blue", Color.BLUE);
	ColorPlayer Green = new ColorPlayer("Green", Color.GREEN);
	after(C4Dialog dialog): this(dialog) && execution(void C4Dialog.makeMove(..)) {
		if (dialog.player.name().equals("Blue")) {
			dialog.player = Green;
			dialog.boardPanel.setDropColor(dialog.player.color());
			dialog.showMessage(dialog.player.name() + " is next");

		} else if (dialog.player.name().equals("Green")) {

			dialog.player = Blue;
			dialog.boardPanel.setDropColor(dialog.player.color());
			dialog.showMessage(dialog.player.name() + " is next");

		}

	}
	after(C4Dialog dialog) returning (BoardPanel panel):
		this(dialog) && call(BoardPanel.new(..)){
		dialog.boardPanel= panel;
		
	}

}
