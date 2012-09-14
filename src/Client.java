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

			//send the solution to the server
			lOut.flush();

			//read answer from the server
			lLine=lIn.readLine();

			System.out.println(lLine);

			lSocket.close();
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}
}
