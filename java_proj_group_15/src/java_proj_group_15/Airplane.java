package java_proj_group_15;

import java.util.Iterator;

/*
This Class acts as the airplane and checks if it can land itself on the runway
*/

public class Airplane {

	private int ID, fuel, burnRate, landTime, taxiTime, unloadTime, arrivalTime,
			runwayStartTime, endTime, curFuel = fuel;
	private boolean hasFinished = false, isRunning = true, hasLanded = false;
	private Runway runway = null;
	private Parking parking = null;
	private Time timeTracker;

	//Create an airplane based on what Input got from the user
	public Airplane(int ID, int fuel, int burnRate, int landTime, int taxiTime,
			int unloadTime, Time timeTracker) {
		this.ID = ID;
		this.fuel = fuel;
		this.burnRate = burnRate;
		this.landTime = landTime;
		this.taxiTime = taxiTime;
		this.unloadTime = unloadTime;
		this.arrivalTime = timeTracker.getCurTime();
		this.timeTracker = timeTracker;
	}

	//Attempt to land on a runway.
	//Look to see if there is an open runway and parking spot in the future
	public boolean tryLand(Time timeTracker, Airport airport) {
		boolean successLand = false;
		Iterator<Runway> runIterator = airport.getRunways().iterator();
		Iterator<Parking> parkIterator = airport.getParkings().iterator();
		Runway curRunway = null;
		Parking curPark = null;

		//Variable for: 	time when moving on ground
		//			time to unload airplane
		//			time when airplane requires parking
		int groundTime = getLandTime() + getTaxiTime() + timeTracker
				.getCurTime();
		int unloadTime = getUnloadTime();
		int bookingTime = unloadTime + groundTime;

		//Iterate through list of runways to see if there is a free runway
		while (runIterator.hasNext()) {
			curRunway = runIterator.next();
			//Case when there is a free runway
			if (curRunway.isOccupied() == false) {
				//Iterate through list of parking to see if there will be free parking
				while (parkIterator.hasNext()) {
					curPark = parkIterator.next();

					//Case when there is free runway and parking (can land)

					//Update which runway airplane is using and which parking
					//Set the runway to occupied, make note of curTime and make reservation to parking
					//Let know that next plane can attempt landing
					if (curPark.isReserved(groundTime) == false) {
						runway = curRunway;
						parking = curPark;
						curRunway.setOccupied(true);
						runwayStartTime = timeTracker.getCurTime();
						curPark.setReserved(groundTime, bookingTime);
						successLand = true;
						hasLanded = true;
						break;
					}
				}
				break;
			}
		}
		return successLand;
	}

	//Calculate how much fuel left in plane based on ow much time plane has been "active" for
	//Only needs to check if fuel needs to be consumed (not in parking)
	public int calcFuel(Time timeTracker) {
		if (isRunning) {
			curFuel = fuel - ((timeTracker.getCurTime() - arrivalTime)
					* burnRate);
			//System.out.println(getID() + " " + curFuel);
		}

		return curFuel;
	}

	//	private void trackPlanes(ArrayList<Airplane> airplanes, Time timeTracker) {
	public String getPlaneLoc() {
		//Determine how long airplane has been on ground
		int travelTime = timeTracker.getCurTime() - getRunwayStartTime();

		//Case where plane has been on ground long enough to finish
		//runway, taki, and unloading. Set hasFinished to true and remove plane from parking
		if (travelTime >= getLandTime() + getTaxiTime() + getUnloadTime()) {
			setEndTime(timeTracker.getCurTime());
			getParking().setOccupied(false);
			setHasFinished(true);
			System.out.println(getID()
					+ ": Clear Parking -> Unload(Complete) | Fuel: "
					+ getCurFuel());
			return "Finished";

			//Case where plane has been on gorund long enough to get to parking
			//Set parking to occupied
		} else if (travelTime >= getLandTime() + getTaxiTime()) {
			setRunning(false);
			getParking().setOccupied(true);
			System.out.println(getID() + ": Clear Taxi -> Parking | Fuel: "
					+ getCurFuel());
			return "Parking";

			//Case where plane has been on ground long enough to clear runway
			//Set runway to Unoccupied
		} else if (travelTime >= getLandTime()) {
			getRunway().setOccupied(false);
			System.out.println(getID() + ": Clear Runway -> Taxi | Fuel: "
					+ getCurFuel());
			return "Runway";
		} else {
			return null;
		}
	}

	//------------------------------------------------------------------------------------------------------------
	//			ACCESSOR AND MUTATOR METHODS FOR VARIABLES IN AIRPLANE
	//------------------------------------------------------------------------------------------------------------		
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getFuel() {
		return fuel;
	}

	public void setFuel(int fuel) {
		this.fuel = fuel;
	}

	public int getBurnRate() {
		return burnRate;
	}

	public void setBurnRate(int burnRate) {
		this.burnRate = burnRate;
	}

	public int getLandTime() {
		return landTime;
	}

	public void setLandTime(int landTime) {
		this.landTime = landTime;
	}

	public int getTaxiTime() {
		return taxiTime;
	}

	public void setTaxiTime(int taxiTime) {
		this.taxiTime = taxiTime;
	}

	public int getUnloadTime() {
		return unloadTime;
	}

	public void setUnloadTime(int unloadTime) {
		this.unloadTime = unloadTime;
	}

	public boolean hasFinished() {
		return hasFinished;
	}

	public void setHasFinished(boolean hasFinished) {
		this.hasFinished = hasFinished;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public int getRunwayStartTime() {
		return runwayStartTime;
	}

	public void setRunwayStartTime(int runwayStartTime) {
		this.runwayStartTime = runwayStartTime;
	}

	public Runway getRunway() {
		return runway;
	}

	public void setRunway(Runway runway) {
		this.runway = runway;
	}

	public Parking getParking() {
		return parking;
	}

	public void setParking(Parking parking) {
		this.parking = parking;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getCurFuel() {
		return curFuel;
	}

	public void setCurFuel(int curFuel) {
		this.curFuel = curFuel;
	}

	public boolean isHasLanded() {
		return hasLanded;
	}

	public void setHasLanded(boolean hasLanded) {
		this.hasLanded = hasLanded;
	}

	//Override toString to more efficiently display an airplane object
	@Override
	public String toString() {
		String toString = null;

		toString = new String("ID: " + ID + ", " + fuel + ", " + burnRate + ", "
				+ landTime + ", " + taxiTime + ", " + unloadTime);

		return toString;
	}
}
