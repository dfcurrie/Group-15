package java_proj_group_15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Reader {
	
	private String numRunInput, numParkInput, numTimeInput;
	
	public Reader(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
		
			String numRunInput = br.readLine();
			String numParInput = br.readLine();
			String numTimeInput = br.readLine();
		
		} catch (IOException e) {
			System.out.println(e);
		//} catch (FileNotFoundException e) {
		//	System.out.println(e);	
		}
	}
	
	public String getNumRunInput(){
		return numRunInput;
	}
	
	public String getNumParkInput(){
		return numParkInput;
	}
	
	public String getTimeInput() {
		return numTimeInput;
	}
}
	
	
	
