import java.util.Vector;


/**
 * Wrapper class that holds the row coordinate 
 * and the column coordinate of a board position.
 * 
 * @author Erik
 *
 */
public class BoardPosition {
	
	/** Row coordinate */
	public byte row;
	
	/** Column coordinate */
	public byte col;
	
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
		this.row = bp.row;
		this.col = bp.col;
	}
	
	public BoardPosition makeChild(byte move) {
		BoardPosition child = new BoardPosition(this.row, this.col);
		
		child.row += BoardConnectivity.rowMask[move];
		child.col += BoardConnectivity.colMask[move];
		
		return child;
	}
	
	public Vector<BoardPosition> makeAllChildren() {
		Vector<BoardPosition> children = new Vector<BoardPosition>(4);
		
		for(byte i=0; i<4; i++) {
			children.add(makeChild(i));
		}
		
		return children;
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
