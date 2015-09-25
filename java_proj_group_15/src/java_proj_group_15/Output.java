package java_proj_group_15;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
		Airplane airplane = airplanes.get(0);
		String toFileP = "CASE " + caseID + ": POSSIBLE\n";
		toFileP = toFileP + airplane.getID() + " LANDED AT " + airplane.getEndTime() + " WITH " + airplane.calcFuel(timeTracker)
				+ " REMAINING.\n\n";

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
		Airplane airplane = airplanes.get(0);
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
