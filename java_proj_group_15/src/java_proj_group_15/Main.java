package java_proj_group_15;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Main {

	public static String outputFile;
	public static String inputFile;

	public static void main(String[] args) {
		try {
			// Print a description of what the program does if java Main -h is
			// run
			if (args[0].equals("-h")) {
				System.out.println(
						"Rayce Rossum		Rayce.Rossum@gmail.com\nCaleb Steele-Lane	csteele@ucalgary.ca\nDavid Currie		dfcurrie@ucalgary.ca\nJoshua Hong		johong@ucalgary.ca\n\nProgram: Airport Landing Simulator\nDescription:This a program to simulates the landing of planes at a airport\nand determines whether a group of planes could land with certain amounts\nof fuel and varying taxi/runwaytime\n\nHow to run: This program runs from command line\nto run it you must input into the command line like this\n<program_name> -f <input_file_name> -o <output_file_name>\nwhere the input_file_name contains the information\nR=(number of runways)\nP=(number of parking spaces)\nT=(time units)\nand output_file_name is the name of the file\nwhere you want the information outputted\n\nThen the program will ask for each of following for each CASE\n• ID: is the unique identifier of the plane.\n• Fuel: is an integer showing how much fuel the plane has.\n• Fuel burn rate: is an integer showing how much fuel is burnt at a unit of\ntime.\n• Landing time: is an integer showing how many units of time it takes for\nthe plane to land.\n• Taxi time: is an integer showing how many units of time it takes for the\nplane to reach the parking, after it had landed.\n• Unload time: is an integer showing how many units of time it takes for\nthe plane to unload its cargo, after it had arrived at the parking\nwith the program beginning to run in real time\n after you've entered the first case\n\n\nafter each scenario the program outputs either\nThis if it fails because one of the planes runs out of fuel before parking\nCASE <number>: IMPOSSIBLE\n<emty_line>\n\n\nor if the Scenario is successful\nCASE <number>: POSSIBLE\n<plain_id> LANDED AT <time_unit> WITH <fuel> REMAINING.\n.....\n{for all planes in this scenario}\n.....\n<empty_line>\n");
				System.exit(1);

				// Run the program on a file containg variables of state
			} else if (args[0].equals("-f") && args[2].equals("-o")) {
				System.out.println("Input: " + args[1] + "\nOutput: "
						+ args[3]);
				inputFile = args[1];
				outputFile = args[3];
				run();

				// Print error message if invalid inputs given
			} else {
				System.out.println(
						"Invalid command line arguments\nProper usage: -f <input_file_name> -o <output_file_name>");
				System.exit(-1);
			}

			// Error checking for if not enough arguments passed
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(
					"Invalid command line arguments\nProper usage: -f <input_file_name> -o <output_file_name>");
			System.exit(-1);
		}
	}

	// Function that runs the program by creating the reader to get read a file
	// and creating the time thread and starting accepting user input
	public static void run() {
		// Read from input file to create so called "airport state" and prints
		// variables contained
		Reader reader = new Reader(System.getProperty("user.dir") + "\\"
				+ inputFile);
		System.out.println(reader.toString());

		// Create airport based on numbers from input file
		Airport airport = new Airport(reader.getNumRunInput(), reader
				.getNumParkInput());

		// Create timer to update simulation at set interval
		Timer timer = new Timer();
		Time timeTracker = new Time();

		// Update in-simulation time every few seconds as specified
		timer.schedule(timeTracker, 0, reader.getTimeInput() * 1000);

		// Start airplane input thread which loads planes into the
		// Input.airplanes
		// list for calculations based on standard input
		BlockingQueue<Airplane> queue = new ArrayBlockingQueue<>(10);
		Input input = new Input(timeTracker, queue);
		input.start();

		ArrayList<Airplane> airplanes = new ArrayList<Airplane>();

		while (!input.isKillScenario()) {
			
			boolean allPlanesFinished = false;
			Airplane curPlane = null;
			airplanes.clear();
			input.setEndScenario(false);
			timeTracker.resetTime(); // Time starts at 0 when Case is entered
			
			for (int i = 0; i < airport.getNumRunways(); i++) {
				airport.getRunways().get(i).setOccupied(false);
			}
			for (int i = 0; i < airport.getNumParkings(); i++) {
				airport.getParkings().get(i).setOccupied(false);
				airport.getParkings().get(i).getBookings().clear();
			}
			

			while (!input.isEndScenario()) {
					Airplane plane = null;
					plane = queue.poll();
					if (plane != null) {
						airplanes.add(plane);
						System.out.println(airplanes.toString());
					}


				Iterator<Airplane> iterator = airplanes.iterator();

				while (iterator.hasNext()) {
					curPlane = iterator.next();
					if (!curPlane.hasFinished()) {
						if (curPlane.tryLand(timeTracker, airport)) {
							if (iterator.hasNext()) {
								curPlane = iterator.next();
								curPlane.setHead(true);
							}
						}
					}
				}

				Iterator<Airplane> iterator2 = airplanes.iterator();
				allPlanesFinished = true;
				while (iterator2.hasNext()) { // Check all planes to see if they were successful
					curPlane = iterator2.next();
					if (!curPlane.hasFinished()) {
						allPlanesFinished = false;
					} 
				}
			}

			//System.out.println(airplanes);
			Output output = new Output(airplanes);
			if (allPlanesFinished) {
				output.runPossible(curPlane.getCaseID(), timeTracker);
			} else if (!allPlanesFinished) {
				output.runImpossible(curPlane.getCaseID());
			}
		}
	}
}