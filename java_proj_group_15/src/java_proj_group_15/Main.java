package java_proj_group_15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
					run();
			} else {
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
	
	public static void run() {
		// Read from data file
		
		Input input = new Input();  // Start airplane input thread which loads planes
		input.start();				// into the Input.airplanes list for calculations
									// based on standard input
		
		// Calculate landing scenarios
		// Print output to screen and file
	}
}
