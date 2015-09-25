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

//	// Checks all airplanes from airplane array and if one is unable to land, return false, else return true
//	public boolean canLand(ArrayList<Airplane> airplanes, Time timeTracker) {
//		Iterator<Airplane> iterator = airplanes.iterator();
//		Airplane curPlane = null;
//
//		if (iterator.hasNext()) {
//			curPlane = iterator.next();
//			while (checkPlaneFuel(airplanes, timeTracker) && !allFinished(
//					airplanes)) {
//				if (curPlane.getRunway() == null) {
//					if (curPlane.tryLand(timeTracker, this)) {
//						if (iterator.hasNext()) {
//							curPlane = iterator.next();
//						}
//					}
//				}
//				trackPlanes(airplanes, timeTracker);
//			}
//		}
//		if (allFinished(airplanes)) {
//			return true;
//		} else {
//			return false;
//		}
//
//	}
//
//
//	//Check if all planes in the scenario have finished unloading
//	public boolean allFinished(ArrayList<Airplane> airplanes) {
//		Iterator<Airplane> iterator = airplanes.iterator();
//		Airplane curPlane = null;
//
//		//Check every plane in the airplanes list
//		while (iterator.hasNext()) {
//			curPlane = iterator.next();
//			if (curPlane.hasFinished() == false) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	//Check if plane has fuel to move
//	public boolean checkPlaneFuel(ArrayList<Airplane> airplanes,
//			Time timeTracker) {
//		if (airplanes == null)
//			return true;
//
//		Iterator<Airplane> iterator = airplanes.iterator();
//		while (iterator.hasNext()) {
//			Airplane curPlane = iterator.next();
//			if (curPlane.calcFuel(timeTracker) < 0 && curPlane.isRunning()) {
//				System.out.println("No Fuel: " + curPlane.getID());
//				return false;
//			}
//		}
//		return true;
//	}

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
