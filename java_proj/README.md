cpsc449fall15 Group-15 Java Assignment Rayce Rossum Rayce.Rossum@gmail.com Caleb Steele-Lane csteele@ucalgary.ca David Currie dfcurrie@ucalgary.ca Joshua Hong johong@ucalgary.ca

Design Decisions
For the design of our program we used a producer consumer model to allow information in our program to cross threads.
As well our objects were made to reflect real world functionality of a airport.


To Compile enter into the command line ant compile

How to run: This program runs from command line to run it you must input into the command line like this 
java Main -f <input_file_name> -o <output_file_name>
where the input_file_name contains the information R=(number of runways) P=(number of parking spaces) T=(time units)
and output_file_name is the name of the file where you want the information outputted
or if you want information about the program java Main -h