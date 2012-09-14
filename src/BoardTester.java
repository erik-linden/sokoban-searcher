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
		FileInputStream fstream = new FileInputStream(new File("data", "all.slc"));

		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;

		strLine = br.readLine();

		while(0 != strLine.compareTo("; 110")) {
			strLine = br.readLine();
		}
		
		ArrayList<String> lines = new ArrayList<String>();
		
		strLine = br.readLine();
		strLine = br.readLine();
		while(strLine.length() != 0) {
			lines.add(strLine);
			System.out.println(strLine);
			
			strLine = br.readLine();
		}
		
		String sol = Solver.solve(lines);
        
        System.out.println(sol);
        br.close();
	}

}
