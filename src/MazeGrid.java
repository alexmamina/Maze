import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;

public class MazeGrid extends JFrame{


	public static int[][] outline3 = {{6,7,6},{15,15,15},{8,5,8}};
	//public static int[][] outline = {{14,6,7,14,6},{3,15,15,0,15},{10,5,10,14,12},{15,4,12,3,15},{8,14,5,8,11}};
	
	//public static int[][] outline = generator(15);
	public static int[][] outline;
	
	public Block[][] maze;
	
	public static JButton[][] grid;
	
	
	
	public MazeGrid() {

		setTitle("Maze");
		MazeGenerator.numWalls = 2*MazeGenerator.size*MazeGenerator.size+2*MazeGenerator.size;
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		GridLayout layout = new GridLayout(MazeGenerator.size, MazeGenerator.size);
		setLayout(layout);
		insertButtons();
		if (MazeGenerator.size < 30) setSize(500, 500);
		else setSize(850, 850);
	}
	private void insertButtons() {
		//maze = createMaze(outline);
		MazeGenerator.createWallsList();
		MazeGenerator.createListOfBlocks();

		MazeGenerator.setUpForDFG();
		ArrayList<Integer> w = MazeGenerator.dfGeneration(MazeGenerator.cells[0][0]);
		MazeGenerator.convertToRegBlocks(w);
		maze = MazeGenerator.cells;
		for (int i = 0; i < MazeGenerator.size; i++) {
			for (int j = 0; j < MazeGenerator.size; j++) {
				Block c = maze[i][j];
			
				JButton b = new JButton();
				
				b.setBorder(BorderFactory.createMatteBorder(c.getT()-1, c.getL()-1, c.getB()-1, c.getR()-1, Color.BLACK));
				
				if (i == 0 && j == 0) { 
					b.setBackground(new Color(174, 142, 255));
					
				}
				else {
					b.setBackground(new Color(251, 255, 190));
					
					
				}
				b.setOpaque(true);
				if (i == MazeGenerator.size - 1 && j == MazeGenerator.size - 1) { b.setText("!!!");
				b.setForeground(Color.black);
				}
	
				ArrowAction lol = new ArrowAction();
				if (ChangeStepAction.top(b) > 0 && ChangeStepAction.left(b) > 0 && 
						ChangeStepAction.bottom(b) > 0 && ChangeStepAction.right(b) > 0) {
					b.addMouseListener(new MouseListener() {
						
						@Override
						public void mouseReleased(MouseEvent e) {
							
							
						}
						
						@Override
						public void mousePressed(MouseEvent e) {
							
							
						}
						
						@Override
						public void mouseExited(MouseEvent e) {
							
							
						}
						
						@Override
						public void mouseEntered(MouseEvent e) {
							
							
						}
						
						@Override
						public void mouseClicked(MouseEvent e) {
							
							if (e.getClickCount() == 2) {
								JOptionPane.showMessageDialog(null, "You have found the secret chamber!");
							}
						}
					});
				}
				
				b.addKeyListener(lol);
		
	
				add(b);
				grid[i][j] = b; 
			}
		}
		
	}

	private Block[][] createMaze(int[][] outline) {
		Block[][] maze = new Block[MazeGenerator.size][MazeGenerator.size];
		for (int i = 0; i < MazeGenerator.size; i++) {
			for (int j = 0; j < MazeGenerator.size; j++) {
				if (i == MazeGenerator.size-1 && j == MazeGenerator.size-1) maze[i][j] = new Block(outline[i][j], true); 
				else maze[i][j] = new Block(outline[i][j], false); 
			}
		}
		return maze;
	}
	

	private static int[][] generator(int k) {
		//Must return a valid maze. Must have walls, entrance and exit in the same place and a valid route.
		int[][] result = new int[k][k];
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result.length; j++) {
				Random random = new Random();
				result[i][j] = random.nextInt((15-0) + 1) + 0;
			}
		}
		
		return result;
	}

	
	public static int[][] loadOutlineFromFile(String path) {
		Path name = Paths.get(path);
		int length = Integer.parseInt(path.substring(41, 43));
		int[][] cells = new int[length][length];
		ArrayList<Integer> c = new ArrayList<>();
		try {
			Scanner in = new Scanner(name);
			while (in.hasNextInt()) {
				c.add(in.nextInt());
				
			}
			in.close();
			for(int row = 0; row < length; row++) {
	            for(int column = 0; column < length; column++) {
	                cells[row][column] = c.get(length*row+column);
	            }
	        }
		} catch (IOException e) {
            throw new UncheckedIOException(e);
        }
		return cells;
	}
	public static void main(String[] args) {
//SIZE AND NUM WALSS
		//String path = "/Users/alexmamina/eclipse-workspace/Maze/" + JOptionPane.showInputDialog(
			//	"Name of file:");
		//outline = loadOutlineFromFile(path);
		MazeGenerator.size = Integer.parseInt(JOptionPane.showInputDialog("Size of maze (must " +
				"be <64):"));
		grid = new JButton[MazeGenerator.size][MazeGenerator.size];
		MazeGrid m = new MazeGrid();
		m.setVisible(true);
	}
}
