import java.io.*;
import java.net.*;
import java.util.Vector;

public class Client {

	public static void main(String[] pArgs) 
	{		
		if(pArgs.length<3)
		{
			System.out.println("usage: java Client host port boardnum");
			return;
		}
	
		try
		{
			Socket lSocket=new Socket(pArgs[0],Integer.parseInt(pArgs[1]));
			PrintWriter lOut=new PrintWriter(lSocket.getOutputStream());
			BufferedReader lIn=new BufferedReader(new InputStreamReader(lSocket.getInputStream()));
	
            lOut.println(pArgs[2]);
            lOut.flush();

            String lLine=lIn.readLine();

            //read number of rows
            int lNumRows=Integer.parseInt(lLine);

            Vector<String> lines = new Vector<String>();
            System.out.println("From server:");
            for(int i=0;i<lNumRows;i++)
            {
                lLine=lIn.readLine();
                lines.add(lLine);
                System.out.println(lLine);
            }
            String sol = Solver.solve(lines);
            
            System.out.println(sol);
            
//            Board board = new Board(lines);
//            
//            System.out.println("\nAll static elements:");
//            System.out.println(board.toString());
//            
//            System.out.println("\nInitial connectivity:");
//            System.out.println(Board.state.connectivity.toString());
//            
//            System.out.println("\nInitial state:");
//            System.out.println(Board.state.toString());
//            
//            Vector<State> childStates = new Vector<State>();
//            Board.state.getPushStates(childStates);
//                        
//            for(State state : childStates) {
//            	System.out.println(state.toString());
//            	System.out.println(state.heuristicValue);
//            }
//            
//            childStates.lastElement().getPushStates(childStates);
//            
//            for(State state : childStates) {
//            	System.out.println(state.toString());
//            	System.out.println(state.heuristicValue);
//            }
//            
//            System.out.println(childStates.lastElement().backtrackSolution());

            
//            System.out.println(Board.state.hash);

            //send the solution to the server
            lOut.flush();
    
            //read answer from the server
            lLine=lIn.readLine();
    
            System.out.println(lLine);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}
}
