

package c4.ext;
import c4.base.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public privileged aspect EndGame {
	pointcut checkWin(C4Dialog cd): execution(* C4Dialog.makeMove(int)) &&target(cd);
	pointcut blockPlayers(C4Dialog cd):execution(* C4Dialog.makeMove(int)) && this(cd);
	pointcut gameOver(C4Dialog cd): execution(* makeMove(int)) && target(cd);
	pointcut newGame(C4Dialog cd): execution(* C4Dialog.newButtonClicked(*)) && target(cd); 

	private void C4Dialog.showWin(ColorPlayer opponent){
		player= opponent;
		JOptionPane.showMessageDialog(new JFrame(), (player.name()+ " Player is the winner!!"), "GAME OVER",JOptionPane.ERROR_MESSAGE);
	}
	private void C4Dialog.showDraw(){
		showMessage("OH Draw!");
		JOptionPane.showMessageDialog(new JFrame(), "OH Draw!", "GAME OVER",JOptionPane.ERROR_MESSAGE);
	}
	
	 after(C4Dialog cd): checkWin(cd){
		if(cd.board.isWonBy(cd.player)) {	
			 cd.showWin(cd.player);
			 }
		if(cd.board.isFull()) {
				 cd.showDraw();
				 }
	 }

	 void around(C4Dialog cd): newGame(cd){
		 cd.startNewGame();
		 	}

	void around(C4Dialog cd): blockPlayers(cd){
		if(cd.board.isGameOver()){
			cd.showWin(cd.player);
		}else{
			proceed(cd);
		}
	}
}
