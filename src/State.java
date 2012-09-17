import java.util.Arrays;
import java.util.Collection;

/**
 * Class that hold the dynamic elements of a board and
 * provides the methods to work with them.
 * 
 * @author Erik
 *
 */
public class State  implements Comparable<State> {
	
	public final BoardPosition playerPosition;
	public final State parent;
	public final Move lastMove;
	public final int nPushes;
	
	private BoardPosition[] boxPositions;
	private BoardConnectivity connectivity;
	private Integer heuristicValue;
	private Integer hash = null;
	
	private State(State parent, BoardPosition playerPosition, BoardPosition[] boxPositions, Move move) {
		this.parent = parent;
		this.playerPosition = playerPosition;
		/*
		 * This copy will be a shallow copy, meaning the elements in the array
		 * are copied by reference. This means that after this statement,
		 * this.boxPositions[0] == boxPositions[0] will return true, but
		 * this.boxPositions == boxPositions will not.
		 */
		this.boxPositions = boxPositions.clone();
		lastMove = move;
		if(parent == null) {
			nPushes = 0;
		} else {
			nPushes = parent.nPushes+1;
		}
	}

	/**
	 * Construct a new state from a static board, the players position and
	 * a list of box positions.
	 * 
	 * @param board the static board
	 * @param playerPosition the player's initial position
	 * @param boxPositions the boxes' initial positions
	 */
	public State(BoardPosition playerPosition,
			BoardPosition[] boxPositions) {
		this(null, playerPosition, boxPositions, Move.NULL);
	}
	
	/**
	 * Constructs a new state by pushing a box.
	 * 
	 * @param parent the parent state
	 * @param boxIndex the index of the box to push
	 * @param move the {@link Move} made to push the box
	 */
	public State(State parent, int boxIndex, Move move) {
		this(parent, parent.boxPositions[boxIndex].clone(), parent.boxPositions, move);
		boxPositions[boxIndex] = move.stepFrom(boxPositions[boxIndex]);
	}
	
	public boolean isSolved() {
		return numBoxesOnGoals() == Board.goalPositions.length;
	}

	public void getChildren(Collection<State> childStates) {
		childStates.clear();

		for(int boxIndex=0; boxIndex<boxPositions.length; boxIndex++) {
			for(Move m : Move.DIRECTIONS) {
				BoardPosition boxDestination = m.stepFrom(boxPositions[boxIndex]);
				
				BoardPosition playerPos = m.opposite().stepFrom(boxPositions[boxIndex]);
				
				boolean playerPosReachable   = connectivity.isReachable(playerPos);
				boolean pushTargetUnOccupied = !isOccupied(boxDestination);
				boolean targetNotDead		 = !Board.deadAt(boxDestination);
						
				if(playerPosReachable && pushTargetUnOccupied && targetNotDead) {
					childStates.add(new State(this, boxIndex, m));
				}
			}
		}
	}
	
	public String backtrackSolution() {
		if(lastMove == Move.NULL) {
			return "";
		}
		
		StringBuilder result = new StringBuilder();
		
		result.append(lastMove.moveChar);
		result.append(parent.connectivity.backtrackPathString(lastMove.stepBack(playerPosition), parent.playerPosition));
		result.append(parent.backtrackSolution());
		
		return result.toString();
	}
	
	public byte numBoxesOnGoals() {
		byte sum = 0;
		for (BoardPosition boxCoordinate : boxPositions) {
			if (Board.goalAt(boxCoordinate)) {
				sum++;
			}
		}

		return sum;
	}
	
	public boolean isOccupied(BoardPosition pos) {
		return Board.wallAt(pos) || boxAt(pos);
	}

	public boolean boxAt(BoardPosition pos) {
		return boxAt(pos.row, pos.col);
	}

	public boolean boxAt(byte row, byte col) {
		for (BoardPosition bc : boxPositions) {
			if (bc.row == row && bc.col == col) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean playerAt(BoardPosition pos) {
		return playerAt(pos.row, pos.col);
	}

	private boolean playerAt(byte row, byte col) {
		if (playerPosition.row == row && playerPosition.col == col) {
			return true;
		}
		
		return false;
	}
	
	public BoardPosition[] getBoxPositions() {
		return boxPositions;
	}

	public BoardConnectivity getConnectivity() {
		if(connectivity == null) {
			connectivity = new BoardConnectivity(this);
		}
		return connectivity;
	}

	public int getHeuristicValue() {
		if(heuristicValue == null) {
			heuristicValue = Heuristics.calculateHeuristic(this);
		}
		return heuristicValue;
	}

	/**
	 * Calculates the hash value for the current state.
	 *
	 * This should conform to the definition of state equality.
	 */
	private void setHash() {
		hash = 0;
		for(byte i=1; i<=Board.rows; i++) {
			for(byte j=1; j<=Board.cols; j++) {
				if(getConnectivity().isReachable(i, j)) {
					hash ^= Board.zValues[i][j];
				}
			}
		}
		for (BoardPosition bp : boxPositions) {
			hash ^= (Board.zValues[bp.row][bp.col] << 1);
		}
	}

	@Override
	public int hashCode() {
		if(hash == null) {
			setHash();
		}
		return hash.intValue();
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
	    return Arrays.equals(state.boxPositions, boxPositions)
	            && state.getConnectivity().equals(getConnectivity());
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
