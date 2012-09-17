import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.util.Vector;

public class Solver {

	public static String solve(ArrayList<String> lines, Deadline deadline) {
		Board.initialize(lines);

		System.out.println("Board to solve:");
		System.out.println(Board.ToString());
		
		State solvedState = idaStar(deadline);
		if(solvedState == null) {
			return "";
		}
		String revSoloution = solvedState.backtrackSolution();
//		new Guireplay(solvedState);

		return  new StringBuffer(revSoloution).reverse().toString();
	}

	private static State idaStar(Deadline deadline) {
		HashSet<State> visited = new HashSet<State>();
		Stack<State> nodesLeft = new Stack<State>();
		Vector<State> childStates = new Vector<State>();

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

				if(!visited.contains(parent)) {
					visited.add(parent);

					parent.getPushStates(childStates);

					for(State child : childStates) {
						
						if(visited.contains(child)) {
							continue;
						}

						if(child.isSolved()) {
							System.out.println("Solved in "+child.nPushes+" pushes.");
							return child;
						}

						int childCost = child.nPushes+child.getHeuristicValue();
						if(childCost > cutoff) {
							nextCutoff = Math.min(nextCutoff, childCost);
						}
						else if(!nodesLeft.contains(child)) {
							nodesLeft.add(child);
						}

					}
				}
			}
			if(cutoff < nextCutoff) {
				cutoff = nextCutoff;
			}
			else {
				return null;
			}
		}
	}
}
