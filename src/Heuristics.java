import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Heuristics {

	static final byte VeryFar = Byte.MAX_VALUE;

	
	public static int calculateHeuristic(State state) {
		return bipartDist(state);
	}

	private static int manhattanDist(State state) {
		int distance = 0;

		for(BoardPosition boxPos : state.getBoxPositions()) {
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
	
	private static int bipartDist(State state) {
		int[][] costs = new int[Board.nGoals][Board.nGoals];

		int i = 0;
		for(BoardPosition boxPos : state.getBoxPositions()) {
			costs[i] = listGoalDistances(state, boxPos);
			i++;
		}
		
		int minCost = HungarianAlgorithm.hgAlgorithm(costs, "min");
		return minCost;
	}

	private static int[] listGoalDistances(State state, BoardPosition start) {
		byte[][] distMat = new byte[Board.rows+2][Board.cols+2];
		byte goalsFound = 0;
		
		for(int i=1; i<=Board.rows; i++) {
			for(int j=1; j<=Board.cols; j++) {
				distMat[i][j] = VeryFar;
			}
		}
		distMat[start.row][start.col] = 0;

		Queue<BoardPosition> nodesToCheck = new LinkedList<BoardPosition>();
		nodesToCheck.add(start);

		while(!nodesToCheck.isEmpty() && goalsFound<Board.nGoals) {
			BoardPosition current = nodesToCheck.poll();
			byte childDist = (byte) (distMat[current.row][current.col]+1);

			List<BoardPosition> children = current.makeAllChildren();
			for(BoardPosition child : children) {
				if(child != null) {
					boolean notChecked = distMat[child.row][child.col]==VeryFar;
					boolean notIsWall  = !Board.wallAt(child);
					boolean notIsDead  = !Board.deadAt(child);

					if(notChecked && notIsWall && notIsDead) {
						distMat[child.row][child.col] = childDist;
						nodesToCheck.add(child);

						if(Board.goalAt(child)) {
							goalsFound++;
						}
					}
				}
			}

		}

		int[] goalDist = new int[Board.goalPositions.size()];
		byte i = 0;

		for(BoardPosition goal : Board.goalPositions) {
			goalDist[i] = distMat[goal.row][goal.col];
			i++;
		}

		return goalDist;
	}

	static String distancesToString(byte[][] distMat) {
		String result = "";

		for(byte i=1; i<=Board.rows; i++) {
			for(byte j=1; j<=Board.cols; j++) {
				byte d = distMat[i][j];
				if(d==VeryFar) {
					result += "|   ";
				}
				else{
					result += String.format("|%3d", d);
				}
			}
			result += "|\n";
		}

		return result;
	}
}
