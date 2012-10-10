import java.util.Collection;

public class BackwardState extends State {
    
    /**
     * The place where the player starts in forward search. The player will need
     * to "return" to this position after a backward search.
     */
    public static BoardPosition playerStartPosition;

    /**
     * Constructs an initial state with boxes at the given positions.
     * @param boxPositions
     */
    public BackwardState(BoardPosition[] boxPositions) {
        super(null, boxPositions);
    }
    
	/**
	 * Constructs a new state by pulling a box.
	 * 
	 * @param parent the parent state
	 * @param boxIndex the index of the box to push
	 * @param move the direction in which to pull the box
	 */
	public BackwardState(State parent, int boxIndex, Move move) {
		super(parent, move.stepFrom(parent.boxPositions[boxIndex], 2), parent.boxPositions, move, boxIndex);
		boxPositions[boxIndex] = move.stepFrom(boxPositions[boxIndex]);
	}
	
	@Override
	public boolean isSolved() {
	    return super.isSolved() && connectivity.isReachable(playerStartPosition);
	}
	
	@Override
	public void getChildren(Collection<State> childStates) {
		childStates.clear();

		BoardPosition boxDestination, playerEndPos;

		for(int boxIndex=0; boxIndex<boxPositions.length; boxIndex++) {
			for(Move m : Move.DIRECTIONS) {
				boxDestination = m.stepFrom(boxPositions[boxIndex]);
				playerEndPos = m.stepFrom(boxPositions[boxIndex], 2);
				
				if(connectivity.isReachable(boxDestination)
						&& connectivity.isReachable(playerEndPos)
						&& !isOccupied(boxDestination)) {
					childStates.add(new BackwardState(this, boxIndex, m));
				}
			}
		}
	}

	/**
	 * Overrides {@link State#getHeuristicValue()} in order to save time, since
	 * backward search is performed without heuristic.
	 *
	 * @return always zero.
	 */
	@Override
	public int getHeuristicValue() {
		return 0;
	}
}
