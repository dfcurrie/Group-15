package java_proj_group_15;

import java.util.Iterator;

public class Airplane {

	private int ID, fuel, burnRate, landTime, taxiTime, unloadTime, arrivalTime, runwayStartTime, endTime;
	private boolean hasFinished = false, isRunning = true;
	private Runway runway = null;
	private Parking parking = null;

	public Airplane(int ID, int fuel, int burnRate, int landTime, int taxiTime,
			int unloadTime, int arrivalTime) {
		this.ID = ID;
		this.fuel = fuel;
		this.burnRate = burnRate;
		this.landTime = landTime;
		this.taxiTime = taxiTime;
		this.unloadTime = unloadTime;
		this.arrivalTime = arrivalTime;
	}

	public void tryLand(Time timeTracker, Airport airport) {
		boolean successLand = false;
		Iterator<Runway> runIterator = airport.getRunways().iterator();
		Iterator<Parking> parkIterator = airport.getParkings().iterator();
		Runway curRunway = null;
		Parking curPark = null;
		Runway clrRunway = null;

		int groundTime = getLandTime() + getTaxiTime() + timeTracker
				.getCurTime();
		int unloadTime = getUnloadTime();
		int bookingTime = unloadTime + groundTime;

		while (runIterator.hasNext()) { //checks if free runway
			curRunway = runIterator.next();
			if (curRunway.isOccupied() == false) {
				//				clrRunway = curRunway; //keeps track of free runway
				while (parkIterator.hasNext()) { //checks if free parking
					curPark = parkIterator.next();
					if (curPark.isReserved(groundTime, bookingTime) == false) {
						runway = curRunway;
						parking = curPark;
						curRunway.setOccupied(true); //occupies runway if can land
						successLand = true;
						System.out.println("WE MADE IT TO LINE 46");
						runwayStartTime = timeTracker.getCurTime();
						curPark.setReserved(groundTime, bookingTime);
					}
				}
			}
		}

		//update parking
		//System.out.println(successLand);
		//return successLand; //varuable used to check whether have to get 
		//new airplane to check
	}

	public int calcFuel(Time timeTracker) {
		int curFuel = 0;
		if (isRunning) {
		curFuel = fuel - ((timeTracker.getCurTime() - arrivalTime)
				* burnRate);
		//System.out.println(curFuel);
		}

		return curFuel;
	}

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

	@Override
	public String toString() {
		String toString = null;

		toString = new String("ID: " + ID + ", " + fuel + ", " + burnRate + ", "
				+ landTime + ", " + taxiTime + ", " + unloadTime);

		return toString;
	}

}
