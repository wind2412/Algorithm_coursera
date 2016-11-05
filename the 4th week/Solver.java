

import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

	private GameTreeNode finalAnswer;

	private boolean isSolvable;

	private MinPQ<GameTreeNode> minpq1 = new MinPQ<>(
			new Comparator<GameTreeNode>() {

				@Override
				public int compare(GameTreeNode o1, GameTreeNode o2) {
					int t = o1.board.manhattan() - o2.board.manhattan() + o1.moves - o2.moves;	//加上moves漂亮！直接就把moves也算做参数了。之前没想到。
					return (t != 0)
							? t
							: o1.board.hamming() - o2.board.hamming();
				}

			});

	private MinPQ<GameTreeNode> minpq2 = new MinPQ<>(
			new Comparator<GameTreeNode>() {

				@Override
				public int compare(GameTreeNode o1, GameTreeNode o2) {
					int t = o1.board.manhattan() - o2.board.manhattan() + o1.moves - o2.moves;
					return (t != 0)
							? t
							: o1.board.hamming() - o2.board.hamming();
				}

			});

	private class GameTreeNode {

		private Board board;
		private GameTreeNode parent;
		private int moves;

		public GameTreeNode(Board board, GameTreeNode parent, int moves) {
			this.board = board;
			this.parent = parent;
			this.moves = moves;
		}

		private boolean isEqualToGrandFather() {
			if (this.parent != null && this.parent.parent != null)
				return board.equals(this.parent.parent.board);
			return false;
		}

	}

	public Solver(Board initial) { // find a solution to the initial board
									// (using the A* algorithm)
		boolean isInitBoardSolvable = false;
		minpq1.insert(new GameTreeNode(initial, null, 0));
		minpq2.insert(new GameTreeNode(initial.twin(), null, 0));
		GameTreeNode currentNode1 = null;
		GameTreeNode currentNode2 = null;
		while (true) {
			if (!(currentNode1 = minpq1.delMin()).board.isGoal()) {
				for (Board b : currentNode1.board.neighbors()) {
					if (!currentNode1.isEqualToGrandFather()) {
						minpq1.insert(new GameTreeNode(b, currentNode1,
								currentNode1.moves + 1));
						// System.out.println(b);
						// System.out.println(currentNode1.moves+1);
					}
				}
			} else {
				isInitBoardSolvable = true;
				break;
			}
			if (!(currentNode2 = minpq2.delMin()).board.isGoal()) {
				for (Board b : currentNode2.board.neighbors()) {
					if (!currentNode2.isEqualToGrandFather())
						minpq2.insert(new GameTreeNode(b, currentNode2,
								currentNode2.moves + 1));
				}
			} else {
				isInitBoardSolvable = false;
				break;
			}
		}
		isSolvable = isInitBoardSolvable;
		if(isSolvable)
			finalAnswer = currentNode1;
	}

	public boolean isSolvable() { // is the initial board solvable?
		return isSolvable;
	}

	public int moves() { // min number of moves to solve initial board; -1 if
							// unsolvable
		return (finalAnswer == null) ? -1 : finalAnswer.moves;
	}

	public Iterable<Board> solution() { // sequence of boards in a shortest
										// solution; null if unsolvable
		if (!isSolvable)
			return null;
		Stack<Board> stack = new Stack<>();
		GameTreeNode temp = finalAnswer;
		while(temp != null) {
			stack.push(temp.board);
			temp = temp.parent;
		}
		return stack;
	}

	public static void main(String[] args) { // solve a slider puzzle (given
												// below)

		In in = new In("8puzzle/puzzle04.txt");
		// In in = new In("8puzzle/puzzle04.txt");
		int n = in.readInt();
		int[][] blocks = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output

		int m = 0;
		for (Board board : solver.solution()) {
			StdOut.println(board);
			m++;
		}
		StdOut.println("length of solution() = " + m);
		StdOut.println("Minimum number of moves = " + solver.moves());

	}

}
