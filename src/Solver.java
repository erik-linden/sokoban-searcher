import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Vector;


public class Solver {
	
	public static String solve(Vector<String> lines) {
		Board board = new Board(lines);
		System.out.println(board.toString());
		
		State solvedState = aStar();
		String revSoloution = solvedState.backtrackSolution();
		new Guireplay(solvedState);
		
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
//			System.out.println(parent.heuristicValue);
//			System.out.println(parent.toString());
			
			if(parent.isSolved()) {
				System.out.println("Solved in "+parent.nPushes+" pushes.");
				return parent;
			}
			
			parent.getPushStates(childStates);
			
			for(State child : childStates) {
//				System.out.println((int) child.hash);
//				System.out.println(child.connectivity.toString());
				
				if(visited.contains(child)) {
					continue;
				}
				
				if(!nodesLeft.contains(child)) {
					nodesLeft.add(child);
//					System.out.println(child.toString());
//					System.out.println("");
				}
				
			}
		}
		
		return null;
	}
}
