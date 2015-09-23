package java_proj_group_15;

import java.util.ArrayList;
import java.util.Iterator;

public class Airport {

	private ArrayList<Runway> runways = new ArrayList<Runway>();
	private ArrayList<Parking> parkings = new ArrayList<Parking>();

	public Airport(int numRunways, int numParkings) {
		for (int i = 0; i < numRunways; i++) {
			runways.add(new Runway());
		}
		for (int i = 0; i < numParkings; i++) {
			parkings.add(new Parking());
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

	public boolean canLand(ArrayList<Airplane> airplanes, int curTime) { // Checks all airplanes from airplane array and if one is unable to land, return false, else return true
		if (airplanes == null) {
			return false;
		}

		Iterator<Airplane> iterator = airplanes.iterator();
		Airplane curPlane = null;

		while (iterator.hasNext()) { // Check every plane in the airplanes list
			curPlane = iterator.next();
			if (curPlane.getFuel() < 1) { // Check all cases where a plane could not land i.e. has no fuel
				return false;
			}
			if (tryLand(curPlane) == false){
				return false;
			}
			//			else {
			//while (tryLand(curPlane) == false) {
		}
		//			}
		//}

		//		if (airplanes.size() > getNumRunways()) {
		//			System.out.println("Too few runways for all planes");
		//			return false;
		//		} else if (airplanes.size() > getNumParkings()) {
		//			System.out.println("Too few parking spots");
		//			return false;
		//		} 

		return true;
	}

	public boolean tryLand(Airplane curPLane) {
		boolean successLand = false;
		Iterator<Runway> runIterator = runways.iterator();
		Iterator<Parking> parkIterator = parkings.iterator();
		Runway curRunway = null;
		Parking curPark = null;
		Runway clrRunway = null;

		while (runIterator.hasNext()) { //checks if free runway
			curRunway = runIterator.next();
			if (curRunway.isOccupied() == false) {
				clrRunway = curRunway; //keeps track of free runway
			}
		}
		while (parkIterator.hasNext() && clrRunway != null) { //checks if free parking
			curPark = parkIterator.next();
			if (curPark.isOccupied() == false) {
				clrRunway.setOccupied(true); //occupies runway if can land
				successLand = true;
			}
		}
		//increase time
		//update runways
		//update parking
		return successLand; //varuable used to check whether have to get 
		//new airplane to check
	}
}
