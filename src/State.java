import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that hold the dynamic elements of a board and
 * provides the methods to work with them.
 * 
 * @author Erik
 *
 */
public class State  implements Comparable<State> {
	
	public BoardPosition playerPosition;
	public final State parent;
	public final Move lastMove;
	public final int indPushedLast;
	public int nPushes;
	
	private BoardPosition[] boxPositions;
	private BoardConnectivity connectivity;
	private Heuristics heuristics = new Heuristics();
	private Integer hash = null;
	private int tunnelExtraPushes = 0;
	
	private State(State parent, BoardPosition playerPosition, BoardPosition[] boxPositions, Move move, int boxInd) {
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
			this.indPushedLast = Heuristics.NoLastBox;
		} else {
			nPushes = parent.nPushes+1;
			this.indPushedLast = boxInd;
			this.heuristics = new Heuristics(parent.heuristics);
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
		this(null, playerPosition, boxPositions, Move.NULL, Heuristics.NoLastBox);
	}
	
	/**
	 * Constructs a new state by pushing a box.
	 * 
	 * @param parent the parent state
	 * @param boxIndex the index of the box to push
	 * @param move the {@link Move} made to push the box
	 */
	public State(State parent, int boxIndex, Move move) {
		this(parent, parent.boxPositions[boxIndex], parent.boxPositions, move, boxIndex);
		boxPositions[boxIndex] = move.stepFrom(boxPositions[boxIndex]);
		tunnelMacro(boxIndex, move);
	}

	public String backtrackSolution() {
		if(lastMove == Move.NULL) {
			return "";
		}

		StringBuilder result = new StringBuilder();
		result.append(lastMove.moveChar);

		BoardPosition prevPos = lastMove.stepBack(playerPosition);
		for(int i=0; i<tunnelExtraPushes; ++i) {
			prevPos = lastMove.stepBack(prevPos);
			result.append(lastMove.moveChar);
		}

		result.append(parent.connectivity.backtrackPathString(prevPos, parent.playerPosition));
		result.append(parent.backtrackSolution());

		return result.toString();
    }

    /**
	 * Checks if the last performed move triggers entering a tunnel, and keeps
	 * pushing the box until it reaches the end of the tunnel.
	 *
	 * @param boxIndex the box that was just pushed
	 * @param direction the move that was performed on the box
	 */
	private void tunnelMacro(int boxIndex, Move direction) {
		Move perpL = direction.perpendicular();
		Move perpR = perpL.opposite();

		// Check if the box is within the tunnel already
		BoardPosition frontOfBox = direction.stepFrom(boxPositions[boxIndex]);
		if(isOccupied(frontOfBox)
				|| !Board.wallAt(perpL.stepFrom(boxPositions[boxIndex]))
				|| !Board.wallAt(perpR.stepFrom(boxPositions[boxIndex]))) {
			return;
		}

		// Keep going forward while in the tunnel
		while(!isOccupied(frontOfBox)
				&& Board.wallAt(perpL.stepFrom(frontOfBox))
				&& Board.wallAt(perpR.stepFrom(frontOfBox))) {
			++tunnelExtraPushes;
			frontOfBox = direction.stepFrom(frontOfBox);
		}
		nPushes += tunnelExtraPushes;
		boxPositions[boxIndex] = direction.stepBack(frontOfBox);
		playerPosition = direction.stepBack(boxPositions[boxIndex]);
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
	
	/**
	 * Used for drawing the solution.
	 * @return a {@link List} with all {@link BoardPosition}s visited since the parent's end position.
	 */
	public List<BoardPosition> getPositionSequence() {
		LinkedList<BoardPosition> result = new LinkedList<BoardPosition>();

		BoardPosition pos = playerPosition;
		for(int i=0; i<tunnelExtraPushes; ++i) {
			pos = lastMove.stepBack(pos);
			result.addFirst(pos);
		}

		pos = lastMove.stepBack(pos);

		List<Move> intermediateMoves = parent.connectivity.backtrackPathMoves(pos, parent.playerPosition);
		System.out.println(intermediateMoves);

		for(Move move : intermediateMoves) {
			result.addFirst(pos);
			pos = move.stepBack(pos);
		}

		return result;
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
	
	public BoardPosition getBox(int ind) {
		return boxPositions[ind];
	}

	public BoardConnectivity getConnectivity() {
		if(connectivity == null) {
			connectivity = new BoardConnectivity(this);
		}
		return connectivity;
	}

	public int getHeuristicValue() {
		if(heuristics.value == null) {
			heuristics.calculateHeuristic(this);
		}
		return heuristics.value;
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
		if (this.heuristics.value == other.heuristics.value)
            return 0;
        else if (this.heuristics.value > other.heuristics.value)
            return 1;
        else
            return -1;
	}
}
