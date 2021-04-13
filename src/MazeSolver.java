import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
        System.out.println(y + " and "+x);
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
                System.out.println(y + " and " + x);
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
                    System.out.println(y + " and " + x);
                    x = oneWay[1];
                    y = oneWay[0];
                    visitedTimes[y][x]++;

                    curr_coord = new int[]{y, x};

                } else {
                    prev = curr_coord;
                    curr = maze[anotherWay[0]][anotherWay[1]];
                    System.out.println(y + " and " + x);
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
                    System.out.println(y + " and " + x);
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
                        System.out.println(y + " and " + x);
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
                        System.out.println(y + " and " + x);
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
                        System.out.println(y + " and " + x);
                        x = passages.get(0)[1];
                        y = passages.get(0)[0];
                        visitedTimes[y][x]++;
                        curr_coord = new int[]{y, x};
                    }
                    else {
                        //Way right
                        prev = curr_coord;
                        curr = maze[lastPassage[0]][lastPassage[1]];
                        System.out.println(y + " and " + x);
                        x = lastPassage[1];
                        y = lastPassage[0];
                        visitedTimes[y][x]++;
                        curr_coord = new int[]{y, x};
                    }

                } else {
                    //From right. Can be  TLBR, LBR, TBR, TLR. Go first available
                    prev = curr_coord;
                    curr = maze[passages.get(0)[0]][passages.get(0)[1]];
                    System.out.println(y + " and " + x);
                    x = passages.get(0)[1];
                    y = passages.get(0)[0];
                    visitedTimes[y][x]++;
                    curr_coord = new int[]{y, x};
                }


            }
            //Color all the area that the algorithm visited for a 'hint'
            grid[y][x].setBackground(Color.LIGHT_GRAY);

        }
        for (int r = 0; r < l; r++) {
            for (int c = 0; c < l; c++) {
                System.out.print(visitedTimes[r][c]);
                if (visitedTimes[r][c] == 1) {
                    //Color where the path definitely is
                    grid[r][c].setBackground(Color.BLUE);
                    grid[0][0].requestFocus();
                }
            }
            System.out.println();
        }

    }

    public static void main(String[] args) {
        wallFollowerToHint(MazeGrid.grid,MazeGrid.maze);
    }
}
