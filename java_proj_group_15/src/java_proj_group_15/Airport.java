package java_proj_group_15;

import java.util.ArrayList;
import java.util.Iterator;

/*
This Class acts as the airport and holds the runways and parking spots
Receives request to see if scenario will work in Input
*/

public class Airport {

	private ArrayList<Runway> runways = new ArrayList<Runway>();
	private ArrayList<Parking> parkings = new ArrayList<Parking>();

	//Create ArrayLists of runways and parking 
	public Airport(int numRunways, int numParkings) {
		for (int i = 0; i < numRunways; i++) {
			runways.add(new Runway());
		}
		for (int i = 0; i < numParkings; i++) {
			parkings.add(new Parking());
		}
	}

	//------------------------------------------------------------------------------------------------------------
	//			ACCESSOR AND MUTATOR METHODS FOR VARIABLES IN AIRPORT
	//------------------------------------------------------------------------------------------------------------	

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
