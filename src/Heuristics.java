import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;


public class Heuristics {

	public static int calculateHeuristic(State state) {
		int[][] costs = new int[Board.nGoals][Board.nGoals];

		int i = 0;
		for(BoardPosition boxPos : state.boxPositions) {
			costs[i] = listGoalDistances(state, boxPos);
			i++;
		}
		
		int minCost = HungarianAlgorithm.hgAlgorithm(costs, "min");
		return minCost;
	}

	private static int[] listGoalDistances(State state, BoardPosition start) {
		byte[][] distMat = new byte[Board.rows+2][Board.cols+2];
		byte goalsFound = 0;

		Queue<BoardPosition> nodesToCheck = new LinkedList<BoardPosition>();
		nodesToCheck.add(start);

		while(!nodesToCheck.isEmpty() && goalsFound<Board.nGoals) {
			BoardPosition current = nodesToCheck.poll();
			byte childDist = (byte) (distMat[current.row][current.col]+1);

			Vector<BoardPosition> children = current.makeAllChildren();
			for(BoardPosition child : children) {
				boolean notChecked = distMat[child.row][child.col]==0;
				boolean notIsWall  = !Board.wallAt(child.row, child.col);
				boolean notIsDead  = !Board.deadAt(child.row, child.col);

				if(notChecked && notIsWall && notIsDead) {
					distMat[child.row][child.col] = childDist;
					nodesToCheck.add(child);
					
					if(Board.goalAt(child.row, child.col)) {
						goalsFound++;
					}
				}
			}

		}
		distMat[start.row][start.col] = 0;

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
				if(d==0) {
					result += "|  ";
				}
				else{
					result += String.format("|%2d", d);
				}
			}
			result += "|\n";
		}

		return result;
	}
}
