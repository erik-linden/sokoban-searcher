import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.Vector;


public class Solver {

	public static String solve(Vector<String> lines, Deadline deadline) {
		Board board = new Board(lines);

		State solvedState = idaStar(deadline);
		if(solvedState == null) {
			return "";
		}
		String revSoloution = solvedState.backtrackSolution();
//		new Guireplay(solvedState);

		return  new StringBuffer(revSoloution).reverse().toString();
	}

	private static State aStar() {
		HashSet<State> visited = new HashSet<State>();
		PriorityQueue<State> nodesLeft = new PriorityQueue<State>();
		Vector<State> childStates = new Vector<State>();

		nodesLeft.add(Board.initialState);

		while(!nodesLeft.isEmpty()) {
			State parent = nodesLeft.poll();
			visited.add(parent);

			if(parent.isSolved()) {
				System.out.println("Solved in "+parent.nPushes+" pushes.");
				return parent;
			}

			parent.getPushStates(childStates);

			for(State child : childStates) {

				if(visited.contains(child)) {
					continue;
				}

				if(!nodesLeft.contains(child)) {
					nodesLeft.add(child);
				}

			}
		}

		return null;
	}

	private static State idaStar(Deadline deadline) {
		HashSet<State> visited = new HashSet<State>();
		Stack<State> nodesLeft = new Stack<State>();
		Vector<State> childStates = new Vector<State>();

		int cutoff = Board.initialState.heuristicValue;

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

						int childCost = child.nPushes+child.heuristicValue;
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
