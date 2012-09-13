import java.io.*;
import java.util.Vector;


public class BoardTester {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		FileInputStream fstream = new FileInputStream("D:\\Desktop\\all.slc");

		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;

		strLine = br.readLine();

		while(0 != strLine.compareTo(";LEVEL 1")) {
			strLine = br.readLine();
		}
		
		Vector<String> lines = new Vector<String>();
		
		strLine = br.readLine();
		while(strLine.charAt(0) != ';') {
			lines.add(strLine);
			System.out.println(strLine);
			
			strLine = br.readLine();
		}
		
		String sol = Solver.solve(lines);
        
        System.out.println(sol);
        br.close();
	}

}
