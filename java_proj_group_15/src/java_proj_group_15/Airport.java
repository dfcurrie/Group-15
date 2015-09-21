package java_proj_group_15;

public class Airport {

	private Runway[] runways;
	private Parking[] parkings;
	
	public Airport(int numRunways, int numParkings) {
		for (int i = 0; i < numRunways; i++) {
			runways[i] = new Runway();
		}
		for (int i = 0; i < numParkings; i++) {
			parkings[i] = new Parking();
		}
	}

	public int getRunways() {
		return runways.length;
	}


	public int getParkings() {
		return parkings.length;
	}
}
