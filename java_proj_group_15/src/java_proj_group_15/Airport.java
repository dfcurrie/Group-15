package java_proj_group_15;

import java.util.ArrayList;

public class Airport {

	private ArrayList<Runway> runways;
	private ArrayList<Parking> parkings;

	public Airport(int numRunways, int numParkings) {
		for (int i = 0; i < numRunways; i++) {
			//runways.add(new Runway());
		}
		for (int i = 0; i < numParkings; i++) {
			//parkings.add(new Parking());
		}
	}

	public int getNumRunways() {
		return runways.size();
	}

	public int getNumParkings() {
		return parkings.size();
	}
	
	public ArrayList<Runway> getRunways() {
		return runways;
	}
	
	public ArrayList<Parking> getParkings() {
		return parkings;
	}
}
