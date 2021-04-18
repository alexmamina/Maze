import java.awt.Color;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;


public class ArrowAction implements  KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {

		JButton b = (JButton) e.getSource();
		String text = b.getText();
		String[] coords = text.split(" ");
		int i, j;
		if (!text.equals("!!!")) {
			i = Integer.parseInt(coords[0]);
			j = Integer.parseInt(coords[1]);
		} else {
			i = MazeGrid.grid.length-1;
			j = MazeGrid.grid.length-1;
		}
		Block curr = MazeGrid.maze[i][j];
		//Check in what direction it's possible to go
		boolean[] passages = curr.whatPassagesExist(MazeGrid.grid.length, i, j);

		switch (e.getKeyCode()) {
			case (KeyEvent.VK_RIGHT) :
				if (passages[3] && !MazeGrid.grid[i][j+1].getBackground().equals(MazeGrid.path)) {
					//If can go right and the right neighbour is not part of the path, color it
					MazeGrid.grid[i][j+1].setBackground(MazeGrid.path);
					MazeGrid.grid[i][j+1].setForeground(MazeGrid.path);
					MazeGrid.grid[i][j+1].requestFocus();
					checkEnd(MazeGrid.grid[i][j+1]);

				} else if (passages[3] && MazeGrid.grid[i][j+1].getBackground().equals(MazeGrid.path)) {
					//Can go right, and right neighbour is already part of path (e.g. going
					// backwards, need to clear the color)
					MazeGrid.grid[i][j].setBackground(MazeGrid.back);
					MazeGrid.grid[i][j].setForeground(MazeGrid.back);
					MazeGrid.grid[i][j+1].requestFocus();
				}
			break;
						
			case (KeyEvent.VK_LEFT) :
				if (passages[1] && !MazeGrid.grid[i][j-1].getBackground().equals(MazeGrid.path)) {
					//Going left and forwards, color as path
					MazeGrid.grid[i][j-1].setBackground(MazeGrid.path);
					MazeGrid.grid[i][j-1].setForeground(MazeGrid.path);
					MazeGrid.grid[i][j-1].requestFocus();
					checkEnd(MazeGrid.grid[i][j-1]);
				} else if (passages[1] && MazeGrid.grid[i][j-1].getBackground().equals(MazeGrid.path)) {
					//Left and backwards, color back to 'unvisited'
					MazeGrid.grid[i][j].setBackground(MazeGrid.back);
					MazeGrid.grid[i][j].setForeground(MazeGrid.back);
					MazeGrid.grid[i][j-1].requestFocus();
				}
			break;

			case (KeyEvent.VK_UP) :
				if (passages[0] && !MazeGrid.grid[i-1][j].getBackground().equals(MazeGrid.path)) {
					//Up and forwards
					MazeGrid.grid[i-1][j].setBackground(MazeGrid.path);
					MazeGrid.grid[i-1][j].setForeground(MazeGrid.path);
					MazeGrid.grid[i-1][j].requestFocus();
					checkEnd(MazeGrid.grid[i-1][j]);
				} else if (passages[0] && MazeGrid.grid[i-1][j].getBackground().equals(MazeGrid.path)) {
					//Up and backwards
					MazeGrid.grid[i][j].setBackground(MazeGrid.back);
					MazeGrid.grid[i][j].setForeground(MazeGrid.back);
					MazeGrid.grid[i-1][j].requestFocus();
				}
			break;
			case (KeyEvent.VK_DOWN) :
				if (passages[2] && !MazeGrid.grid[i+1][j].getBackground().equals(MazeGrid.path)) {
					//Down and forwards
					MazeGrid.grid[i+1][j].setBackground(MazeGrid.path);
					MazeGrid.grid[i+1][j].setForeground(MazeGrid.path);
					MazeGrid.grid[i+1][j].requestFocus();
					checkEnd(MazeGrid.grid[i+1][j]);
				} else if (passages[2] && MazeGrid.grid[i+1][j].getBackground().equals(MazeGrid.path)) {
					//Down and backwards
					MazeGrid.grid[i][j].setBackground(MazeGrid.back);
					MazeGrid.grid[i][j].setForeground(MazeGrid.back);
					MazeGrid.grid[i+1][j].requestFocus();
				}
			break;
		}

		
	}

	private void checkEnd(JButton a) {
		if (a.getText().equals("!!!")) {
			JOptionPane.showMessageDialog(null,
					"Congratulations, you win! \nIf you want to start" +
					" a" +
					" new game, press the button in the top left");
		}
	}
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
}
