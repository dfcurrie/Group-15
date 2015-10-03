package java_proj_group_15;

import java.util.ArrayList;
import java.util.List;

/*
This Class acts as the parking for airplanes
*/

public class Parking {

	private boolean isOccupied = false;

	List<Integer> bookings = new ArrayList<Integer>();

	//Check if plane will be able to go into parking for provided time
	public boolean isReserved(int startTime) {

		if (bookings.contains(startTime)) {
			return true;
		}

		return false;
	}

	//Set the reservation time
	public void setReserved(int startTime, int endTime) {
		for (int i = startTime; i < endTime; i++) {
			bookings.add(i);
		}
	}

	//Accessor for isOccupied
	public boolean isOccupied() {
		return isOccupied;
	}

	//Mutator for isOccupied
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public List<Integer> getBookings() {
		return bookings;
	}

	public void setBookings(List<Integer> bookings) {
		this.bookings = bookings;
	}

}
