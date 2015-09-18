package java_proj_group_15;

public class Main {

    public static void main(String[] args) {
    	try {
	    	if (args[0].equals("-h")) {
	    		System.out.println("This is a short description of what this program does.");
	    		System.exit(1);
	    		
	    	} else if (args[0].equals("-f") && args[2].equals("-o")) {
	    		System.out.println("Input: " + args[1] + "\nOutput: " + args[3]);
	    		
	    	} else {
	    		System.out.println("Invalid command line arguments\nProper usage: -f <input_file_name> -o <output_file_name>");
	    		System.exit(-1);
	    	}
	    		
	 
    	} catch (ArrayIndexOutOfBoundsException e) {
    		System.out.println("Invalid command line arguments\nProper usage: -f <input_file_name> -o <output_file_name>");
    		System.exit(-1);
    	}
    }

}
