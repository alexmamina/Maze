import java.awt.Color;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;


public class ArrowAction implements  KeyListener {


	@Override
	public void keyPressed(KeyEvent e) {
	
		for (int i = 0; i < MazeGrid.grid.length; i++) {
			for (int j = 0; j < MazeGrid.grid.length; j++) {
				if (MazeGrid.grid[i][j].isFocusOwner()) {

					switch (e.getKeyCode()) {
						case (KeyEvent.VK_RIGHT) :
							if (j<MazeGrid.grid.length-1 && ChangeStepAction.left(MazeGrid.grid[i][j+1]) == 0
							&& ChangeStepAction.right(MazeGrid.grid[i][j]) == 0
							&& !MazeGrid.grid[i][j+1].getBackground().equals(new Color(174, 142, 255))) {
							
							MazeGrid.grid[i][j+1].setBackground(new Color(174, 142, 255));	
							MazeGrid.grid[i][j+1].requestFocus();
							
							checkEnd(MazeGrid.grid[i][j+1]);
							} else if (j<MazeGrid.grid.length-1 && ChangeStepAction.left(MazeGrid.grid[i][j+1]) == 0
							&& ChangeStepAction.right(MazeGrid.grid[i][j]) == 0
							&& MazeGrid.grid[i][j+1].getBackground().equals(new Color(174, 142, 255))) {
								
								MazeGrid.grid[i][j].setBackground(new Color(251, 255, 190));
								MazeGrid.grid[i][j+1].requestFocus();
							}
						
						break;
						
						case (KeyEvent.VK_LEFT) :
							if (j > 0 && ChangeStepAction.left(MazeGrid.grid[i][j]) == 0
									&& ChangeStepAction.right(MazeGrid.grid[i][j-1]) == 0
									&& !MazeGrid.grid[i][j-1].getBackground().equals(new Color(174, 142, 255))) {
								
							 MazeGrid.grid[i][j-1].setBackground(new Color(174, 142, 255));
							
							 MazeGrid.grid[i][j-1].requestFocus();
							 
							 checkEnd(MazeGrid.grid[i][j-1]);
							} else if (j > 0 && ChangeStepAction.left(MazeGrid.grid[i][j]) == 0
									&& ChangeStepAction.right(MazeGrid.grid[i][j-1]) == 0
									&& MazeGrid.grid[i][j-1].getBackground().equals(new Color(174, 142, 255))) {
								
								 MazeGrid.grid[i][j].setBackground(new Color(251, 255, 190));
								 MazeGrid.grid[i][j-1].requestFocus();
							}
						break;
						case (KeyEvent.VK_UP) :
							if (i>0 && ChangeStepAction.top(MazeGrid.grid[i][j]) == 0
									&& ChangeStepAction.bottom(MazeGrid.grid[i-1][j]) == 0
									&& !MazeGrid.grid[i-1][j].getBackground().equals(new Color(174, 142, 255))) {
							 MazeGrid.grid[i-1][j].setBackground(new Color(174, 142, 255));
							 
							MazeGrid.grid[i-1][j].requestFocus();
							checkEnd(MazeGrid.grid[i-1][j]);
							} else if (i>0 && ChangeStepAction.top(MazeGrid.grid[i][j]) == 0
									&& ChangeStepAction.bottom(MazeGrid.grid[i-1][j]) == 0
									&& MazeGrid.grid[i-1][j].getBackground().equals(new Color(174, 142, 255))) {
								MazeGrid.grid[i][j].setBackground(new Color(251, 255, 190));
								 MazeGrid.grid[i-1][j].requestFocus();
							}
						break;
						case (KeyEvent.VK_DOWN) :
							if (i< MazeGrid.grid.length-1 && ChangeStepAction.top(MazeGrid.grid[i+1][j]) == 0
									&& ChangeStepAction.bottom(MazeGrid.grid[i][j]) == 0
									&& !MazeGrid.grid[i+1][j].getBackground().equals(new Color(174, 142, 255))) {
							MazeGrid.grid[i+1][j].setBackground(new Color(174, 142, 255));
							 MazeGrid.grid[i+1][j].requestFocus();
							 checkEnd(MazeGrid.grid[i+1][j]);
							} else if (i< MazeGrid.grid.length-1 && ChangeStepAction.top(MazeGrid.grid[i+1][j]) == 0
									&& ChangeStepAction.bottom(MazeGrid.grid[i][j]) == 0
									&& MazeGrid.grid[i+1][j].getBackground().equals(new Color(174, 142, 255))) {
								MazeGrid.grid[i][j].setBackground(new Color(251, 255, 190));
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
			JOptionPane.showMessageDialog(null, "Congratulations, you win!");
			for (int l = 0; l < MazeGrid.grid.length; l++) {
				for (int m = 0; m < MazeGrid.grid.length; m++) {
					
					MazeGrid.grid[l][m].setBackground(new Color(251, 255, 190));
					
					MazeGrid.grid[0][0].requestFocus();
					MazeGrid.grid[0][0].setBackground(new Color(174, 142, 255));
					
					}
				}
			}
		
	}
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
	
		
	}


}
