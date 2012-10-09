import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Solver {

	public static String solve(ArrayList<String> lines, Deadline deadline) {
		Board.initialize(lines);

		System.out.println("Board to solve:");
		System.out.println(Board.initialState);

		State solvedState = idaStar(deadline);
		if(solvedState == null) {
			return "";
		}
		String revSoloution = solvedState.backtrackSolution();
		// new Guireplay(solvedState);

		return new StringBuffer(revSoloution).reverse().toString();
	}

	public static String solveBackward(ArrayList<String> lines, Deadline deadline) {
		Board.initialize(lines);
		Board.initializeBackward();

		System.out.println("Board to solve:");
		System.out.println(Board.initialState);

		State solvedState = idaStar(deadline);
		System.out.println("Solved state:");
		System.out.println(solvedState);

		if(solvedState == null) {
			return "";
		}
		String backSoloution = solvedState.backtrackSolution();
		String preSolution =
				solvedState.connectivity.backtrackPathString(
						BackwardState.playerStartPosition,
						solvedState.playerPosition);

		System.out.println("Additional moves: " + preSolution);

		String sol = preSolution + backSoloution;
		sol = sol.replaceAll("R", "l");
		sol = sol.replaceAll("L", "r");
		sol = sol.replaceAll("U", "d");
		sol = sol.replaceAll("D", "u");

		sol = sol.replaceAll("r", "R");
		sol = sol.replaceAll("l", "L");
		sol = sol.replaceAll("u", "U");
		sol = sol.replaceAll("d", "D");

		// new Guireplay(solvedState);

		return sol;
	}

	private static State idaStar(Deadline deadline) {
		HashSet<Integer> visited = new HashSet<Integer>();
		Stack<State> nodesLeft = new Stack<State>();
		List<State> childStates = new LinkedList<State>();

		int cutoff = Board.initialState.getHeuristicValue();

		while(true) {
			
			int nextCutoff = Integer.MAX_VALUE;
			nodesLeft.push(Board.initialState);
			visited.clear();
						
			System.out.println("Search depth: "+cutoff);
			while(!nodesLeft.isEmpty()) {
				
				if(deadline.TimeUntil()<0) {
					return null;
				}
				
				State parent = nodesLeft.pop();

				if(!visited.contains(parent.hashCode())) {
					visited.add(parent.hashCode());

					parent.getChildren(childStates);

					for(State child : childStates) {
						
						if(visited.contains(child.hashCode())) {
							continue;
						}

						if(child.isSolved()) {
							System.out.println("Solved in "
									+ child.getNumberOfSignificantMoves()
									+ " significant moves.");
							return child;
						}

						int childCost = child.getNumberOfSignificantMoves() + child.getHeuristicValue();
						if(childCost > cutoff) {
							nextCutoff = Math.min(nextCutoff, childCost);
						} else if(!nodesLeft.contains(child)) {
							nodesLeft.add(child);
						}

					}
				}
			}
			if(cutoff < nextCutoff) {
				cutoff = nextCutoff;
			} else {
				return null;
			}
		}
	}
}
