

package c4.ext;
import c4.base.BoardPanel;
import java.awt.*;
import java.awt.event.*;

public privileged aspect PressDisc {
	private int BoardPanel.click=-1;
	private int BoardPanel.motion=-1;

	pointcut pressDisc(BoardPanel b):
		execution(BoardPanel.new(..)) && this(b);

	after(BoardPanel b):pressDisc(b){
		b.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(!b.board.isGameOver()) {
					b.click = b.locateSlot(e.getX(), e.getY());
					b.repaint();
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if(!b.board.isGameOver()) {
					b.click=-1;
					b.repaint();
				}
			}
		});
	}

	pointcut drawD (BoardPanel b, Graphics g):
		execution(void BoardPanel.drawDroppableCheckers(Graphics))&& this(b) && args(g);

	after(BoardPanel b, Graphics g) : drawD(b,g){
		if(b.click >= 0) {
			b.drawChecker(g, b.dropColor, b.click, -1, 0);
			b.drawChecker(g, Color.BLACK, b.click, -1, 6);

		}

	}
}