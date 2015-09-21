package java_proj_group_15;

public class Airport {

	private int runways, parkings;
	
	public Airport(int runways, int parkings) {
		this.runways = runways;
		this.parkings = parkings;
	}

	public int getRunways() {
		return runways;
	}

	public void setRunways(int runways) {
		this.runways = runways;
	}

	public int getParkings() {
		return parkings;
	}

	public void setParkings(int parkings) {
		this.parkings = parkings;
	}
}
