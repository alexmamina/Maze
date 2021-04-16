import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.*;

public class MazeGrid extends JFrame{


	public static int[][] outline;
	
	public static Block[][] maze;
	
	public static JButton[][] grid;
	
	public static Color back = new Color(199,255,205);
    public static Color path = new Color(51,165,82);


    public MazeGrid() {
		setTitle("Maze");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		//Get the level of difficulty
		String[] ops = {"D: Long paths, easy (<60)", "I: Long paths, easy (any size)", "K: " +
				"Shorter paths, " +
				"harder (smaller maze)", "P: Shorter paths, harder (any size)"};

        JOptionPane op = new JOptionPane(
                "When choosing size > 40, wait a little after making the first step");
        op.setOptions(ops);
        //Set the options in a column, not in one long row
        ((Container)op.getComponent(1)).setLayout(new GridLayout(4, 1));
        JDialog dlg = op.createDialog("What kind of maze would you like to play?");
        dlg.setVisible(true);
        String opt = (String) op.getValue();
        //Find index of that option in the array of options
		int choice = Arrays.binarySearch(ops, opt);
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
			dispose();
		});
		bar.add(newgame);
		JButton clear = new JButton("Clear");
		clear.addActionListener(e->{
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid.length; j++) {
					grid[i][j].setBackground(back);
				}
			}
			grid[0][0].setBackground(path);
			grid[0][0].requestFocus();
		});
		clear.setFocusable(false);
		bar.add(clear);
		newgame.setFocusable(false);
        JMenu color = addColorChange();
        bar.add(color);
        //Hint-solve using wall follower
        JButton solve = new JButton("Hint");
        solve.addActionListener(e->{
        	JOptionPane.showMessageDialog(null, "This will " +
					"show some cells on the correct path. \n To reach the end, follow all of " +
							"those cells.",
					"Hint",JOptionPane.INFORMATION_MESSAGE);
        	MazeSolver.wallFollowerToHint(grid, maze);
		});
		solve.setFocusable(false);
		bar.add(solve);
		JButton solve2 = new JButton("Solve bfs");
		solve2.addActionListener(e->{
			JOptionPane.showMessageDialog(null, "This will" +
							" show you the complete solution for this maze, \nafter which you " +
							"will " +
							"only be able to start a new game.",
					"Solution",JOptionPane.INFORMATION_MESSAGE);
			//MazeSolver.recursive(grid, maze);
			//MazeSolver.deadEnd(grid, maze);
			long start = System.nanoTime();
			MazeSolver.shortest(grid, maze);
			System.out.println("BFS done. "+ (System.nanoTime()-start)/1000);
		});
		solve2.setFocusable(false);
		bar.add(solve2);
		JButton solve3 = new JButton("Solve end");
		solve3.addActionListener(e->{
			//MazeSolver.recursive(grid, maze);
			long start = System.nanoTime();

			MazeSolver.deadEnd(grid, maze);
			System.out.println("DEF done. "+ (System.nanoTime()-start)/1000);

			//MazeSolver.shortest(grid, maze);
		});
		solve3.setFocusable(false);
		bar.add(solve3);
		JButton solve4 = new JButton("Solve rec");
		solve4.addActionListener(e->{
			long start = System.nanoTime();

			MazeSolver.recursive(grid, maze);
			System.out.println("Rec done. "+ (System.nanoTime()-start)/1000);

			//MazeSolver.deadEnd(grid, maze);
			//MazeSolver.shortest(grid, maze);
		});
		solve4.setFocusable(false);
		bar.add(solve4);
		setJMenuBar(bar);
	}
    private JMenu addColorChange() {
        JMenu color = new JMenu("Change color");
        JMenuItem opt1 = new JMenuItem("Blue & White");
        JMenuItem opt2 = new JMenuItem("Blue");
        JMenuItem opt3 = new JMenuItem("Green");
        JMenuItem opt4 = new JMenuItem("Purple & Yellow");
        opt1.addActionListener(e->{

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid.length; j++) {
                    if (grid[i][j].getBackground().equals(back))
                        grid[i][j].setBackground(Color.WHITE);
                    else if(grid[i][j].getBackground().equals(path))
                    	grid[i][j].setBackground(new Color(58,76,148));
                }
            }
            path = new Color(58,76,148);
            back = Color.white;
        });

        opt2.addActionListener(e->{
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid.length; j++) {
                    if (grid[i][j].getBackground().equals(back))
                        grid[i][j].setBackground(new Color(200,240,255));
					else if(grid[i][j].getBackground().equals(path))
						grid[i][j].setBackground(new Color(148,185,225));
                }
            }
            path = new Color(148,185,225);
            back = new Color(200,240,255);
        });

        opt3.addActionListener(e->{
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid.length; j++) {
                    if (grid[i][j].getBackground().equals(back))
                        grid[i][j].setBackground(new Color(199,255,205));
					else if(grid[i][j].getBackground().equals(path))
						grid[i][j].setBackground(new Color(51,165,82));
                }
            }
            back = new Color(199,255,205);
            path = new Color(51,165,82);
        });

        opt4.addActionListener(e->{
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid.length; j++) {
                    if (grid[i][j].getBackground().equals(back))
                        grid[i][j].setBackground(new Color(251, 255, 190));
					else if(grid[i][j].getBackground().equals(path))
						grid[i][j].setBackground(new Color(174,142,255));
                }
            }
            back = new Color(251, 255, 190);
            path = new Color(174, 142, 255);
        });

        color.add(opt1);
        color.add(opt2);
        color.add(opt3);
        color.add(opt4);
        return color;
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
				System.out.println("Depth First Search");
				break;
			case 1:
				MazeGenerator.setUpForGen();
				w = MazeGenerator.iterative();
				System.out.println("Iterative");
				break;
			case 2:
				MazeGenerator.kruskalsPrep();
				w = MazeGenerator.kruskal();
				System.out.println("Kruskal");
				break;
			case 3:
				MazeGenerator.setUpForGen();
				w = MazeGenerator.prim();
				System.out.println("Prim");
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
					b.setBackground(path);
					
				}
				else {
					b.setBackground(back);
					
					
				}
				b.setOpaque(true);
				if (i == MazeGenerator.size - 1 && j == MazeGenerator.size - 1) { b.setText("!!!");
				b.setForeground(Color.black);
				}

				ArrowAction lol = new ArrowAction();
				if (ArrowAction.top(b) > 0 && ArrowAction.left(b) > 0 &&
						ArrowAction.bottom(b) > 0 && ArrowAction.right(b) > 0) {
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
