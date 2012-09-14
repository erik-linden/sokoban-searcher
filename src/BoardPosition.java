
/**
 * Wrapper class that holds the row coordinate 
 * and the column coordinate of a board position.
 * 
 * @author Erik
 *
 */
public class BoardPosition {
	
	/** Row coordinate */
	public final byte row;
	
	/** Column coordinate */
	public final byte col;
	
	/**
	 * Construct a <code>BoardPosition</code> from
	 * a row coordinate and a column coordinate.
	 * 
	 * @param row
	 * @param column
	 */
	public BoardPosition(byte row, byte col) {
		this.row = row;
		this.col = col;
	}
	
	public BoardPosition(BoardPosition bp) {
		this(bp.row, bp.col);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BoardPosition) {
			return equals((BoardPosition) obj);
		}

		return false;
	}
	
	private final boolean equals(BoardPosition bp) {
		return bp != null && this.row == bp.row && this.col == bp.col; 
	}
}
