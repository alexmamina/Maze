import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class MazeSolver {

    //wall follower. Much better on smaller, longer paths
    /*
    Start following passages, and whenever you reach a junction always turn right (or left).
    Equivalent to a human solving a Maze by putting their hand on the right (or left) wall and
    leaving it there as they walk through. If you like you can mark what cells you've visited,
    and what cells you've visited twice, where at the end you can retrace the solution by
    following those cells visited once
     */
    public static void wallFollowerToHint(JButton[][] grid, Block[][] maze)  {
        //TODO look at this with new whatpassagesexist fn
        int l = maze.length;
        int[][] visitedTimes = new int[l][l];
        //Init the array with zeros
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < l; j++) {
                visitedTimes[i][j]= 0;
            }
        }
        int x = 0;
        int y = 0;
        Block curr = maze[y][x];
        int[] curr_coord = {y, x};
        int[] prev = null;
        visitedTimes[y][x]++;
        //While we're not at the end
        while (x != l-1 || y != l-1) {
            //Get possible passages from current point
            ArrayList<int[]> passages = curr.getPassage(l, y, x);
            if (passages.size() == 1) {
                //From dead end
                int[] coords = passages.get(0);
                prev = curr_coord;
                visitedTimes[y][x]++;
                curr = maze[coords[0]][coords[1]];
                x = coords[1];
                y = coords[0];
                visitedTimes[y][x]++;
                curr_coord = new int[]{y, x};

            } else if (passages.size() == 2) {
                //Always go in the direction that's visited the least
                //back and forward
                int[] oneWay = passages.get(0);
                int[] anotherWay = passages.get(1);
                if (visitedTimes[oneWay[0]][oneWay[1]] < visitedTimes[anotherWay[0]][anotherWay[1]]) {
                    prev = curr_coord;

                    curr = maze[oneWay[0]][oneWay[1]];
                    x = oneWay[1];
                    y = oneWay[0];
                    visitedTimes[y][x]++;

                    curr_coord = new int[]{y, x};

                } else {
                    prev = curr_coord;
                    curr = maze[anotherWay[0]][anotherWay[1]];
                    x = anotherWay[1];
                    y = anotherWay[0];
                    visitedTimes[y][x]++;

                    curr_coord = new int[]{y, x};

                }
            } else {
                //3 or 4 possible ways. Get rightmost one. In order: top right bottom left
                //For more explanation see Notes

                if (prev[0] < curr_coord[0]) {
                    //From top. First in passages will then be the backward way.
                    //Get the second (which will be to go right, straight, left)
                    prev = curr_coord;
                    curr = maze[passages.get(1)[0]][passages.get(1)[1]];
                    x = passages.get(1)[1];
                    y = passages.get(1)[0];
                    visitedTimes[y][x]++;
                    curr_coord = new int[]{y, x};
                } else if (prev[1] < curr_coord[1]) {
                    //From left. Top does or does not exist. if !top then 2 else 3
                    if (passages.get(0)[0] == prev[0] && passages.get(0)[1] == prev[1]) {
                        //No top
                        prev = curr_coord;
                        //Go to bottom
                        curr = maze[passages.get(1)[0]][passages.get(1)[1]];
                        x = passages.get(1)[1];
                        y = passages.get(1)[0];
                        visitedTimes[y][x]++;
                        curr_coord = new int[]{y, x};
                    }
                    else {
                        //Top
                        prev = curr_coord;
                        //Go to bottom or right
                        curr = maze[passages.get(2)[0]][passages.get(2)[1]];
                        x = passages.get(2)[1];
                        y = passages.get(2)[0];
                        visitedTimes[y][x]++;
                        curr_coord = new int[]{y, x};
                    }

                } else if (prev[0] > curr_coord[0]) {
                    //From bottom. Passages can have TLBR, TLB, LBR, TBR
                    int[] lastPassage = passages.get(passages.size()-1);
                    if (lastPassage[0] == prev[0] && lastPassage[1] == prev[1]) {
                        //No way right (last is bottom), go top or left.
                        prev = curr_coord;
                        curr = maze[passages.get(0)[0]][passages.get(0)[1]];
                        x = passages.get(0)[1];
                        y = passages.get(0)[0];
                        visitedTimes[y][x]++;
                        curr_coord = new int[]{y, x};
                    }
                    else {
                        //Way right
                        prev = curr_coord;
                        curr = maze[lastPassage[0]][lastPassage[1]];
                        x = lastPassage[1];
                        y = lastPassage[0];
                        visitedTimes[y][x]++;
                        curr_coord = new int[]{y, x};
                    }

                } else {
                    //From right. Can be  TLBR, LBR, TBR, TLR. Go first available
                    prev = curr_coord;
                    curr = maze[passages.get(0)[0]][passages.get(0)[1]];
                    x = passages.get(0)[1];
                    y = passages.get(0)[0];
                    visitedTimes[y][x]++;
                    curr_coord = new int[]{y, x};
                }


            }


        }
        Random rand = new Random();
        for (int r = 0; r < l; r++) {
            for (int c = 0; c < l; c++) {
                if (visitedTimes[r][c] == 1) {
                    if (rand.nextInt() % maze.length % 4 == 0) {
                        //Color random cells on the path to the end
                        grid[r][c].setBackground(new Color(250,150,255));
                        grid[r][c].setForeground(new Color(250,150,255));

                    }
                }

            }

        }

    }
//TODO add "waiting for soln" while searching

    public static void recursive(JButton[][] grid, Block[][] maze) {
        int len = maze.length;
        boolean[][] visited = new boolean[len][len];
        boolean[][] correct = new boolean[len][len];
        //Init the array with zeros
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                visited[i][j]= false;
                correct[i][j] = false;
            }
        }
        int x = 0;
        int y = 0;
        Block curr = maze[y][x];
        System.out.println("Starting recursion");
        if (recSolve(curr, y, x, maze, visited, correct)) {
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze.length; j++) {
                    if (correct[i][j] || i == maze.length-1 && j == maze.length-1) {
                        grid[i][j].setBackground(MazeGrid.path);
                        grid[i][j].setForeground(MazeGrid.path);

                    }
                }
            }
        }
        System.out.println("Done with recursion");
    }

    private static boolean recSolve(Block curr, int y, int x, Block[][] maze, boolean[][] visited,
                                 boolean[][] correct) {
        if (visited[y][x]) return false;
        if (y == maze.length-1 && x == maze.length-1) {
            visited[y][x] = true;
            return true;
        }
        visited[y][x] = true;
        ArrayList<int[]> passages = curr.getPassage(maze.length, y, x);
        if (passages.size() == 1 && x != 0 && y != 0) return false;
        else {
            for (int[] p : passages) {
                if (!visited[p[0]][p[1]]) {
                    curr = maze[p[0]][p[1]];
                    if (recSolve(curr, p[0], p[1], maze, visited, correct)) {
                        correct[y][x] = true;
                        return true;
                    }
                }
            }

        }
        return false;
    }


    /*
    Just scan the Maze, and fill in each dead end, filling in the passage backwards
    from the block until you reach a junction. That includes filling in passages that
    become parts of dead ends once other dead ends are removed
     */
    public static void deadEnd(JButton[][] grid, Block[][] maze) {
        ArrayList<Block> deadends = new ArrayList<>();
        ArrayList<int[]> endindices = new ArrayList<>();
        boolean[][] correct = new boolean[maze.length][maze.length];
        //Get a list of dead ends (one passage, not start). Add to list. Also init correct
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze.length; j++) {
                correct[i][j] = true;
                Block b = maze[i][j];
                ArrayList<int[]> passages = b.getPassage(maze.length, i, j);
                //Start and end don't count as dead ends
                if (passages.size() == 1 && !(i == 0 && j == 0) && !(i == maze.length-1
                                                            && j == maze.length-1)) {
                    deadends.add(b);
                    endindices.add(new int[]{i, j});
                }
            }
        }
        //For each, fill in color until the first junction (>2 passages)
        //AND if the junction leads to 2 or more dead ends, fill that in as well.
        for (int k = 0; k < deadends.size(); k++) {
            Block b = deadends.get(k);
            int[] indices = endindices.get(k);
            ArrayList<int[]> passages = b.getPassage(maze.length, indices[0], indices[1]);
            int validPassages = 1;
            while (validPassages < 2 && !(indices[0] == maze.length-1 && indices[1] == maze.length-1) &&
                                                !(indices[0] == 0 && indices[1] == 0)) {
                //fill in false path
                correct[indices[0]][indices[1]] = false;
                //get neighbour that's not visited
                for (int[] p : passages) {
                    //this checks that it's not going backwards, so only goes through 1 iter.
                    if (correct[p[0]][p[1]]) {
                        Block next = maze[p[0]][p[1]];
                        indices = p;
                        passages = next.getPassage(maze.length, indices[0], indices[1]);
                        validPassages = passages.size();
                        //if a passage leads to a dead path, decrease num of available passages
                        for (int[] newp : passages) {
                            if (!correct[newp[0]][newp[1]]) {
                                validPassages--;
                            }
                        }
                        break;
                    }
                }
            }
        }
        //Color path
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze.length; j++) {
                if (correct[i][j]) {
                    grid[i][j].setBackground(MazeGrid.path);
                    grid[i][j].setForeground(MazeGrid.path);

                }
            }
        }
    }


    //Breadth-first search
    /*
    procedure BFS(G, root) is
 2      let Q be a queue
 3      label root as discovered
 4      Q.enqueue(root)
 5      while Q is not empty do
 6          v := Q.dequeue()
 7          if v is the goal then
 8              return v
 9          for all edges from v to w in G.adjacentEdges(v) do
10              if w is not labeled as discovered then
11                  label w as discovered
12                  Q.enqueue(w)
     */
    public static void shortest(JButton[][] grid, Block[][] maze) {
        Queue<int[]> queue = new LinkedBlockingQueue<>();
        boolean[][] visited = new boolean[maze.length][maze.length];
        //Key is child, value is parent indices
        HashMap<int[], int[]> parents = new HashMap<>();
        int[] indices = new int[]{0,0};
        int[] end = new int[]{maze.length, maze.length};
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze.length; j++) {
                visited[i][j] = false;
            }
        }
        visited[indices[0]][indices[1]] = true;
        queue.add(indices);
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            Block b = maze[curr[0]][curr[1]];
            if (curr[0] == maze.length-1 && curr[1] == maze.length-1) {
                getPath(curr, parents, grid);
            }
            ArrayList<int[]> passages = b.getPassage(maze.length, curr[0], curr[1]);
            for (int[] p : passages) {
                if (!visited[p[0]][p[1]]) {
                    parents.put(p, curr);
                    visited[p[0]][p[1]] = true;
                    queue.add(p);
                }
            }
        }
    }
    //Get parents of children and add to the final path, reconstructing backwards
    private static void getPath(int[] curr, HashMap<int[],int[]> parents, JButton[][] grid) {
        ArrayList<int[]> path = new ArrayList<>();
        path.add(curr);
        int[] parent = new int[2];
        while (!(curr[0] == 0 && curr[1] == 0)) {
            for (int[] k : parents.keySet()) {
                if (k[0] == curr[0] && k[1] == curr[1]) {
                    parent = parents.get(k);
                    parents.remove(k);
                    break;
                }
            }
            path.add(parent);
            curr = parent;
        }
        for (int[] c : path) {
            grid[c[0]][c[1]].setBackground(MazeGrid.path);
            grid[c[0]][c[1]].setForeground(MazeGrid.path);

        }
    }

}
