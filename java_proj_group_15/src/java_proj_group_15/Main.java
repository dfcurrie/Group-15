package java_proj_group_15;

import java.util.Timer;

public class Main {

	public static void main(String[] args) {
		try {
			if (args[0].equals("-h")) {
				System.out.println(
						"This is a short description of what this program does.");
				System.exit(1);

			} else if (args[0].equals("-f") && args[2].equals("-o")) {
				System.out.println("Input: " + args[1] + "\nOutput: "
						+ args[3]);
					run(args[1]);
				System.out.println(
						"Invalid command line arguments\nProper usage: -f <input_file_name> -o <output_file_name>");
				System.exit(-1);
			}

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(
					"Invalid command line arguments\nProper usage: -f <input_file_name> -o <output_file_name>");
			System.exit(-1);
		}
	}
	
	public static void run(String fileName) {
		// Read from data file
		// Create airport and time

		Reader reader = new Reader(System.getProperty("user.dir")+ "\\" + fileName);
		Airport airport = new Airport(reader.getNumRunInput(), reader.getNumParkInput());
		Input input = new Input();  			// Start airplane input thread which loads planes
		input.start();							// into the Input.airplanes list for calculations
												// based on standard input
		Timer timer = new Timer();
		timer.schedule(new Time(), 0, reader.getTimeInput()*1000);	// Update in-simulation time every few seconds as specified
		// Calculate landing scenarios based on current time
		// Print output to screen and file
	}
}
