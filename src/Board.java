import java.util.Random;
import java.util.Vector;

/**
 * Class used to initialize a board configuration. It contains 
 * all the static elements of the board and a <code>State</code>
 * object which holds the initial configuration of the dynamic elements.
 * 
 * @author Erik
 *
 */
public final class Board {
	public static final byte FLOOR = (1 << 0);
	public static final byte WALL  = (1 << 1);
	public static final byte GOAL  = (1 << 2);
	
	/**
	 * Matrix of static elements on the board.
	 */
	private static byte[][] board;
	/**
	 * Random values used to calculate hash functions.
	 */
	public static long[][] zValues;
	/**
	 * Number of board rows.
	 */
	public static byte rows = 0;
	/**
	 * Number of row columns.
	 */
	public static byte cols = 0;
	/**
	 * Vector of goal positions.
	 */
	public static Vector<BoardPosition> goalPositions = new Vector<BoardPosition>();
	/**
	 * The initial state of the board.
	 */
	public static State state;
	
	/**
	 * Constructs a board using an vector of
	 * strings supplied from the course server.
	 * 
	 * @param lines Lines from the server
	 */
	public Board(Vector<String> lines) {
		rows = (byte) lines.size();
		cols = (byte) lines.get(0).length();
		
		/*
		 * Pad the sides so we don't have to worry about edge effects.
		 */
		board   = new byte[rows+2][cols+2];
		zValues = new long[rows+2][cols+2];
		
		BoardPosition playerPosition = null;
		Vector<BoardPosition> boxPositions = new Vector<BoardPosition>();

		Random random = new Random();
		for (byte i=1; i<=rows; i++) {
			String line = lines.get(i-1);
			for (byte j=1; j<=cols; j++) {
				char character = line.charAt(j-1);

				zValues[i][j] = random.nextLong();
				board[i][j] = FLOOR;
				switch (character) {
				case '#':	// wall
					board[i][j] = WALL;
					break;
				case '@':	// player
					playerPosition = new BoardPosition(i, j);
					break;
				case '+':	// player on goal
					board[i][j] = GOAL;
					goalPositions.add(new BoardPosition(i, j));
					playerPosition = new BoardPosition(i, j);
					break;
				case '$':	// box
					boxPositions.add(new BoardPosition(i, j));
					break;
				case '*':	// box on goal
					board[i][j] = GOAL;
					goalPositions.add(new BoardPosition(i, j));
					boxPositions.add(new BoardPosition(i, j));
					break;
				case '.':	// goal
					board[i][j] = GOAL;
					goalPositions.add(new BoardPosition(i, j));
					break;
				case ' ':	// floor
					board[i][j] = FLOOR;
					break;
				}
			}
		}
		
		state = new State(this, playerPosition, boxPositions);
	}

	public final boolean floorAt(byte row, byte col) {
		return (board[row][col]&FLOOR) != 0;
	}

	public final boolean goalAt(byte row, byte col) {
		return board[row][col] == GOAL;
	}

	public final boolean wallAt(byte row, byte col) {
		return board[row][col] == WALL;
	}
	
	@Override
	public String toString() {
		String result = "";

		for(int i=1; i<=rows; i++) {
			for(int j=1; j<=cols; j++) {
				switch (board[i][j]) {
				case WALL:
					result += '#';
					break;
				case GOAL:
					result += '.';
					break;
				case FLOOR:
					result += ' ';
					break;
				default:
					result += '?';
					break;
				}
			}
			result += "\n";
		}
		
		return result;
	}
}
