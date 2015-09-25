package java_proj_group_15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/*
This class gets the user input from the command line
and helps set up the scenario by creating airplanes
based on input and then calls the canLand() method in airport
to run the scenario
*/

public class Input extends Thread {

	private ArrayList<Airplane> airplanes;
	private Time timeTracker;
	private Airport airport;

	//Constructor for Input
	public Input(Time timeTracker, Airport airport) {
		this.timeTracker = timeTracker;
		this.airport = airport;
	}

	//Run program to get Input from the user which should be in format:
	//	CASE #: 	<enter>
	//	(#,#,#,#,#,#) 	<enter>
	//	...		<enter>
	//	END		<enter>
	@Override
	public void run() {
		//Create reader object to read user input
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		//Initialize variables for creating airplanes
		String curLine = null;
		airplanes = new ArrayList<Airplane>();
		int ID = 0, fuel = 0, burnRate = 0, landTime = 0, taxiTime = 0,
				unloadTime = 0, caseID = 0;
				
		System.out.println("Enter: CASE <caseNumber>: to create a new plane");
		
		//Attempt to read user input
		try {
			while ((curLine = reader.readLine()) != "END") {
				//Create a new scenario if CASE typed. Get Case number from input.
				// clear list of airplanes and reset time for the new scenario
				if (curLine.startsWith("CASE")) {
					curLine = curLine.substring(0, curLine.length()-1);
					caseID = Integer.parseInt(curLine.substring(5));
					System.out.println("CASE ID: " + caseID);
					timeTracker.resetTime(); // Time starts at 0 when Case is entered
					airplanes.clear();
					
					Iterator<Airplane> iterator = airplanes.iterator();
					Airplane curPlane = null;
					while(iterator.hasNext()) {
						curPlane = iterator.next();
						curPlane.getParking().getBookings().clear();
					}

				//Start scenario if enter is pressed with no input
				//  creates an output
				} else if (curLine.isEmpty()) {
					// End of scenario, check if can land here
					Output output = new Output(getAirplanes());

					//Calculate landing scenarios based on current time
					//if (airport.canLand(getAirplanes(), timeTracker) == true) {
					Iterator<Airplane> iterator = airplanes.iterator();
					Airplane curPlane = null;
					boolean allPlanesFinished = true;
					while(iterator.hasNext()) {
						curPlane = iterator.next();
						if (!curPlane.hasFinished()) {
							allPlanesFinished = false;
						}
					}
					if (allPlanesFinished) {
						//Print output to screen and to outfile with airplane information for input.getAirplanes()
						System.out.println("Can land all planes");
						output.runPossible(caseID, timeTracker);
					} else {
						//Print impossible and exit
						System.out.println("Can not land all planes");
						output.runImpossible(caseID);
					}
					
					
		//Add Comment here			
				} else if (curLine.startsWith("END")) {
					break;
				
				//Create airplane based on user input in format
				//	(ID,fuel,burnRate,landTime,taxiTime,unloadTime)
				} else {
					curLine = curLine.substring(1, curLine.length() - 1); // Remove ()

					List<String> planeInfo = Arrays.asList(curLine.split("\\s*,\\s*"));
					System.out.println(planeInfo.toString());

					ID = Integer.parseInt(planeInfo.get(0));
					fuel = Integer.parseInt(planeInfo.get(1));
					burnRate = Integer.parseInt(planeInfo.get(2));
					landTime = Integer.parseInt(planeInfo.get(3));
					taxiTime = Integer.parseInt(planeInfo.get(4));
					unloadTime = Integer.parseInt(planeInfo.get(5));

					//Create the airplane with the paramaters
					Airplane plane = new Airplane(ID, fuel, burnRate, landTime,
							taxiTime, unloadTime, timeTracker);
					airplanes.add(plane);
					//Print confirmation message of airplane creation
					System.out.println("New Airplane created: " + ID + fuel
							+ burnRate + landTime + taxiTime + unloadTime);
					plane.tryLand(timeTracker, airport);
				}
			}
		} catch (
			IOException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		System.out.println(airplanes);
		System.exit(1);
	}

	//Accessor method to get ArrayList of all created airplanes
	public ArrayList<Airplane> getAirplanes() {
		return airplanes;
	}
}
