import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

public class MazeGenerator {


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
        Collections.shuffle(x);
        while (x.get(0) != 1 && x.get(numWalls-1) != numWalls) Collections.shuffle(x);
        for (int i = 0; i < numWalls; i++) {
            walls[i] = x.get(i);
        }
        System.out.println("WALLS: "+x);
        return walls;
    }

    //Creates an array of the top walls of all cells to create Blocks easier later (Block contains
    // 4 numbers representing the walls it has, e.g. in a 16-cell maze the first cell is
    //Block(1,5,10,6)
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
        return cells;
    }
    public static ArrayList<Integer> remWalls;
    public static HashMap<Block, Boolean> visited;
    //Set all cells as unvisited
    public static void setUpForGen() {
        remWalls = toAList(walls);
        visited = new HashMap<>();
        for (Block block : listofcells) {
            visited.put(block, false);
        }
    }

    //Checks if all neighbours are visited - returns False if some unvisited
    private static boolean checkUnvisited(ArrayList<Block> neighbours, HashMap<Block, Boolean> visited) {
        boolean result = true;
        for (Block n : neighbours) {
            result = result && visited.get(n);
        }
        return result;
    }

    //Convert array to arraylist - library function ddi not work
    private static ArrayList<Integer> toAList(int[] a) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            res.add(a[i]);
        }

        return res;
    }

    //Convert blocks that have numbers of walls in them to "binary" (2=1, 1=0) blocks for easier
    //border painting
    public static void convertToRegBlocks(ArrayList<Integer> remWalls) {
        //convert 1221 to numbers, then it will be the mazegrid.outline
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Block b = cells[r][c];
                if (remWalls.contains(b.getT())) b.setT(2); else b.setT(1);
                if (remWalls.contains(b.getL())) b.setL(2); else b.setL(1);
                if (remWalls.contains(b.getB())) b.setB(2); else b.setB(1);
                if (remWalls.contains(b.getR())) b.setR(2); else b.setR(1);
            }
        }
    }

    /* --------------DEPTH-FIRST SEARCH GENERATION ALGORITHM ---------------------------------------
    1. Given a current cell as a parameter,
    2. Mark the current cell as visited
    3. While the current cell has any unvisited neighbour cells
         1. Choose one of the unvisited neighbours
         2. Remove the wall between the current cell and the chosen cell
         3. Invoke the routine recursively for a chosen cell

     */

    public static ArrayList<Integer> dfGeneration(Block b) {
        visited.replace(b, false, true);
        System.out.println("Rem walls "+remWalls);
        System.out.println("Block "+b);
        ArrayList<Block> neighbours = b.getAllNeighbours(cells);
        System.out.println("Neighbours "+neighbours);
        while (!checkUnvisited(neighbours, visited)) {
            //Gets next unvisited random neighbour from the list
            Block n = b.getRandomNeighbour(neighbours);
            while (visited.get(n)) {
                n = b.getRandomNeighbour(neighbours);
            }
            System.out.println("Neighbour "+n);
            remWalls.remove(remWalls.indexOf(b.getCommonWall(n)));
            //Remove it from the list of neighbours for easier while loop later
            neighbours.remove(n);
            dfGeneration(n);
        }
        //Dead end
        System.out.println("Done");
        return remWalls;
    }
//-------------------------------DFS END-----------------------------------------------------------


//-------------------ITERATIVE IMPLEMENTATION ALGORITHM--------------------------------------------
   /* 1. Choose the initial cell, mark it as visited and push it to the stack
2. While the stack is not empty
    1. Pop a cell from the stack and make it a current cell
    2. If the current cell has any neighbours which have not been visited
        1. Push the current cell to the stack
        2. Choose one of the unvisited neighbours
        3. Remove the wall between the current cell and the chosen cell
        4. Mark the chosen cell as visited and push it to the stack */


   public static ArrayList<Integer> iterative() {
       Stack stack = new Stack();
       Block init = cells[0][0];
       visited.replace(init, false, true);
       stack.push(init);
       while (!stack.isEmpty()) {
           Block curr = (Block) stack.pop();
           System.out.println("Rem walls " + remWalls);
           System.out.println("Block " + curr);
           ArrayList<Block> neighbours = curr.getAllNeighbours(cells);
           System.out.println("Neighbours " + neighbours);
           if (!checkUnvisited(neighbours, visited)) {
               stack.push(curr);
               //Gets next unvisited random neighbour from the list
               Block n = curr.getRandomNeighbour(neighbours);
               while (visited.get(n)) {
                   n = curr.getRandomNeighbour(neighbours);
               }
               System.out.println("Neighbour " + n);
               remWalls.remove(remWalls.indexOf(curr.getCommonWall(n)));
               //Remove it from the list of neighbours for easier while loop later
               neighbours.remove(n);
               visited.replace(n, false, true);
               stack.push(n);
           }
           System.out.println("Done");
       }
       System.out.println("Finished loop");
        return remWalls;
   }
//------------------------------------ITERATIVE END------------------------------------------------
    //Main for testing algorithms before integrating them into the UI
    public static void main(String[] args) {
        size = 4;
        numWalls = 2*size*size+2*size;
        createWallsList();
        Block[][] test = createListOfBlocks();

        setUpForGen();
        //ArrayList<Integer> w = dfGeneration(test[0][0]);
        ArrayList<Integer> w = iterative();
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
