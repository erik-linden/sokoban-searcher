import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BoardTester {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		final int file = 0;
		String fileName = null;
		String level = ";LEVEL 1";
		
		switch(file) {
		case 0:
			fileName = "m1.txt";
			break;
		case 1:
			fileName = "all.slc";
			break;
		}
		
		BufferedReader br =
				new BufferedReader(new InputStreamReader(new DataInputStream(
						new FileInputStream(new File("data", fileName)))));

		String strLine;

		strLine = br.readLine();
		ArrayList<String> lines = new ArrayList<String>();
		
		switch(file) {
		case 0:
			while(0 != strLine.compareTo("; 10")) {
				strLine = br.readLine();
			}
						
			strLine = br.readLine();
			strLine = br.readLine();
			while(strLine.length() != 0) {
				lines.add(strLine);
				System.out.println(strLine);
				
				strLine = br.readLine();
			}
			break;
		case 1:
			while(strLine.compareTo(level) != 0) {
				strLine = br.readLine();
			}
						
			strLine = br.readLine();
			while(strLine.charAt(0) != ';') {
				lines.add(strLine);
				System.out.println(strLine);
				
				strLine = br.readLine();
			}
			break;
		}
		
		br.close();
		
        String sol = Solver.solveForward(lines, new Deadline(30000));
        String solb = Solver.solveBackward(lines, new Deadline(30000));
        
        System.out.println();
        
        System.out.println("Solution:");
        System.out.println(sol);
        System.out.println("Backward solution:");
        System.out.println(solb);
	}

}
