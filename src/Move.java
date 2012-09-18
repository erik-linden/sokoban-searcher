/**
 * Abstracts a move on the board, and provides methods for traversing the board.
 */
public enum Move {
	NO_MOVE('-', 0, 0),
	NULL('X', 0, 0),
	RIGHT('R', 0, 1),
	UP('U', -1, 0),
	LEFT('L', 0, -1),
	DOWN('D', 1, 0);

	/**
	 * Holds the values RIGHT, UP, LEFT, DOWN, in that order, for easy looping.
	 */
	public static final Move[] DIRECTIONS = new Move[]{RIGHT, UP, LEFT, DOWN};
	
	/**
	 * The textual representation of this move.
	 */
	public final char moveChar;
	/**
	 * The change in row coordinate that this {@link Move} describes.
	 */
	public final int dr;
	/**
	 * The change in column coordinate that this {@link Move} describes.
	 */
	public final int dc;

	private Move(char moveChar, int dr, int dc) {
		this.moveChar = moveChar;
		this.dr = dr;
		this.dc = dc;
	}

	/**
	 * Returns the move opposite to this one, according to the following rules:
	 *
	 * <ul>
	 * <li>RIGHT and LEFT are opposites</li>
	 * <li>UP and DOWN are opposites</li>
	 * <li>NULL is opposite to itself</li>
	 * <li>NO_MOVE is opposite to itself</li>
	 * </ul>
	 *
	 * @return The move opposite to this one.
	 */
	public Move opposite() {
		switch(this) {
		case RIGHT: return LEFT;
		case UP: return DOWN;
		case LEFT: return RIGHT;
		case DOWN: return UP;
		case NULL: return NULL;
		case NO_MOVE: return NO_MOVE;
		}
		return NO_MOVE;
	}

	/**
	 * Returns a move that is perpendicular to this one, by rotating 90 degrees counterclockwise.
	 *
	 * Cheat sheet:
	 * <ul>
	 * <li>RIGHT -> UP</li>
	 * <li>UP -> LEFT</li>
	 * <li>LEFT -> DOWN</li>
	 * <li>DOWN -> RIGHT</li>
	 * <li>Any other move is mapped to itself.</li>
	 * </ul>
	 * @return The move you get by rotating this one 90 degrees counterclockwise.
	 */
	public Move perpendicular() {
		switch(this) {
		case RIGHT: return UP;
		case UP: return LEFT;
		case LEFT: return DOWN;
		case DOWN: return RIGHT;
		}
		return this;
	}
	
	/**
	 * Returns the position you would end up if you make this move.
	 * 
	 * @param pos
	 *            A position to go from.
	 * @return The position where you end up if you make this move from the
	 *         given position.
	 * @see #stepBack(BoardPosition)
	 */
	public BoardPosition stepFrom(BoardPosition pos) {
		return new BoardPosition((byte)(pos.row + dr), (byte) (pos.col + dc));
	}
	
	/**
	 * The inverse of {@link #stepFrom(BoardPosition)}. That is, this method
	 * returns the position you would end up in if you went backwards from the
	 * given position.
	 * 
	 * @param pos
	 *            A position to go to.
	 * @return The position you would have to start in in order to end up in the
	 *         given position after making this move.
	 */
	public BoardPosition stepBack(BoardPosition pos) {
		return opposite().stepFrom(pos);
	}
	
	@Override
	public String toString() {
		return ""+moveChar;
	}

}
