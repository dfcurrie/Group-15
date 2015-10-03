package java_proj_group_15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
This Class reads from the provided infile 
and stores the information that it contains
to be accessed by other classes
*/

public class Reader {

	//Variables to be stored from file
	private int numRunInput, numParkInput, numTimeInput;

	//Constructor to read from file
	public Reader(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			String runInput, parkInput, timeInput;

			//Read the file to get information
			runInput = br.readLine();
			parkInput = br.readLine();
			timeInput = br.readLine();
			
			//Format the read string to be converted to an int
			runInput = runInput.substring(2, runInput.length());
			parkInput = parkInput.substring(2, parkInput.length());
			timeInput = timeInput.substring(2, timeInput.length());
			
			//Convert the strings to ints
			numRunInput = Integer.parseInt(runInput);
			numParkInput = Integer.parseInt(parkInput);
			numTimeInput = Integer.parseInt(timeInput);

		} catch (IOException e) {
			System.out.println(e);
			// } catch (FileNotFoundException e) {
			// System.out.println(e);
		}
	}

	//Accessor method to get number of runways
	public int getNumRunInput() {
		return numRunInput;
	}

	//Accessor method to get number of parking zones
	public int getNumParkInput() {
		return numParkInput;
	}

	//Accessor method to get time unit
	public int getTimeInput() {
		return numTimeInput;
	}

	//Override toString for simpler output to screen for what was read in file
	@Override
	public String toString() {
		String toString = numRunInput + ", " + numParkInput + ", " + numTimeInput;
		return toString;
	}
}
