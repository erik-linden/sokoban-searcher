import java.util.LinkedList;


/**
 * Class used to analyze the connectivity of a board state.
 * 
 * @author Erik
 *
 */
public class BoardConnectivity {
	public static final byte MOVE_NULL  = -1;
	public static final byte MOVE_RIGHT = (1 << 0);
	public static final byte MOVE_UP    = (1 << 1);
	public static final byte MOVE_LEFT  = (1 << 2);
	public static final byte MOVE_DOWN  = (1 << 3);
	public static final String moveLetters = "RULD";

	/**
	 * Mask used to look at adjacent squares.
	 */
	public static final byte[] rowMask = {0, -1, 0, 1};
	/**
	 * Mask used to look at adjacent squares.
	 */
	public static final byte[] colMask = {1, 0, -1, 0};

	private final byte[][] connectivity;

	/**
	 * Constructs the connectivity matrix for the 
	 * supplied state.
	 * 
	 * @param state State used for connectivity graph
	 */
	public BoardConnectivity(State state) {
		connectivity = new byte[Board.rows+2][Board.cols+2];

		for(int i=0; i<Board.rows+2; i++) {
			for(int j=0; j<Board.cols+2; j++) {
				connectivity[i][j] = 0;
			}
		}

		setConnectivity(state);
	}

	/**
	 * Uses a flood-fill algorithm to mark all reachable squares.
	 * 
	 * @param state State checked for connectivity
	 */
	private void setConnectivity(State state) {
		LinkedList<BoardPosition> positionsToExpand = new LinkedList<BoardPosition>();

		positionsToExpand.add(state.playerPosition);
		byte playerRow = state.playerPosition.row;
		byte playerCol = state.playerPosition.col;

		// The players position is reached via the null move.
		connectivity[playerRow][playerCol] = MOVE_NULL;

		while(!positionsToExpand.isEmpty()) {

			BoardPosition currenPos = positionsToExpand.pop();

			for(byte i=0; i<4; i++) {
				byte row = (byte) (currenPos.row + rowMask[i]);
				byte col = (byte) (currenPos.col + colMask[i]);

				if(!state.isOccupied(row, col) && connectivity[row][col] == 0) {
					connectivity[row][col] =  (byte) (1 << i);

					BoardPosition bp = new BoardPosition(row, col);
					positionsToExpand.add(bp);
				}
			}
		}
	}
	
	public boolean isReachable(byte row, byte col) {
		return connectivity[row][col] != 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BoardConnectivity) {
			return equals((BoardConnectivity) obj);
		}

		return false;
	}

	/**
	 * Two connectivity objects are seen as equal if
	 * exactly the same squares are reachable in both.
	 * 
	 * @param bc
	 * @return
	 */
	private boolean equals(BoardConnectivity bc) {
		for(int i=1; i<=Board.rows; i++) {
			for(int j=1; j<=Board.cols; j++) {
				if((connectivity[i][j] == 0) != (bc.connectivity[i][j] == 0)) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public String toString() {
		String result = "";

		for(int i=1; i<=Board.rows; i++) {
			for(int j=1; j<=Board.cols; j++) {
				switch (connectivity[i][j]) {
				case MOVE_RIGHT:
					result += "R";
					break;
				case MOVE_UP:
					result += "U";
					break;
				case MOVE_LEFT:
					result += "L";
					break;
				case MOVE_DOWN:
					result += "D";
					break;
				case MOVE_NULL:
					result += "X";
					break;
				case 0:
					result += "-";
					break;

				}
			}
			result += "\n";
		}
		
		return result;
	}
}
