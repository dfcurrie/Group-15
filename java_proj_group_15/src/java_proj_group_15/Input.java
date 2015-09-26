package java_proj_group_15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/*
This class gets the user input from the command line
and helps set up the scenario by creating airplanes
based on input and then calls the canLand() method in airport
to run the scenario
*/

public class Input extends Thread {

	private ArrayList<Airplane> airplanes;
	private Time timeTracker;
	private int caseID = 0;
	private boolean endScenario = false;
	private boolean killScenario = false;
	private BlockingQueue<Airplane> queue;

	//Constructor for Input
	public Input(Time timeTracker, BlockingQueue<Airplane> queue) {
		this.timeTracker = timeTracker;
		this.queue = queue;
	}

	//Run program to get Input from the user which should be in format:
	//	CASE #: 	<enter>`
	//	(#,#,#,#,#,#) 	<enter>
	//	...		<enter>
	//	END		<enter>
	@Override
	public void run() {
		//Create reader object to read user input
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		//Initialize variables for creating airplanes
		String curLine = null;
		airplanes = new ArrayList<Airplane>();
		int ID = 0, fuel = 0, burnRate = 0, landTime = 0, taxiTime = 0,
				unloadTime = 0;

		System.out.println("Enter: CASE <caseNumber>: to start a new scenario");

		//Attempt to read user input
		try {
			while ((curLine = reader.readLine()) != "END") {
				//Create a new scenario if CASE typed. Get Case number from input.
				// clear list of airplanes and reset time for the new scenario
				if (curLine.startsWith("CASE")) {
					curLine = curLine.substring(0, curLine.length() - 1);
					caseID = Integer.parseInt(curLine.substring(5));
					System.out.println("CASE ID: " + caseID);


					//Start scenario if enter is pressed with no input
					//  creates an output
				} else if (curLine.isEmpty()) {
					endScenario = true;
					
				} else if (curLine.startsWith("END")) {
					killScenario = true;
					break;

					//Create airplane based on user input in format
					//	(ID,fuel,burnRate,landTime,taxiTime,unloadTime)
				} else {
					curLine = curLine.substring(1, curLine.length() - 1); // Remove ()

					List<String> planeInfo = Arrays.asList(curLine.split(
							"\\s*,\\s*"));
					//System.out.println(planeInfo.toString());

					ID = Integer.parseInt(planeInfo.get(0));
					fuel = Integer.parseInt(planeInfo.get(1));
					burnRate = Integer.parseInt(planeInfo.get(2));
					landTime = Integer.parseInt(planeInfo.get(3));
					taxiTime = Integer.parseInt(planeInfo.get(4));
					unloadTime = Integer.parseInt(planeInfo.get(5));

					//Create the airplane with the paramaters
					Airplane plane = new Airplane(ID, fuel, burnRate, landTime,
							taxiTime, unloadTime, caseID, timeTracker);
//					airplanes.add(plane);
					queue.put(plane);
					//System.out.println(airplanes);
					//Print confirmation message of airplane creation
					//					System.out.println("New Airplane created: " + ID + fuel
					//							+ burnRate + landTime + taxiTime + unloadTime);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(airplanes);
		System.exit(1);
	}

	//Accessor method to get ArrayList of all created airplanes
	public ArrayList<Airplane> getAirplanes() {
		return airplanes;
	}

	public int getCaseID() {
		return caseID;
	}

	public void setCaseID(int caseID) {
		this.caseID = caseID;
	}

	public boolean isEndScenario() {
		return endScenario;
	}

	public void setEndScenario(boolean endScenario) {
		this.endScenario = endScenario;
	}

	public boolean isKillScenario() {
		return killScenario;
	}

	public void setKillScenario(boolean killScenario) {
		this.killScenario = killScenario;
	}

}
