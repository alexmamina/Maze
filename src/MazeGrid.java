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
		setTitle("Maze");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		String[] ops = {"D: Long paths, easy (<60)", "I: Long paths, easy (any size)", "K: " +
				"Shorter paths, " +
				"harder (smaller maze)", "P: Shorter paths, harder (any size)"};
		int choice = JOptionPane.showOptionDialog(null,
				"What kind of maze would you like to play?\n" +
						"Harder options may not have an exit!\n" +
						"When choosing a maze of size > 40, wait a little after making the first " +
						"step.\n The game needs to load", "Maze options",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,null,ops,null);
		insertButtons(choice);
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

	private void insertButtons(int choice) {
		MazeGenerator.size = Integer.parseInt(JOptionPane.showInputDialog("Size of maze:"));
		GridLayout layout = new GridLayout(MazeGenerator.size, MazeGenerator.size);
		setLayout(layout);
		grid = new JButton[MazeGenerator.size][MazeGenerator.size];
		MazeGenerator.numWalls = 2*MazeGenerator.size*MazeGenerator.size+2*MazeGenerator.size;
		MazeGenerator.createWallsList();
		MazeGenerator.createListOfBlocks();
		ArrayList<Integer> w = new ArrayList<>();
		switch (choice) {
			case 0:
				MazeGenerator.setUpForGen();
				w = MazeGenerator.dfGeneration(MazeGenerator.cells[0][0]);
				System.out.println("DFS end");
				break;
			case 1:
				MazeGenerator.setUpForGen();
				w = MazeGenerator.iterative();
				System.out.println("ITER end");
				break;
			case 2:
				MazeGenerator.kruskalsPrep();
				w = MazeGenerator.kruskal();
				System.out.println("KRU end");
				break;
			case 3:
				MazeGenerator.setUpForGen();
				w = MazeGenerator.prim();
				System.out.println("PRIM end");
				break;
			default:
				JOptionPane.showMessageDialog(null, "Oops");
		}
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
