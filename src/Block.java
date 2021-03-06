import java.util.ArrayList;
import java.util.Random;

public class Block {
	private int t, l, b, r;
	
	public Block(int t, int l, int b, int r) {
		this.t = t;
		this.l = l;
		this.b = b;
		this.r = r;
	}
	
	public int getT() { return t; };
	public int getL() { return l; };
	public int getB() { return b; };
	public int getR() { return r; };

	public void setT(int t) { this.t = t; };
	public void setL(int l) { this.l = l; };
	public void setB(int b) { this.b = b; };
	public void setR(int r) { this.r = r; };
	

	public String toString() {
		return getT()+" "+getL()+" "+getB()+" "+getR();
	}

	public static Block[] getTwoNeighbours(int wall, Block[][] cells) {
		Block[] result = new Block[2];
		int counter = 0;
		for (int r = 0; r < MazeGenerator.size; r++) {
			for (int c = 0; c < MazeGenerator.size; c++) {
				Block b = cells[r][c];

					if (b.getT() == wall || b.getL() == wall
							|| b.getB() == wall || b.getR() == wall) {
						result[counter] = b;
						counter++;
					}
			}
		}
		return result;
	}

	public ArrayList<Block> getAllNeighbours(Block[][] cells) {
		ArrayList<Block> result = new ArrayList<>();
		for (int r = 0; r < MazeGenerator.size; r++) {
			for (int c = 0; c < MazeGenerator.size; c++) {
				Block b = cells[r][c];
				if (!this.equals(b)) {
					if (b.getT() == this.b || b.getL() == this.r
							|| b.getB() == this.t || b.getR() == this.l) result.add(b);
				}
			}
		}
		return result;
	}

	public Block getRandomNeighbour(ArrayList<Block> allneighbours) {
		int random = (new Random().nextInt(allneighbours.size()));
		return allneighbours.get(random);
	}

	public int getCommonWall(Block a) {
		if (a.getT() == this.b) return this.b;
		else if (a.getL() == this.r) return this.r;
		else if (a.getB() == this.t) return this.t;
		else if (a.getR() == this.l) return this.l;
		else return 10000;
	}

	//Returns list of coordinates of blocks that don't have a wall
	//with the current block
	public  ArrayList<int[]> getPassage(int len, int i, int j) {
		ArrayList<int[]> result = new ArrayList<>();
		boolean[] passages = this.whatPassagesExist(len, i, j);
		//cells is already 2 or 1
		int[] r;
		if (passages[0]) {
			r = new int[2];
			r[0] = i-1;
			r[1] = j;
			result.add(r);
		}
		if (passages[1]) {
			r = new int[2];
			r[0] = i;
			r[1] = j-1;
			result.add(r);
		}
		if (passages[2]) {
			r = new int[2];
			r[0] = i+1;
			r[1] = j;
			result.add(r);
		}
		if (passages[3]) {
			r = new int[2];
			r[0] = i;
			r[1] = j+1;
			result.add(r);
		}

		return result;
	}

	public boolean[] whatPassagesExist(int len, int i, int j) {
		boolean[] topLeftBottomRight = new boolean[4];
		topLeftBottomRight[0] = this.getT() == 1 && i > 0;
		topLeftBottomRight[1] = this.getL() == 1 && j > 0;
		topLeftBottomRight[2] = this.getB() == 1 && i < len - 1;
		topLeftBottomRight[3] = this.getR() == 1 && j < len - 1;
		return topLeftBottomRight;
	}

	public int[] getAllWalls() {
		int[] ws = new int[4];
		ws[0] = this.t;
		ws[1] = this.l;
		ws[2] = this.b;
		ws[3] = this.r;
		return ws;
	}

	private boolean equals(Block b) {
		return this.t == b.getT() && this.l == b.getL() && this.b == b.getB() && this.r == b.getR();
	}


	//OLD VERSION -------------------------------------------------------------------
	//Used with reading numbers from file
	private int[] createTable() {
		int[] cells = new int[16];
		//Types of cells: 2 is wall, 1 - no wall. Top-left-bottom-right
		//Can't use 0 and 1 due to computer thinking it's binary
		cells[0] = 2222;
		cells[1] = 1222;
		cells[2] = 2122;
		cells[3] = 2212;
		cells[4] = 2221;
		cells[5] = 1122;
		cells[6] = 2112;
		cells[7] = 2211;
		cells[8] = 1221;
		cells[9] = 2111;
		cells[10] = 1211;
		cells[11] = 1121;
		cells[12] = 1112;
		cells[13] = 1111;
		cells[14] = 2121;
		cells[15] = 1212;

		return cells;
	}

	private int[] values = createTable();


	public Block(int k) {
		int a = conversion(k);
		t = a / 1000;
		l = (a / 100) % 10;
		b = (a / 10) % 10;
		r = a % 10;
	}

	private int conversion(int n) {
		return values[n];
	}
}
