import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Vector;


public class Solver {
	
	public static String solve(Vector<String> lines) {
		new Board(lines);
		
		State solvedState = aStar();
		String revSoloution = solvedState.backtrackSolution();
		
		return  new StringBuffer(revSoloution).reverse().toString();
	}

	private static State aStar() {
		HashSet<State> visited = new HashSet<State>();
		PriorityQueue<State> nodesLeft = new PriorityQueue<State>();
		Vector<State> childStates = new Vector<State>();
		
		nodesLeft.add(Board.state);
		
		while(!nodesLeft.isEmpty()) {
			State parent = nodesLeft.poll();
			visited.add(parent);
//			System.out.println(parent.toString());
			
			if(parent.isSolved()) {
				return parent;
			}
			
			parent.getPushStates(childStates);
			
			for(State child : childStates) {
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
