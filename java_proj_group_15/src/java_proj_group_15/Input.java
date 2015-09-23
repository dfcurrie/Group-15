package java_proj_group_15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Input extends Thread {

	private ArrayList<Airplane> airplanes;

	int numOfPlanes = 0;

	public int numOfPlanes() {
		return numOfPlanes;
	}

	@Override
	public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String curLine = null;
		airplanes = new ArrayList<Airplane>();
		int curTuple = 0;
		int ID = 0, fuel = 0, burnRate = 0, landTime = 0, taxiTime = 0, unloadTime = 0;
		System.out.println("Enter: CASE <caseNumber> to create a new plane");
		try {
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
						numOfPlanes = numOfPlanes + 1;
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
