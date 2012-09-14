import java.io.*;
import java.util.Vector;


public class BoardTester {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		final int file = 0;
		String fileName = null;
		
		switch(file) {
		case 0:
			fileName = "m1.txt";
			break;
		case 1:
			fileName = "all.slc";
			break;
		}
		
		FileInputStream fstream = new FileInputStream(new File("data", fileName));

		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;

		strLine = br.readLine();
		Vector<String> lines = new Vector<String>();
		
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
			while(0 != strLine.compareTo(";LEVEL 2080")) {
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
		
		
		String sol = Solver.solve(lines);
        
        System.out.println(sol);
        br.close();
	}

}
