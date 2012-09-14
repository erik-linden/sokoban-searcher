
public class Heuristics {
	
	public static int calculateHeuristic(State state) {
		int distance = 0;
		
		for(BoardPosition boxPos : state.boxPositions) {
			int minDist = Integer.MAX_VALUE;
			
			for(BoardPosition goalPos : Board.goalPositions) {
			    minDist = Math.min(minDist,
			            Math.abs(boxPos.row - goalPos.row)
			            + Math.abs(boxPos.col - goalPos.col));
			}
			
			distance += minDist;
		}
		
		return distance;
	}
}
