import java.awt.Color;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;


public class ArrowAction implements  KeyListener {

	public static int top(JButton b) {
		return b.getBorder().getBorderInsets(b).top;
	}
	public static int left(JButton b) {
		return b.getBorder().getBorderInsets(b).left;
	}
	public static int bottom(JButton b) {
		return b.getBorder().getBorderInsets(b).bottom;
	}
	public static int right(JButton b) {
		return b.getBorder().getBorderInsets(b).right;
	}


	@Override
	public void keyPressed(KeyEvent e) {

		for (int i = 0; i < MazeGrid.grid.length; i++) {
			for (int j = 0; j < MazeGrid.grid.length; j++) {
				if (MazeGrid.grid[i][j].isFocusOwner()) {

					switch (e.getKeyCode()) {
						case (KeyEvent.VK_RIGHT) :
							if (j<MazeGrid.grid.length-1 && left(MazeGrid.grid[i][j+1]) == 0
							&& right(MazeGrid.grid[i][j]) == 0
							&& !MazeGrid.grid[i][j+1].getBackground().equals(MazeGrid.path)) {
							
							MazeGrid.grid[i][j+1].setBackground(MazeGrid.path);
							MazeGrid.grid[i][j+1].requestFocus();
							
							checkEnd(MazeGrid.grid[i][j+1]);
							} else if (j<MazeGrid.grid.length-1 && left(MazeGrid.grid[i][j+1]) == 0
							&& right(MazeGrid.grid[i][j]) == 0
							&& MazeGrid.grid[i][j+1].getBackground().equals(MazeGrid.path)) {
								
								MazeGrid.grid[i][j].setBackground(MazeGrid.back);
								MazeGrid.grid[i][j+1].requestFocus();
							}
						
						break;
						
						case (KeyEvent.VK_LEFT) :
							if (j > 0 && left(MazeGrid.grid[i][j]) == 0
									&& right(MazeGrid.grid[i][j-1]) == 0
									&& !MazeGrid.grid[i][j-1].getBackground().equals(MazeGrid.path)) {
								
							 MazeGrid.grid[i][j-1].setBackground(MazeGrid.path);
							
							 MazeGrid.grid[i][j-1].requestFocus();
							 
							 checkEnd(MazeGrid.grid[i][j-1]);
							} else if (j > 0 && left(MazeGrid.grid[i][j]) == 0
									&& right(MazeGrid.grid[i][j-1]) == 0
									&& MazeGrid.grid[i][j-1].getBackground().equals(MazeGrid.path)) {
								
								 MazeGrid.grid[i][j].setBackground(MazeGrid.back);
								 MazeGrid.grid[i][j-1].requestFocus();
							}
						break;
						case (KeyEvent.VK_UP) :
							if (i>0 && top(MazeGrid.grid[i][j]) == 0
									&& bottom(MazeGrid.grid[i-1][j]) == 0
									&& !MazeGrid.grid[i-1][j].getBackground().equals(MazeGrid.path)) {
							 MazeGrid.grid[i-1][j].setBackground(MazeGrid.path);
							 
							MazeGrid.grid[i-1][j].requestFocus();
							checkEnd(MazeGrid.grid[i-1][j]);
							} else if (i>0 && top(MazeGrid.grid[i][j]) == 0
									&& bottom(MazeGrid.grid[i-1][j]) == 0
									&& MazeGrid.grid[i-1][j].getBackground().equals(MazeGrid.path)) {
								MazeGrid.grid[i][j].setBackground(MazeGrid.back);
								 MazeGrid.grid[i-1][j].requestFocus();
							}
						break;
						case (KeyEvent.VK_DOWN) :
							if (i< MazeGrid.grid.length-1 && top(MazeGrid.grid[i+1][j]) == 0
									&& bottom(MazeGrid.grid[i][j]) == 0
									&& !MazeGrid.grid[i+1][j].getBackground().equals(MazeGrid.path)) {
							MazeGrid.grid[i+1][j].setBackground(MazeGrid.path);
							 MazeGrid.grid[i+1][j].requestFocus();
							 checkEnd(MazeGrid.grid[i+1][j]);
							} else if (i< MazeGrid.grid.length-1 && top(MazeGrid.grid[i+1][j]) == 0
									&& bottom(MazeGrid.grid[i][j]) == 0
									&& MazeGrid.grid[i+1][j].getBackground().equals(MazeGrid.path)) {
								MazeGrid.grid[i][j].setBackground(MazeGrid.back);
								 MazeGrid.grid[i+1][j].requestFocus();
							}
						break;
					}
					
				}
			}
		}
		
	}

	private void checkEnd(JButton a) {


		if (a.getText().equals("!!!")) {
			JOptionPane.showMessageDialog(null, "Congratulations, you win! \nIf you want to start" +
					" a" +
					" new game, press the button in the top left");
			/*for (int l = 0; l < MazeGrid.grid.length; l++) {
				for (int m = 0; m < MazeGrid.grid.length; m++) {
					
					MazeGrid.grid[l][m].setBackground(MazeGrid.back);
					
					MazeGrid.grid[0][0].requestFocus();
					MazeGrid.grid[0][0].setBackground(MazeGrid.path);
					
				}
			} */
		}
		
	}
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
	
		
	}


}
