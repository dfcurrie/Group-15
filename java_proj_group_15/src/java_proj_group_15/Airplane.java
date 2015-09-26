package java_proj_group_15;

import java.util.Iterator;

/*
This Class acts as the airplane and checks if it can land itself on the runway
*/

public class Airplane {

	private int ID, fuel, burnRate, landTime, taxiTime, unloadTime, arrivalTime,
			runwayStartTime, endTime, curFuel = fuel, caseID;
	private boolean hasFinished = false, isRunning = true, hasLanded = false, head = false;
	private Runway runway = null;
	private Parking parking = null;
	private Time timeTracker;
	private String planeLoc;

	//Create an airplane based on what Input got from the user
	public Airplane(int ID, int fuel, int burnRate, int landTime, int taxiTime,
			int unloadTime, int caseID, Time timeTracker) {
		this.ID = ID;
		this.fuel = fuel;
		this.burnRate = burnRate;
		this.landTime = landTime;
		this.taxiTime = taxiTime;
		this.unloadTime = unloadTime;
		this.arrivalTime = timeTracker.getCurTime();
		this.timeTracker = timeTracker;
		this.caseID = caseID;
	}

	//Attempt to land on a runway.
	//Look to see if there is an open runway and parking spot in the future
	public boolean tryLand(Time timeTracker, Airport airport) {
		Boolean successLand = false;
		
		//Check to see if the plane has already landed or lacks required fuel
		if (!hasFinished && calcFuel(timeTracker) >= calcNeededFuel()) {
			
			if (!hasLanded) { // If plane is in the air
				int groundTime = getLandTime() + getTaxiTime() + timeTracker
						.getCurTime();
				int unloadTime = getUnloadTime();
				int bookingTime = unloadTime + groundTime;

				Iterator<Runway> runIterator = airport.getRunways().iterator();
				Iterator<Parking> parkIterator = airport.getParkings()
						.iterator();
				Runway curRunway = null;
				Parking curPark = null;

				while (runIterator.hasNext()) {
					curRunway = runIterator.next();
					if (!curRunway.isOccupied()) {
						while (parkIterator.hasNext()) {
							curPark = parkIterator.next();
							if (!curPark.isReserved(groundTime)) {
								curRunway.setOccupied(true);
								curPark.setReserved(groundTime, bookingTime);
								hasLanded = true;
								parking = curPark;
								runway = curRunway;
								runwayStartTime = timeTracker.getCurTime();
								successLand = true;
								break;
							}
						}
						break;
					}
				}

			}

		} 
		if (hasLanded) {
			planeLoc = getPlaneLoc();
		}
		return successLand;
	}
	

	//Calculate how much fuel left in plane based on how much time plane has been "active" for
	//Only needs to check if fuel needs to be consumed (not in parking)
	public int calcFuel(Time timeTracker) {
		if (isRunning) {
			curFuel = fuel - ((timeTracker.getCurTime() - arrivalTime)
					* burnRate);
			//System.out.println(getID() + " " + curFuel); //debug
		}

		return curFuel;
	}

	public int calcNeededFuel() {
		int neededFuel = (landTime + taxiTime) * burnRate;

		return neededFuel;
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
					+ ": Clear Parking -> Unload(Complete) | Fuel: " + calcFuel(
							timeTracker));
			setEndTime(timeTracker.getCurTime());
			return "Finished";

			//Case where plane has been on gorund long enough to get to parking
			//Set parking to occupied
		} else if (travelTime >= getLandTime() + getTaxiTime()) {
			setRunning(false);
			getRunway().setOccupied(false);
			getParking().setOccupied(true);
//			System.out.println(getID() + ": Clear Taxi -> Parking | Fuel: "	//debug
//					+ calcFuel(timeTracker));								//debug
			return "Parking";

			//Case where plane has been on ground long enough to clear runway
			//Set runway to Unoccupied
		} else if (travelTime >= getLandTime()) {
//			System.out.println(getID() + ": Clear Airspace -> Runway | Fuel: "	//debug
//					+ calcFuel(timeTracker));									//debug
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

	public int getCaseID() {
		return caseID;
	}

	public void setCaseID(int caseID) {
		this.caseID = caseID;
	}

	public boolean isHead() {
		return head;
	}

	public void setHead(boolean head) {
		this.head = head;
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
