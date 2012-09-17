import java.util.Collection;
import java.util.LinkedList;

/**
 * Class that hold the dynamic elements of a board and
 * provides the methods to work with them.
 * 
 * @author Erik
 *
 */
public class State  implements Comparable<State> {
	public BoardPosition playerPosition;
	public Collection<BoardPosition> boxPositions;
	
	public BoardConnectivity connectivity;
	public State parent;
	public byte lastMove;
	public int nPushes;
	public int heuristicValue;
	
	public int hash = 0;
	
	/**
	 * Construct a new state from a static board, the players position and
	 * a list of box positions.
	 * 
	 * @param board the static board
	 * @param playerPosition the player's initial position
	 * @param boxPositions the boxes' initial positions
	 */
	public State(BoardPosition playerPosition,
			Collection<BoardPosition> boxPositions) {
		this.playerPosition = playerPosition;
		this.boxPositions 	= boxPositions;
		this.parent 		= null;
		this.connectivity 	= new BoardConnectivity(this);
		this.lastMove 		= BoardConnectivity.MOVE_NULL;
		this.nPushes		= 0;
		
		setHash();
		this.heuristicValue = Heuristics.calculateHeuristic(this);
	}
	
	/**
	 * Constructs a new state by pushing a box.
	 * 
	 * @param parent the parent state
	 * @param playerPosition the original location of the box being pushed
	 * @param move the move made to push the box, from <code>BoardConnectivity</code>
	 */
	public State(State parent, BoardPosition oldBoxPosition, byte move) {
		this.parent	 		= parent;
		this.playerPosition = new BoardPosition(oldBoxPosition);
		this.lastMove 		= move;
		this.nPushes		= parent.nPushes+1;
		
		boxPositions = new LinkedList<BoardPosition>();
		for(BoardPosition bp : parent.boxPositions) {
			if(!bp.equals(oldBoxPosition)) {
				boxPositions.add(new BoardPosition(bp));
			}
		}
		
		byte row = (byte) (oldBoxPosition.row + BoardConnectivity.rowMask[move]);
		byte col = (byte) (oldBoxPosition.col + BoardConnectivity.colMask[move]);
		
		BoardPosition newBox = new BoardPosition(row, col);
		boxPositions.add(newBox);
		
		connectivity = new BoardConnectivity(this);
		
		this.setHash();
		this.heuristicValue = Heuristics.calculateHeuristic(this);
	}
	
	public void getPushStates(Collection<State> childStates) {
		childStates.clear();
		
		for(BoardPosition boxPos : boxPositions) {
			byte row = boxPos.row;
			byte col = boxPos.col;
			
			for(byte i=0; i<4; i++) {
				byte playerInd = (byte) ((i+2) % 4);
				
				byte pushedBoxRow = (byte) (row + BoardConnectivity.rowMask[i]);
				byte pushedBoxCol = (byte) (col + BoardConnectivity.colMask[i]);
				
				byte playerRow = (byte) (row + BoardConnectivity.rowMask[playerInd]);
				byte playerCol = (byte) (col + BoardConnectivity.colMask[playerInd]);
				
				boolean playerPosReachable   = connectivity.isReachable(playerRow, playerCol);
				boolean pushTargetUnOccupied = !isOccupied(pushedBoxRow, pushedBoxCol);
				boolean targetNotDead		 = !Board.deadAt(pushedBoxRow, pushedBoxCol);
						
				if(playerPosReachable && pushTargetUnOccupied && targetNotDead) {
					byte move = i;
					
					childStates.add(new State(this, boxPos, move));
				}
			}
		}
	}
	
	public String backtrackSolution() {
		String result = "";
		
		if(lastMove == BoardConnectivity.MOVE_NULL) {
			return result;
		}
		
		result += BoardConnectivity.MOVE_CHARS[lastMove];
		
		byte currRow = (byte) (playerPosition.row - BoardConnectivity.rowMask[lastMove]);
		byte currCol = (byte) (playerPosition.col - BoardConnectivity.colMask[lastMove]);
		
		byte startRow = parent.playerPosition.row;
		byte startCol = parent.playerPosition.col;
		
		BoardPosition endPos = new BoardPosition(currRow, currCol);
		BoardPosition startPos = new BoardPosition(startRow, startCol);
		
		result += parent.connectivity.backtrackPathString(endPos, startPos);
		
		result += parent.backtrackSolution();
		
		return result;
	}
	
	/**
	 * Calculates the hash value for the current state.
	 * 
	 * This should conform to the definition of state equality.
	 */
	private void setHash() {
		for(byte i=1; i<=Board.rows; i++) {
			for(byte j=1; j<=Board.cols; j++) {
				if(connectivity.isReachable(i,j)) {
					hash ^= Board.zValues[i][j];
				}
			}
		}
		for (BoardPosition bp : boxPositions) {
			hash ^= (Board.zValues[bp.row][bp.col] << 1);
		}
	}
	
	public boolean isSolved() {
		int boxesOnGoal = boxesOnGoals();
	
		if (boxesOnGoal == Board.goalPositions.size()) {
			return true;
		}

		return false;
	}
	
	public byte boxesOnGoals() {
		byte sum = 0;
		for (BoardPosition boxCoordinate : boxPositions) {
			if (Board.goalAt(boxCoordinate.row, boxCoordinate.col)) {
				sum++;
			}
		}

		return sum;
	}
	
	public boolean isOccupied(byte row, byte col) {
		return Board.wallAt(row, col) || boxAt(row, col);
	}

	public boolean boxAt(byte row, byte col) {
		for (BoardPosition bc : boxPositions) {
			if (bc.row == row && bc.col == col) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean playerAt(byte row, byte col) {
		if (playerPosition.row == row && playerPosition.col == col) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof State) {
			return equals((State) obj);
		}

		return false;
	}

	/**
	 * Two states are considered equal if they have the same connectivity
	 * and the boxes are at the same locations.
	 * 
	 * @param state
	 * @return
	 */
    private boolean equals(State state) {
        return state.boxPositions.containsAll(this.boxPositions)
                && state.connectivity.equals(this.connectivity);
    }

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public String toString() {
		String result = "";
		
		for(byte i=1; i<=Board.rows; i++) {
			for(byte j=1; j<=Board.cols; j++) {
				if(boxAt(i, j) && Board.goalAt(i, j)){
					result += "*";
				}
				else if(boxAt(i, j)){
					result += "$";
				}
				else if(playerAt(i, j)) {
					result += "@";
				}
				else if(Board.goalAt(i, j)) {
					result += ".";
				}
				else if(Board.wallAt(i, j)) {
					result += "#";
				}
				else {
					result += " ";
				}
			}
			result += "\n";
		}
		
		return result;
	}

	@Override
	public int compareTo(State other) {
		if (this.heuristicValue == other.heuristicValue)
            return 0;
        else if (this.heuristicValue > other.heuristicValue)
            return 1;
        else
            return -1;
	}
}
