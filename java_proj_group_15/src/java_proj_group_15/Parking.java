package java_proj_group_15;

import java.util.ArrayList;
import java.util.List;

public class Parking {

	private boolean isOccupied = false;
	
	List<Integer> bookings = new ArrayList<Integer>();

	public boolean isReserved(int startTime, int endTime) {
		for (int i = startTime; i < endTime; i++)
			if (bookings.contains(i)) {
				return false;
			}
		
		return true;

	}

	public void setReserved(int startTime, int endTime) {
		for (int i = startTime; i < endTime; i++) {
			bookings.add(i);
		}
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

}
