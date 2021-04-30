import java.util.*;

public class MazeGenerator {


    public static int size;
    public static int numWalls;
    private static int[] walls;
    public static Block[][] cells;
    private static Block[] listofcells;
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
        while ((x.get(0) != 1) && (x.get(numWalls-1) != numWalls)) Collections.shuffle(x);
        for (int i = 0; i < numWalls; i++) {
            walls[i] = x.get(i);
        }
        return walls;
    }

    //Creates an array of the top walls of all cells to create Blocks easier later (Block contains
    // 4 numbers representing the walls it has, e.g. in a 16-cell maze the first cell is
    //Block(1,5,10,6)
    //So this returns a list [1,2,3,4,10,11,12,13,...] as those are all the top walls of each cell
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
    //This basically creates a grid so that each cell is a block with its associated walls
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
    private static ArrayList<Integer> remWalls;
    private static HashMap<Block, Boolean> visited;
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

    //Convert array to arraylist - library function did not work
    private static ArrayList<Integer> toAList(int[] a) {
        ArrayList<Integer> res = new ArrayList<>();
        for (int value : a) {
            res.add(value);
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
        ArrayList<Block> neighbours = b.getAllNeighbours(cells);
        while (!checkUnvisited(neighbours, visited)) {
            //Gets next unvisited random neighbour from the list
            Block n = b.getRandomNeighbour(neighbours);
            while (visited.get(n)) {
                n = b.getRandomNeighbour(neighbours);
            }
            remWalls.remove(remWalls.indexOf(b.getCommonWall(n)));
            //Remove it from the list of neighbours for easier while loop later
            neighbours.remove(n);
            dfGeneration(n);
        }
        //Dead end
        return remWalls;
    }
//-------------------------------DFS END-----------------------------------------------------------

/*-------------------ITERATIVE IMPLEMENTATION ALGORITHM--------------------------------------------
    1. Choose the initial cell, mark it as visited and push it to the stack
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
           ArrayList<Block> neighbours = curr.getAllNeighbours(cells);
           if (!checkUnvisited(neighbours, visited)) {
               stack.push(curr);
               //Gets next unvisited random neighbour from the list
               Block n = curr.getRandomNeighbour(neighbours);
               while (visited.get(n)) {
                   n = curr.getRandomNeighbour(neighbours);
               }
               remWalls.remove(remWalls.indexOf(curr.getCommonWall(n)));
               //Remove it from the list of neighbours for easier while loop later
               neighbours.remove(n);
               visited.replace(n, false, true);
               stack.push(n);
           }
       }
        return remWalls;
   }
//------------------------------------ITERATIVE END------------------------------------------------

    /*---------------------RANDOMIZED KRUSKAL'S ALGORITHM----------------------------------------------
        1. Create a list of all walls, and create a set for each cell, containing just that one cell.
        2. For each wall, in some random order:
            1. If the cells divided by this wall belong to distinct sets:
                1. Remove the current wall.
                2. Join the sets of the formerly divided cells.
     */
    private static ArrayList<HashSet<Block>> sets = new ArrayList<>();

    //Creates a list of sets, each containing one cell
    public static void kruskalsPrep() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                HashSet<Block> set = new HashSet<>();
                set.add(cells[r][c]);
                sets.add(set);
            }
        }
        remWalls = toAList(walls);
    }

    public static ArrayList<Integer> kruskal() {
        for (int w : walls) {
            ArrayList<HashSet<Block>> neighbSets = new ArrayList<>();
            Block[] neighbours = Block.getTwoNeighbours(w, cells);
            //If it's a border cell, skip to next wall
            if (neighbours[1] == null) continue;
            boolean distinct = true;
            for (HashSet<Block> s : sets) {
                if (s.contains(neighbours[0]) && s.contains(neighbours[1])) {
                    distinct = false;
                    break;
                } else if (s.contains(neighbours[0]) || s.contains(neighbours[1])) {
                    //Adds sets where those neighbouring blocks are, to combine them together later
                    neighbSets.add(s);
                }
            }
            if (distinct) {
                remWalls.remove((Integer) w);
                //Combine the neighbouring sets into one (union)
                HashSet<Block> union = new HashSet<>();
                union.addAll(neighbSets.get(0));
                union.addAll(neighbSets.get(1));
                sets.add(union);
                //Remove the separate occurences of those sets as they have been unionized
                sets.removeAll(neighbSets);
            }
        }
        return remWalls;
    }
    //------------------------------------KRUSKAL'S END------------------------------------------------

    /*-----------------------RANDOMIZED PRIM'S ALGORITHM-----------------------------------------------
        1. Start with a grid full of walls.
        2. Pick a cell, mark it as part of the maze. Add the walls of the cell to the wall list.
        3. While there are walls in the list:
            1. Pick a random wall from the list.
            If only one of the two cells that the wall divides is visited, then:
                1. Make the wall a passage and mark the unvisited cell as part of the maze.
                2. Add the neighboring walls of the cell to the wall list.
            2. Remove the wall from the list.
     */

    public static ArrayList<Integer> prim() {
    ArrayList<Integer> walllist = new ArrayList<>();
    Block b = cells[0][0];
    visited.replace(b,false,true);
    ArrayList<Integer> allwalls = toAList(b.getAllWalls());
    //Add the block's walls to the list of walls
    walllist.addAll(allwalls);
    while (!walllist.isEmpty()) {
        //Get a random wall from the list
        int w = walllist.get((new Random().nextInt(walllist.size())));
        Block[] ns = Block.getTwoNeighbours(w, cells);
        //If a border cell
        if (ns[0] == null || ns[1] == null) {
            walllist.remove((Object) w);
            continue;
        }
        //If only one of the neighbours is visited (one or the other)
        else if (visited.get(ns[0]) && !visited.get(ns[1])) {
            remWalls.remove((Object) w);
            visited.replace(ns[1],false,true);
            //Add its walls to the list
            walllist.addAll(toAList(ns[1].getAllWalls()));

        } else if (visited.get(ns[1]) && !visited.get(ns[0])){
            remWalls.remove((Object) w);
            visited.replace(ns[0],false,true);
            walllist.addAll(toAList(ns[0].getAllWalls()));
        }
        walllist.remove((Object) w);
    }

    return remWalls;
}

//------------------------------------PRIM'S END---------------------------------------------------


}
