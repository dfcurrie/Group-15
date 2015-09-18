package java_proj_group_15;

public class Airplane {
	
	private int ID, fuel, burnRate, landTime, taxiTime, unloadTime;

	public Airplane(int ID, int fuel, int burnRate, int landTime, int taxiTime, int unloadTime) {
		this.ID 		= ID;
		this.fuel 		= fuel;
		this.burnRate 	= burnRate;
		this.landTime 	= landTime;
		this.taxiTime 	= taxiTime;
		this.unloadTime = unloadTime;
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
	
	@Override
	public String toString() {
		String toString = null;
		
		toString = new String(ID + ", " + fuel +  ", " + burnRate + ", " + landTime + ", " + taxiTime + ", " + unloadTime);
		
		
		return toString;
		
		
	}

}
