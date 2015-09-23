package java_proj_group_15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.File;

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
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String curLine = null;
//		String toFile = "CASE:";
		airplanes = new ArrayList<Airplane>();
		int curTuple = 0;
		int ID = 0, fuel = 0, burnRate = 0, landTime = 0, taxiTime = 0, unloadTime = 0;
		System.out.println("Enter: CASE <caseNumber> to create a new plane");
		try {
//			File file = new File("outputFile"); //////Need to file a way to pass the outputfile given to
//			FileWriter fw = new FileWriter(file); /// input
			while ((curLine = reader.readLine()) != null) {
				if (curLine.startsWith("CASE")) {
					curLine = curLine.substring(0, curLine.length());
					ID = Integer.parseInt(curLine.substring(5));
					System.out.println("ID: " + ID);
				} else if (curLine.equals("END")) {
					break;
				} else {
					switch (curTuple) {
					case 0:
						fuel = Integer.parseInt(curLine);
						System.out.println("Fuel: " + fuel);
						curTuple++;
						break;
					case 1:
						burnRate = Integer.parseInt(curLine); // switched with
																// print line
						System.out.println("Burn Rate: " + burnRate); // added +
																		// burnRate
						curTuple++;
						break;
					case 2:
						landTime = Integer.parseInt(curLine);
						System.out.println("Land Time: " + landTime);
						curTuple++;
						break;
					case 3:
						taxiTime = Integer.parseInt(curLine);
						System.out.println("Taxi Time: " + taxiTime);
						curTuple++;
						break;
					case 4:
						unloadTime = Integer.parseInt(curLine);
						System.out.println("Unload Time: " + unloadTime);
						System.out.println("\nPress enter to create new plane");
						curTuple++;
						break;
					case 5:
						curTuple = 0;
						airplanes.add(new Airplane(ID, fuel, burnRate, landTime, taxiTime, unloadTime));
						System.out.println(
								"New Airplane created: " + ID + fuel + burnRate + landTime + taxiTime + unloadTime);
//						toFile = toFile +","+ ID +","+ fuel +","+ burnRate +","+ landTime +","+ taxiTime +","+ unloadTime;
//						fw.write(toFile);
//						fw.close();
						// End of scenario, check if can land here
						if (airport.canLand(getAirplanes(), timeTracker.getCurTime()) == true) { // Calculate landing scenarios based on current time
							// Print output to screen and file with airplane information for input.getAirplanes()
							System.out.println("Can land all planes");
						} else {
							// Print impossible and exit
							System.out.println("Can not land all planes");
						}
						
						System.out.println(
								"Enter CASE <caseNumber> to create a new plane or END to stop creating new planes");
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		System.out.println(airplanes);
	}

	public ArrayList<Airplane> getAirplanes() {
		return airplanes;
	}

}
