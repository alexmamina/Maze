import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;

import javax.swing.*;



public class ChangeStepAction implements KeyListener{

	
	public JButton b;
	

	/*public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			b = (JButton) e.getSource();
		}

		if (b.getBackground().equals(Color.white) )  {

			for (int i = 0; i < MazeGrid.grid.length; i++) {
				for (int j = 0; j < MazeGrid.grid.length; j++) {
					if (MazeGrid.grid[i][j].equals(b)) {
						if (i > 0 && top(b) == 0 && bottom(MazeGrid.grid[i-1][j]) == 0 && MazeGrid.grid[i-1][j].getBackground().equals(Color.yellow)
						 || i < MazeGrid.grid.length-1 && bottom(b) == 0 && top(MazeGrid.grid[i+1][j]) == 0 && MazeGrid.grid[i+1][j].getBackground().equals(Color.yellow)
						 || j > 0 && left(b) == 0 && right(MazeGrid.grid[i][j-1]) == 0 && MazeGrid.grid[i][j-1].getBackground().equals(Color.yellow)
						 || j < MazeGrid.grid.length-1 && right(b) == 0 && left(MazeGrid.grid[i][j+1]) == 0 && MazeGrid.grid[i][j+1].getBackground().equals(Color.yellow)) {
							b.setBackground(Color.YELLOW);
							b.setForeground(Color.yellow);
							break;
						}
					
					}
				}
			}
		}
		else {
			b.setBackground(Color.WHITE);
			b.setForeground(Color.white);
			
		}
		
		if (b.getText().equals("true")) {
			JOptionPane.showMessageDialog(null, "Congratulations, you win!");
			for (int i = 0; i < MazeGrid.grid.length; i++) {
				for (int j = 0; j < MazeGrid.grid.length; j++) {
					if (!(i == 0 && j == 0)) {
					MazeGrid.grid[i][j].setBackground(Color.white);
					MazeGrid.grid[i][j].setForeground(Color.white);
					}
				}
			}
		}
	}
	*/
	
	
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
				if (MazeGrid.grid[i][j].isFocusable()) {
					switch(e.getKeyCode()) {
					
					case (KeyEvent.VK_UP) :
						if (i > 0) {
							if (MazeGrid.grid[i-1][j].getBackground().equals(Color.WHITE) &&
									bottom(MazeGrid.grid[i-1][j]) == 0 && top(MazeGrid.grid[i][j]) == 0) {
								MazeGrid.grid[i][j].setBackground(Color.YELLOW);
								MazeGrid.grid[i-1][j].setFocusable(true);
							}
						}
					case (KeyEvent.VK_DOWN) :
						if (i < MazeGrid.grid.length - 1) {
							if (MazeGrid.grid[i+1][j].getBackground().equals(Color.white) &&
									top(MazeGrid.grid[i+1][j]) == 0 && bottom(MazeGrid.grid[i][j]) == 0) {
								MazeGrid.grid[i][j].setBackground(Color.YELLOW);
								MazeGrid.grid[i+1][j].setFocusable(true);
							}
						}
					case (KeyEvent.VK_LEFT) :
						if (j > 0) {
							if (MazeGrid.grid[i][j-1].getBackground().equals(Color.white) &&
									left(MazeGrid.grid[i][j])  == 0 && right(MazeGrid.grid[i][j-1]) == 0) {
								MazeGrid.grid[i][j].setBackground(Color.YELLOW);
								MazeGrid.grid[i][j-1].setFocusable(true);
							}
						}
					case (KeyEvent.VK_RIGHT) :
						if (j < MazeGrid.grid.length - 1) {
							if (MazeGrid.grid[i][j+1].getBackground().equals(Color.white) &&
									left(MazeGrid.grid[i][j+1])  == 0 && right(MazeGrid.grid[i][j]) == 0) {
								MazeGrid.grid[i][j].setBackground(Color.YELLOW);
								MazeGrid.grid[i][j+1].setFocusable(true);
							}
						}
					}
				}
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}
}
