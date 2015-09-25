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

	// Checks all airplanes from airplane array and if one is unable to land, return false, else return true
	public boolean canLand(ArrayList<Airplane> airplanes, Time timeTracker) {
		Iterator<Airplane> iterator = airplanes.iterator();
		Airplane curPlane = null;

		if (iterator.hasNext()) {
			curPlane = iterator.next();
			while (checkPlaneFuel(airplanes, timeTracker) && !allFinished(
					airplanes)) {
				if (curPlane.getRunway() == null) {
					if (curPlane.tryLand(timeTracker, this)) {
						if (iterator.hasNext()) {
							curPlane = iterator.next();
						}
					}
				}
				trackPlanes(airplanes, timeTracker);
			}
		}
		if (allFinished(airplanes)) {
			return true;
		} else {
			return false;
		}
		//		//		if (airplanes == null) {
		//		//			return false;
		//		//		}
		//
		//		Iterator<Airplane> iterator = airplanes.iterator();
		//		Airplane curPlane = null;
		//
		//		//Continues to try and land, progress, and check planes' fuel to see if situation can be successful
		//		//while there are still airplanes that have not finished unloading
		//		//If all have, return true
		//		do {
		//			while (iterator.hasNext()) {
		//				curPlane = iterator.next();
		//				
		//	//Might want to consider having tryLand return true or false so it knows whether to attempt to land further planes
		//	//else situation might happen where third airplane will try to go before second.
		//	//or alternatively maybe make tryLand recursive? might cause own issues
		//				curPlane.tryLand(timeTracker, this);
		//			}
		//			//Check where planes are and move them if need be
		//			trackPlanes(airplanes, timeTracker);
		//			//Return false if any plane ran out of fuel (scenario did not work)
		//			if (checkPlaneFuel(airplanes, timeTracker) == false) {
		//				return false;
		//			}
		//		}
		//		while (allFinished(airplanes) == false);
		//		
		//		return true;
	}

	//Determine if the airplaes need to be moved based on how long they've been on the ground
	private void trackPlanes(ArrayList<Airplane> airplanes, Time timeTracker) {
		Iterator<Airplane> iterator = airplanes.iterator();
		Airplane curPlane = null;

		//System.out.println(airplanes.toString());
		//Check every plane in the airplanes list
		while (iterator.hasNext()) {
			curPlane = iterator.next();
			if (curPlane.isHasLanded()) {

				//			System.out.println(curPlane.getID());
				//			System.out.println("Land Time: " + curPlane.getLandTime());
				//			System.out.println("Start Time: " + curPlane.getRunwayStartTime());
				//Determine how long airplane has been on ground
				int travelTime = timeTracker.getCurTime() - curPlane
						.getRunwayStartTime();

				//Case where plane has been on ground long enough to finish
				//runway, taki, and unloading. Set hasFinished to true and remove plane from parking
				if (travelTime >= curPlane.getLandTime() + curPlane
						.getTaxiTime() + curPlane.getUnloadTime()) {
					curPlane.setEndTime(timeTracker.getCurTime());
					curPlane.getParking().setOccupied(false);
					curPlane.setHasFinished(true);
					System.out.println(curPlane.getID()
							+ ": Clear Parking -> Unload(Complete) | Fuel: "
							+ curPlane.getCurFuel());

					//Case where plane has been on gorund long enough to get to parking
					//Set parking to occupied
				} else if (travelTime >= curPlane.getLandTime() + curPlane
						.getTaxiTime()) {
					curPlane.setRunning(false);
					curPlane.getParking().setOccupied(true);
					System.out.println(curPlane.getID()
							+ ": Clear Taxi -> Parking | Fuel: " + curPlane
									.getCurFuel());

					//Case where plane has been on ground long enough to clear runway
					//Set runway to Unoccupied
				} else if (travelTime >= curPlane.getLandTime()) {
					curPlane.getRunway().setOccupied(false);
					System.out.println(curPlane.getID()
							+ ": Clear Runway -> Taxi | Fuel: " + curPlane
									.getCurFuel());
				}
			}
		}
	}

	//Check if all planes in the scenario have finished unloading
	public boolean allFinished(ArrayList<Airplane> airplanes) {
		Iterator<Airplane> iterator = airplanes.iterator();
		Airplane curPlane = null;

		//Check every plane in the airplanes list
		while (iterator.hasNext()) {
			curPlane = iterator.next();
			if (curPlane.hasFinished() == false) {
				return false;
			}
		}
		return true;
	}

	//Check if plane has fuel to move
	//MIGHT WANT TO CHANGE FUNCTIONS THAT USE THIS TO USE calcFuel INSTEAD
	public boolean checkPlaneFuel(ArrayList<Airplane> airplanes,
			Time timeTracker) {
		if (airplanes == null)
			return true;

		Iterator<Airplane> iterator = airplanes.iterator();
		while (iterator.hasNext()) {
			Airplane curPlane = iterator.next();
			if (curPlane.calcFuel(timeTracker) < 0 && curPlane.isRunning()) {
				System.out.println("No Fuel: " + curPlane.getID());
				return false;
			}
		}
		return true;
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
