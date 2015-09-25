package java_proj_group_15;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;

/*
This Class outputs the result of the scenario into the provided
outfile. 
*/
public class Output {
	private ArrayList<Airplane> airplanes;
	File file = new File(Main.outputFile);

	public Output(ArrayList<Airplane> airplanes) {
		this.airplanes = airplanes;
	}
	
	public Output() {
		
	}

	//Output the result if the scenario was successful
	public void runPossible(int caseID, Time timeTracker) {
		String toFileP = "CASE " + caseID + ": POSSIBLE\n";
		Airplane curPlane = null;
		Iterator<Airplane> iterator = airplanes.iterator();
		while (iterator.hasNext()) {
			curPlane = iterator.next();
			toFileP = toFileP + curPlane.getID() + " LANDED AT " + curPlane.getEndTime() + " WITH " + curPlane.calcFuel(timeTracker)
			+ " REMAINING.\n";
		}
		toFileP = toFileP + "\n";
		FileWriter fw;
		try {
			fw = new FileWriter(file, true);
			fw.write(toFileP);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Output the result if the scenario was unsuccessful
	public void runImpossible(int caseID) {
		String toFileI = "CASE " + caseID + ": IMPOSSIBLE\n\n";

		FileWriter fw;
		try {
			fw = new FileWriter(file, true);
			fw.write(toFileI);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
