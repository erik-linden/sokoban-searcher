import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Heuristics {

	static final byte VeryFar   = Byte.MAX_VALUE;
	static final byte NoLastBox = -1;
	private int[][] costMat;
	private Integer value = null;

	public Heuristics() {
		costMat = new int[Board.goalPositions.length][Board.goalPositions.length];
	}
	
	public Heuristics(Heuristics h) {
		costMat = new int[Board.goalPositions.length][];
		for(int i=0; i<costMat.length; ++i) {
			costMat[i] = h.costMat[i].clone();
		}
	}

	private int calculateHeuristic(State state) {
		return bipartDist(state);
	}


	public int getValue(State state) {
		if(value == null) {
			value = calculateHeuristic(state);
		}
		return value;
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

	private int bipartDist(State state) {
		int indLastPushed = state.indPushedLast;
		if(indLastPushed == NoLastBox) {
			for(int i=0; i<Board.goalPositions.length; i++) {
				costMat[i] = listGoalDistances(state, i);
			}
		} else {
			costMat[indLastPushed] = listGoalDistances(state, indLastPushed);
		}

		return HungarianAlgorithm.hgAlgorithm(costMat, "min");
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
				if(child != null
						&& distMat[child.row][child.col] == VeryFar
						&& Board.isPushableTo(child)) {
					distMat[child.row][child.col] = childDist;
					nodesToCheck.add(child);

					if(Board.goalAt(child)) {
						++goalsFound;
					}
				}
			}

		}

		int[] goalDist = new int[Board.goalPositions.length];

		for(int i=0; i<Board.goalPositions.length; ++i) {
			goalDist[i] = distMat[Board.goalPositions[i].row][Board.goalPositions[i].col];
		}

		return goalDist;
	}

	public static String distancesToString(byte[][] distMat) {
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
