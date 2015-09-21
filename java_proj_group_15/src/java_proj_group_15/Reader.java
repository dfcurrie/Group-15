package java_proj_group_15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Reader {
	
	private int numRunInput, numParkInput, numTimeInput;
	
	public Reader(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String runInput, parkInput, timeInput;
			
			runInput = br.readLine();
			runInput = runInput.substring(2, runInput.length());
			parkInput = br.readLine();
			parkInput = parkInput.substring(2, parkInput.length());
			timeInput = br.readLine();
			timeInput = timeInput.substring(2, timeInput.length());
		
			numRunInput = Integer.parseInt(runInput);
			numParkInput = Integer.parseInt(parkInput);
			numTimeInput = Integer.parseInt(timeInput);
			
		} catch (IOException e) {
			System.out.println(e);
		//} catch (FileNotFoundException e) {
		//	System.out.println(e);	
		}
	}
	
	public int getNumRunInput(){
		return numRunInput;
	}
	
	public int getNumParkInput(){
		return numParkInput;
	}
	
	public int getTimeInput() {
		return numTimeInput;
	}
}
	
	
	
