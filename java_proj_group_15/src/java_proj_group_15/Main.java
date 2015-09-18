package java_proj_group_15;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
    	try {
	    	if (args[0].equals("-h")) {
	    		System.out.println("This is a short description of what this program does.");
	    		System.exit(1);
	    		
	    	} else if (args[0].equals("-f") && args[2].equals("-o")) {
	    		System.out.println("Input: " + args[1] + "\nOutput: " + args[3]);
	    		ArrayList<Airplane> airplanes = parseInput();
	    		System.out.println(airplanes.toString());
	    		
	    	} else {
	    		System.out.println("Invalid command line arguments\nProper usage: -f <input_file_name> -o <output_file_name>");
	    		System.exit(-1);
	    	}
	    		
	 
    	} catch (ArrayIndexOutOfBoundsException e) {
    		System.out.println("Invalid command line arguments\nProper usage: -f <input_file_name> -o <output_file_name>");
    		System.exit(-1);
    	}
    }
    
    
    public static ArrayList<Airplane> parseInput() {	// Returns an array of Airplane objects created from the input file   	
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	String curLine = null;
    	ArrayList<Airplane> airplanes = new ArrayList<Airplane>();
    	int curTuple = 0;
    	int ID = 0, fuel = 0, burnRate = 0, landTime = 0, taxiTime = 0, unloadTime = 0;
    
    	try {
			while ((curLine = reader.readLine()) != null) {
				if (curLine.startsWith("CASE")) {

					ID = Integer.parseInt(curLine.substring(5));
					System.out.println("ID: " + ID);
				} else if (curLine.equals("END")){
					break;
				} else {
					switch (curTuple) {
					case 0:
						fuel = Integer.parseInt(curLine);
						System.out.println("Fuel: " + fuel);
						curTuple++;
						break;
					case 1:
						burnRate = Integer.parseInt(curLine);
						System.out.println("Burn Rate: " + burnRate);
						curTuple++;
						break;
					case 2:
						landTime = Integer.parseInt(curLine);
						System.out.println("Land Time: " + landTime);
						curTuple++;
						break;
					case 3:
						taxiTime = Integer.parseInt(curLine);
						System.out.println("Taxi Time: " + taxiTime);
						curTuple++;
						break;
					case 4:
						unloadTime = Integer.parseInt(curLine);
						System.out.println("Unload Time: " + unloadTime);
				    	airplanes.add(new Airplane(ID, fuel, burnRate, landTime, taxiTime, unloadTime));	
				    	System.out.println("New Airplane created: " + ID + fuel + burnRate + landTime + taxiTime + unloadTime);
						curTuple = 0;
						break;
					}
				}
					
			}
		} catch (IOException e) {
			System.out.println("Invalid input file");
			System.exit(-1);
		}
    	
    	return airplanes;
    }
}
