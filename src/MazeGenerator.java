import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MazeGenerator {

//SIZE 4 WORKS, OTHERS DON'T

    public static int size;
    public static int numWalls;
    public static int[] walls;
    public static Block[][] cells;
    public static Block[] listofcells;
    //Creates a randomised list of walls (represented by numbers, counted from top left to top
    // right, then vertical walls, then horizontal etc). Starts from 1 and ends with the last wall
    public static int[] createWallsList() {
        numWalls = 2*size*size+2*size;
        walls = new int[numWalls];
        ArrayList<Integer> x = new ArrayList<>();
        for (int i = 1; i <= numWalls; i++) {
            x.add(i);
        }
        while (x.get(0) != 1 && x.get(numWalls-1) != numWalls) Collections.shuffle(x);
        for (int i = 0; i < numWalls; i++) {
            walls[i] = x.get(i);
        }
        return walls;
    }

    //Creates an array of the top walls of all cells to create Blocks easier later (Block contains
    // 4 numbers representing the walls it has
    private static int[] createTopWallsForCells() {
        int counter = -1;
        int[] t = new int[size*size];
        for (int i = 1; i <= (size*size); i++) {
            if (i % size == 1) counter++;
            t[i-1] = i+counter*(size+1);
        }
        return t;
    }

    //Creates an array of blocks where each contains the numbers of its walls
    public static Block[][] createListOfBlocks() {
        int[] t = createTopWallsForCells();
        cells = new Block[size][size];
        listofcells = new Block[size*size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                cells[r][c] = new Block(t[size*r+c], t[size*r+c]+size, t[size*r+c]+2*size+1,
                        t[size*r+c]+size+1);
                listofcells[size*r+c] = cells[r][c];
            }
        }
        cells[size-1][size-1].setEnd(true);
        listofcells[size-1].setEnd(true);
        return cells;
    }

    /*
    1. Given a current cell as a parameter,
2. Mark the current cell as visited
3. While the current cell has any unvisited neighbour cells
    1. Choose one of the unvisited neighbours
    2. Remove the wall between the current cell and the chosen cell
    3. Invoke the routine recursively for a chosen cell

     */
    public static ArrayList<Integer> remWalls;
    public static HashMap<Block, Boolean> visited;

    public static void setUpForDFG() {
        remWalls = toAList(walls);
        visited = new HashMap<>();
        for (Block block : listofcells) {
            visited.put(block, false);
        }
    }
    public static ArrayList<Integer> dfGeneration(Block b) {
       visited.replace(b, false, true);
        System.out.println("Rem walls "+remWalls);
        System.out.println("Block "+b);
       ArrayList<Block> neighbours = b.getAllNeighbours(cells);
        System.out.println("Neighbours "+neighbours);
       //Remove neighbour from list when visited -- will not work with next neighbour
       while (!checkUnvisited(neighbours, visited)) {
           //Check unvisited
           Block n = b.getRandomNeighbour(neighbours);
           while (visited.get(n)) {
               n = b.getRandomNeighbour(neighbours);
           }
           System.out.println("Neighbour "+n);
           remWalls.remove(remWalls.indexOf(b.getCommonWall(n)));
           neighbours.remove(n);
           dfGeneration(n);
       }
        System.out.println("Done");
        return remWalls;
    }

    private static boolean checkUnvisited(ArrayList<Block> neighbours, HashMap<Block, Boolean> visited) {
        boolean result = true;
        for (Block n : neighbours) {
            result = result && visited.get(n);
        }
        return result;
    }

    private static ArrayList<Integer> toAList(int[] a) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            res.add(a[i]);
        }

        return res;
    }

    public static void convertToRegBlocks(ArrayList<Integer> remWalls) {
        //convert 1221 to numbers, then it will be the mazegrid.outline
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Block b = cells[r][c];
                if (remWalls.contains(b.getT())) b.setT(2); else b.setT(1);
                if (remWalls.contains(b.getL())) b.setL(2); else b.setL(1);
                if (remWalls.contains(b.getB())) b.setB(2); else b.setB(1);
                if (remWalls.contains(b.getR())) b.setR(2); else b.setR(1);
                System.out.println(r+" "+c+" "+b);
            }
        }
    }


    public static void main(String[] args) {
       // size = 4;
       // numWalls = 2*size*size+2*size;
        createWallsList();
        Block[][] test = createListOfBlocks();

        setUpForDFG();
        ArrayList<Integer> w = dfGeneration(test[0][0]);
        for (int i : w) {
            System.out.println(i);
        }
        convertToRegBlocks(w);
         for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                 System.out.println(test[r][c]);
            }
         }
    }
}
