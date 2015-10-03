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
				System.out.println();
				System.exit(1);

				// Run the program on a file containing variables of state
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
		Reader reader = new Reader(System.getProperty("user.dir") + "/"
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

			//System.out.println(airplanes); //debug
			Output output = new Output(airplanes);
			if (allPlanesFinished) {
				output.runPossible(curPlane.getCaseID(), timeTracker);
			} else if (!allPlanesFinished) {
				output.runImpossible(curPlane.getCaseID());
			}
		}
	}
}
