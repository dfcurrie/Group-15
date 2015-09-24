package java_proj_group_15;

import java.util.Iterator;

public class Airplane {

	private int ID, fuel, burnRate, landTime, taxiTime, unloadTime, arrivalTime;
	private boolean hasLanded = false, isRunning = true;
	private String status = "flight";

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

	public boolean tryLand(Time timeTracker, Airport airport) {
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
				clrRunway = curRunway; //keeps track of free runway
			}
		}

		while (parkIterator.hasNext() && clrRunway != null) { //checks if free parking
			curPark = parkIterator.next();
			if (curPark.isReserved(groundTime, unloadTime) == false) {
				clrRunway.setOccupied(true); //occupies runway if can land
				successLand = true;
				int runwayStartTime = timeTracker.getCurTime();
				curPark.setReserved(groundTime, bookingTime);
				setStatus("runway");
			}
		}
		
		//update parking
		return successLand; //varuable used to check whether have to get 
		//new airplane to check
	}

	public int calcFuel(Time timeTracker) {
		int curFuel = fuel - ((timeTracker.getCurTime() - arrivalTime)
				* burnRate);

		return curFuel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public boolean hasLanded() {
		return hasLanded;
	}

	public void setHasLanded(boolean hasLanded) {
		this.hasLanded = hasLanded;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	@Override
	public String toString() {
		String toString = null;

		toString = new String("ID: " + ID + ", " + fuel + ", " + burnRate + ", "
				+ landTime + ", " + taxiTime + ", " + unloadTime);

		return toString;
	}

}
