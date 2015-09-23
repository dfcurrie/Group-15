package java_proj_group_15;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

public class Output {
	private ArrayList<Airplane> airplanes;
	File file = new File("outputFile");

	public Output(ArrayList<Airplane> airplanes) {
		this.airplanes = airplanes;
	}

	public void runPossible() {
		Airplane airplane = airplanes.get(0);
		String toFileP = "CASE " + airplane.getID() + ": POSSIBLE\n";
		toFileP = toFileP + airplane.getID() + " LANDED AT " + airplane.getLandTime() + " WITH " + airplane.getFuel()
				+ " REMAINING.\n\n";

		FileWriter fw;
		try {
			fw = new FileWriter(file, true);
			fw.write(toFileP);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void runImpossible() {
		Airplane airplane = airplanes.get(0);
		String toFileI = "CASE " + airplane.getID() + ": IMPOSSIBLE\n\n";

		FileWriter fw;
		try {
			fw = new FileWriter(file, true);
			fw.write(toFileI);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
