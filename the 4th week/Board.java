
import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Board {

	private int[] blocks;
	private int manhattan;
	private int hamming; 
	private int dimension;
	// private int direction ; //向哪边移动。如果本次向上移，就记成1.向右2.向左-2.向下-1.

	private void swap(int[] blocks, int src, int des) {
		int temp = blocks[src];
		blocks[src] = blocks[des];
		blocks[des] = temp;
	}

	private Board(int[] blocks){
		this.blocks = blocks;
		this.dimension = (int)Math.sqrt(this.blocks.length);
		solve();
	}
	
	public Board(int[][] blocks) { // construct a board from an n-by-n array of
									// blocks
		dimension = blocks.length;
		this.blocks = new int[dimension*dimension];
		for (int i = 0; i < dimension; i++) { // 这一步赋值让内部变得安全了！！！因为外部传入的只是个指针，可以变！！如果外边变了，里边也一定会变的！！这就泄露了！！
			for (int j = 0; j < dimension; j++) {
				this.blocks[i*dimension+j] = blocks[i][j];
			}
		}
		solve();
	}
	
	private void solve(){
		//constuct manhattan:
		int length = blocks.length;
		for (int i = 0; i < length; i++) {
			if (blocks[i] != i + 1 && blocks[i] != 0) {
				int targetI = (blocks[i] - 1) / dimension;
				int targetJ = (blocks[i] - 1) % dimension;
				manhattan += (Math.abs(targetI - i / dimension) + Math.abs(targetJ - i % dimension));
			}
		}
		
		for (int i = 0; i < length - 1; i++) {
			if (blocks[i] != i + 1) {
				hamming++;
			}
		}
		
	}
	
	// (where blocks[i][j] = block in row i, column j)
	public int dimension() { // board dimension n
		return dimension;
	}

	public int hamming() { // number of blocks out of place
		return hamming;
	}

	public int manhattan() { // sum of Manhattan distances between blocks and goal
		return manhattan;
	}

	public boolean isGoal() { // is this board the goal board?
		int length = blocks.length;
		for (int i = 0; i < length - 1; i++) {
			if (blocks[i] != i + 1) {
				return false;
			}
		}
		return true;
	}

	public Board twin() { // a board that is obtained by exchanging any pair of
							// blocks
		int length = blocks.length;
		int[] copy = blocks.clone();
		// System.arraycopy(blocks, 0, copy, 0, length*length);
		int randomI1 = StdRandom.uniform(length);
		int randomI2 = StdRandom.uniform(length);
		while (copy[randomI1] == 0) {
			randomI1 = StdRandom.uniform(length);
		}
		while (copy[randomI2] == 0
				|| randomI1 == randomI2) {
			randomI2 = StdRandom.uniform(length);
		}
		swap(copy, randomI1, randomI2);
		return new Board(copy);
	}

	public boolean equals(Object y) { // does this board equal y?

		if (y == null)
			return false;

		if (y == this)
			return true;
		
		if (y.getClass() != this.getClass()) {
			return false;
		}

		if (((Board) y).dimension() != this.dimension()) {
			return false;
		}

		for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] != ((Board) y).blocks[i]) 
				return false;
		}
		return true;
	}

	public Iterable<Board> neighbors() { // all neighboring boards
		List<Board> list = new ArrayList<Board>();
		int length = blocks.length;
		int[] copy = blocks.clone();
		for (int block = 0; block < length; block++) {
			if (blocks[block] == 0) {
				if (block - dimension >= 0) {
					swap(copy, block, block - dimension);
					int[] copy1 = copy.clone();
					list.add(new Board(copy1)); // list.add(new
												// Board(copy));的话，传递全是引用，在外边修改会修改所有。。。
					swap(copy, block, block - dimension); // 归位
				}
				if (block + dimension < length) {
					swap(copy, block, block + dimension);
					int[] copy1 = copy.clone();
					list.add(new Board(copy1));
					swap(copy, block, block + dimension);
				}
				if ((block+1) % dimension != 0) {	//如果dimension == 3,则测下标为2的二维数组元素。
					swap(copy, block, block + 1);
					int[] copy1 = copy.clone();
					list.add(new Board(copy1));
					swap(copy, block, block + 1);
				}
				if (block % dimension != 0) {	//如果dimension == 3,则测下标为0的二维数组元素。
					swap(copy, block, block - 1);
					int[] copy1 = copy.clone();
					list.add(new Board(copy1));
				}
				return list;
			}
		}
		return null;
	}

	public String toString() { // string representation of this board (in the
								// output format specified below)
		StringBuilder sb = new StringBuilder();
		int length = blocks.length;
		sb.append(dimension).append("\n");
		for (int i = 0; i < length; i++) {
			sb.append(blocks[i]).append(" ");
			if((i+1) % dimension == 0)	sb.append("\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	public static void main(String[] args) { // unit tests (not graded)

		// In in = new In("8puzzle/puzzle3x3-unsolvable.txt");
//		In in = new In("8puzzle/puzzle2x2-unsolvable1.txt");
//		int n = in.readInt();
//		int[][] blocks = new int[n][n];
//		for (int i = 0; i < n; i++)
//			for (int j = 0; j < n; j++)
//				blocks[i][j] = in.readInt();
//		Board initial = new Board(blocks);
//		StdOut.println(initial);
//		StdOut.println(initial.twin());

		 int grid[][] = {{8,1,3},{4,0,2},{7,6,5}};
		 Board board = new Board(grid);
		 StdOut.println(board);
		
		 StdOut.println(board.hamming());
		 StdOut.println(board.manhattan()+"\n");
		
		 List<Board> list = (List<Board>)board.neighbors();
		 for(Board b : list){
			 StdOut.println(b);
		 }

		// System.out.println(board.twin());

		// System.out.println("/\\");

	}

}