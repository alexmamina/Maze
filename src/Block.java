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

	public Block getNeighbour(int wall, Block[][] cells) {
		for (int r = 0; r <
				MazeGenerator.size; r++) {
			for (int c = 0; c < MazeGenerator.size; c++) {
				Block b = cells[r][c];
				if (!this.equals(b)) {
					if (b.getT() == wall || b.getL() == wall
							|| b.getB() == wall || b.getR() == wall) return b;
				}
			}
		}
		return null;
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


	public boolean equals(Block b) {
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

	int[] values = createTable();


	public Block(int k) {
		int a = conversion(k);
		t = a / 1000;
		l = (a / 100) % 10;
		b = (a / 10) % 10;
		r = a % 10;
	}

	public int conversion(int n) {
		return values[n];
	}
}
