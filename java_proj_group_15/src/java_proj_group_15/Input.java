package java_proj_group_15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Input extends Thread {

	private ArrayList<Airplane> airplanes;
	private Time timeTracker;
	private Airport airport;

	public Input(Time timeTracker, Airport airport) {
		this.timeTracker = timeTracker;
		this.airport = airport;
	}

	@Override
	public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		String curLine = null;
		airplanes = new ArrayList<Airplane>();
		int ID = 0, fuel = 0, burnRate = 0, landTime = 0, taxiTime = 0,
				unloadTime = 0, caseID = 0;
		System.out.println("Enter: CASE <caseNumber> to create a new plane");
		try {
			while ((curLine = reader.readLine()) != "END") {
				if (curLine.startsWith("CASE")) {
					curLine = curLine.substring(0, curLine.length());
					caseID = Integer.parseInt(curLine.substring(5));
					System.out.println("CASE ID: " + caseID);
					timeTracker.resetTime(); // Time starts at 0 when Case is entered

				} else if (curLine.isEmpty()) {
					// End of scenario, check if can land here
					Output output = new Output(getAirplanes());

					if (airport.canLand(getAirplanes(), timeTracker
							.getCurTime()) == true) { // Calculate landing scenarios based on current time
						// Print output to screen and file with airplane information for input.getAirplanes()
						System.out.println("Can land all planes");
						output.runPossible();
					} else {
						// Print impossible and exit
						System.out.println("Can not land all planes");
						output.runImpossible();
					}
				} else if (curLine.startsWith("END")) {
					break;
				} else {
					curLine = curLine.substring(1, curLine.length() - 1); // Remove ()

					List<String> planeInfo = Arrays.asList(curLine.split(
							"\\s*,\\s*"));
					System.out.println(planeInfo.toString());

					ID = Integer.parseInt(planeInfo.get(0));
					fuel = Integer.parseInt(planeInfo.get(1));
					burnRate = Integer.parseInt(planeInfo.get(2));
					landTime = Integer.parseInt(planeInfo.get(3));
					taxiTime = Integer.parseInt(planeInfo.get(4));
					unloadTime = Integer.parseInt(planeInfo.get(5));

					airplanes.add(new Airplane(ID, fuel, burnRate, landTime,
							taxiTime, unloadTime));
					System.out.println("New Airplane created: " + ID + fuel
							+ burnRate + landTime + taxiTime + unloadTime);

				}
			}

		} catch (

		IOException e)

		{
			e.printStackTrace();
			System.exit(-1);
		}

		System.out.println(airplanes);

	}

	public ArrayList<Airplane> getAirplanes() {
		return airplanes;
	}

}
