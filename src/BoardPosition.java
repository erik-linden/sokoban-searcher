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
		this(bp.row, bp.col);
	}
	
	/**
	 * Returns a new move in the requested direction.
	 * Returns null if move is pushing from a wall
	 * or if being pushed into a wall or a dead square.
	 * 
	 * @param move
	 * @return
	 */
	public BoardPosition makeChild(byte move) {
		BoardPosition child = new BoardPosition(this.row, this.col);
		
		byte revDir = (byte) ((move+2)%4);
		
		if(!Board.wallAt((byte)(child.row +BoardConnectivity.rowMask[revDir]), 
					     (byte)(child.col +BoardConnectivity.colMask[revDir]))) {
			child.row += BoardConnectivity.rowMask[move];
			child.col += BoardConnectivity.colMask[move];
			
			if(!Board.wallAt(child.row, child.col) && !Board.deadAt(child.row, child.col)) {
				return child;
			}
		}
		
		return null;
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
