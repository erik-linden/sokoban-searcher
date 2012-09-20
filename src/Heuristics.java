import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Heuristics {

	static final byte VeryFar   = Byte.MAX_VALUE;
	static final byte NoLastBox = -1;
	int[][] costMat = new int[Board.goalPositions.length][Board.goalPositions.length];
	Integer value = null;

	public void calculateHeuristic(State state) {
		bipartDist(state);
	}
	
	Heuristics(Heuristics h) {
		this.costMat = h.costMat.clone();
		this.value   = null; 
	}

	public Heuristics() {
		// TODO Auto-generated constructor stub
	}

	private int manhattanDist(State state) {
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

	private void bipartDist(State state) {
		int indLastPushed = state.indPushedLast;
		if(indLastPushed == NoLastBox) {
			for(int i=0; i<Board.goalPositions.length; i++) {
				costMat[i] = listGoalDistances(state, i);
			}
		}
		else {
			costMat[indLastPushed] = listGoalDistances(state, indLastPushed);
		}

		value = HungarianAlgorithm.hgAlgorithm(costMat, "min");
	}

	private int[] listGoalDistances(State state, int boxInd) {
		byte[][] distMat = new byte[Board.rows+2][Board.cols+2];
		BoardPosition start = state.getBox(boxInd);
		byte goalsFound = 0;

		for(int i=1; i<=Board.rows; i++) {
			for(int j=1; j<=Board.cols; j++) {
				distMat[i][j] = VeryFar;
			}
		}
		distMat[start.row][start.col] = 0;

		Queue<BoardPosition> nodesToCheck = new LinkedList<BoardPosition>();
		nodesToCheck.add(start);

		while(!nodesToCheck.isEmpty() && goalsFound<Board.goalPositions.length) {
			BoardPosition current = nodesToCheck.poll();
			byte childDist = (byte) (distMat[current.row][current.col]+1);

			List<BoardPosition> children = current.getPushableNeighbors();
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

		int[] goalDist = new int[Board.goalPositions.length];
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
