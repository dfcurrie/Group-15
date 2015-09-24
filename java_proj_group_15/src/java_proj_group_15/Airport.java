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

	public boolean canLand(ArrayList<Airplane> airplanes, Time timeTracker) { // Checks all airplanes from airplane array and if one is unable to land, return false, else return true
		//		if (airplanes == null) {
		//			return false;
		//		}

		Iterator<Airplane> iterator = airplanes.iterator();
		Airplane curPlane = null;

		do {
			while (iterator.hasNext()) { // Check every plane in the airplanes list
				curPlane = iterator.next();

				curPlane.tryLand(timeTracker, this);
			}
			trackPlanes(airplanes, timeTracker);

			if (checkPlaneFuel(airplanes, timeTracker) == false) {
				return false;
			}
		}
		while (allFinished(airplanes) == false);

		//} while ( curPlane.tryLand(timeTracker, this) == false);

		//if (checkPlaneFuel(airplanes, timeTracker) == false) return false;

		return true;
	}

	private void trackPlanes(ArrayList<Airplane> airplanes, Time timeTracker) {
		Iterator<Airplane> iterator = airplanes.iterator();
		Airplane curPlane = null;

		while (iterator.hasNext()) { // Check every plane in the airplanes list
			curPlane = iterator.next();
			if (curPlane.getRunway() != null) {

				int travelTime;
				travelTime = timeTracker.getCurTime() - curPlane
						.getRunwayStartTime();

				if (travelTime >= curPlane.getLandTime() + curPlane
						.getTaxiTime() + curPlane.getUnloadTime()) {
					curPlane.setEndTime(timeTracker.getCurTime());
					curPlane.getParking().setOccupied(false);
					curPlane.setHasFinished(true);

				} else if (travelTime >= curPlane.getLandTime() + curPlane
						.getTaxiTime()) {
					curPlane.setRunning(false);
					curPlane.getParking().setOccupied(true);

				} else if (travelTime >= curPlane.getLandTime()) {

					curPlane.getRunway().setOccupied(false);

				}
			}
		}
	}

	public boolean allFinished(ArrayList<Airplane> airplanes) {
		Iterator<Airplane> iterator = airplanes.iterator();
		Airplane curPlane = null;

		while (iterator.hasNext()) { // Check every plane in the airplanes list
			curPlane = iterator.next();
			if (curPlane.hasFinished() == false) {
				return false;
			}
		}
		return true;
	}

	public boolean checkPlaneFuel(ArrayList<Airplane> airplanes,
			Time timeTracker) {
		if (airplanes == null)
			return true;

		Iterator<Airplane> iterator = airplanes.iterator();
		while (iterator.hasNext()) {
			Airplane curPlane = iterator.next();
			if (curPlane.calcFuel(timeTracker) < 0 && curPlane.isRunning()) {
				System.out.println("No Fuel " + curPlane.calcFuel(timeTracker));
				return false; 
			}
		}
		return true;
	}

}
