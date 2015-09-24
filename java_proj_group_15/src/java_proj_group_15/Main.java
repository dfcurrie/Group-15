package java_proj_group_15;

import java.util.Timer;

public class Main {

	public static void main(String[] args) {
		try {
			//Print a description of what the program does if java Main -h is run
			if (args[0].equals("-h")) {
				System.out.println("This is a short description of what this program does.");
				System.exit(1);
			
			//Run the program on a file containg variables of state
			} else if (args[0].equals("-f") && args[2].equals("-o")) {
				System.out.println("Input: " + args[1] + "\nOutput: " + args[3]);
				run(args[1]);
				
			//Print error message if invalid inputs given
			} else {
				System.out.println(
						"Invalid command line arguments\nProper usage: -f <input_file_name> -o <output_file_name>");
				System.exit(-1);
			}

		//Error checking for if not enough arguments passed
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(
					"Invalid command line arguments\nProper usage: -f <input_file_name> -o <output_file_name>");
			System.exit(-1);
		}
	}

	//Function that runs the program by creating the reader to get read a file
	// and creating the time thread and starting accepting user input
	public static void run(String fileName) {
		//Read from input file to create so called "airport state" and prints variables contained
		Reader reader = new Reader(System.getProperty("user.dir") + "\\"		
				+ fileName);
		System.out.println(reader.toString());
		
		//Create airport based on numbers from input file
		Airport airport = new Airport(reader.getNumRunInput(), reader
				.getNumParkInput());	

		// Create timer to update simulation at set interval
		Timer timer = new Timer();												
		Time timeTracker = new Time();
		
		// Update in-simulation time every few seconds as specified
		timer.schedule(timeTracker, 0, reader.getTimeInput() * 1000); 

		//Start airplane input thread which loads planes into the Input.airplanes 
		// list for calculations based on standard input
		Input input = new Input(timeTracker, airport); 
		input.start(); 				

		//System.exit(1);
	}
}
