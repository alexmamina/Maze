import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

public class MazeGrid extends JFrame{


	public static int[][] outline;
	
	public Block[][] maze;
	
	public static JButton[][] grid;
	
	
	
	public MazeGrid() {
		MazeGenerator.size = Integer.parseInt(JOptionPane.showInputDialog("Size of maze (must " +
				"be <64):"));
		grid = new JButton[MazeGenerator.size][MazeGenerator.size];
		setTitle("Maze");
		MazeGenerator.numWalls = 2*MazeGenerator.size*MazeGenerator.size+2*MazeGenerator.size;
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		GridLayout layout = new GridLayout(MazeGenerator.size, MazeGenerator.size);
		setLayout(layout);
		insertButtons();
		insertMenu();
		if (MazeGenerator.size < 30) setSize(500, 500);
		else setSize(850, 850);
	}


	private void insertMenu() {
		JMenuBar bar = new JMenuBar();
		JButton newgame = new JButton("New game");
		newgame.addActionListener(e->{
			MazeGrid newgrid = new MazeGrid();
			newgrid.setVisible(true);
			setVisible(false);
		});
		bar.add(newgame);
		newgame.setFocusable(false);
		setJMenuBar(bar);
	}

	private void insertButtons() {
		MazeGenerator.createWallsList();
		MazeGenerator.createListOfBlocks();

		MazeGenerator.setUpForGen();
		//ArrayList<Integer> w = MazeGenerator.dfGeneration(MazeGenerator.cells[0][0]);
		//ArrayList<Integer> w = MazeGenerator.iterative();
		//MazeGenerator.kruskalsPrep();
		//ArrayList<Integer> w = MazeGenerator.kruskal();
		ArrayList<Integer> w = MazeGenerator.prim();
		MazeGenerator.convertToRegBlocks(w);
		maze = MazeGenerator.cells;
		for (int i = 0; i < MazeGenerator.size; i++) {
			for (int j = 0; j < MazeGenerator.size; j++) {
				Block c = maze[i][j];
				System.out.println(c);
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

//--------------------------------------OLD--------------------------------------------------------
	//For older versions: create the maze from decimal values loaded from file
	private Block[][] createMaze(int[][] outline) {
		Block[][] maze = new Block[MazeGenerator.size][MazeGenerator.size];
		for (int i = 0; i < MazeGenerator.size; i++) {
			for (int j = 0; j < MazeGenerator.size; j++) {
				if (i == MazeGenerator.size-1 && j == MazeGenerator.size-1)
					maze[i][j] = new Block(outline[i][j]);
				else maze[i][j] = new Block(outline[i][j]);
			}
		}
		return maze;
	}


	//For older versions: load values from file and convert into regular blocks
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
//-------------------------------------------------------------------------------------------------





	public static void main(String[] args) {
		MazeGrid m = new MazeGrid();
		m.setVisible(true);
	}
}
