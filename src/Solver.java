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
}