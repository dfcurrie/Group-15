import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;

/**
 * Program to test the Prolog programming assignment for CPSC 449 W2015.  
 *
 * <p><b>Title:</b> CASA Agent Infrastructure</p>
 * <p><b>Copyright: Copyright (c) 2003-2015, Knowledge Science Group, University of Calgary.</b> 
 * Permission to use, copy, modify, distribute and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice appear in all copies and that both that copyright
 * notice and this permission notice appear in supporting documentation.  
 * The  Knowledge Science Group makes no representations about the suitability
 * of  this software for any purpose.  It is provided "as is" without express
 * or implied warranty.</p>
 * <p><b>Company:</b> Knowledge Science Group, Department of Computer Science, University of Calgary</p>
 * @version 1
 * @author <a href="http://pages.cpsc.ucalgary.ca/~kremer/">Rob Kremer</a>
 *
 */
public class PrologTest {

	/** The prolog command */
	static final String PROLOG = "/usr/bin/gprolog";
	
	/** This program's version */
	static final String version = "4";

	/** The prolog prompt used by GNU Prolog. */
	static final String P_PROMPT = "\n| ?- ";
	
  /** This is the path where the prolog code (both the inference program and the data file) reside. */
	static String pprogPath = ".";

	/** This is the name of the inference program to test.  You can fill in this value to avoid the 
	 *  program prompting you for input when it starts. 
	 */
	static String testFileName = null;
	
	/** The current subprocess, where the test program is running. */
	ProcDesc curProc = null;
	
	/** Used by {@link #print(String)} and {@link #println(String)} for indenting. */
	int indentLevel = 0;
	/** Used by {@link #print(String)} and {@link #println(String)} for indenting. */
	boolean indent = true;
	
	/**
	 * Constructor.  Make sure the test file exists, then call all the tests, then report.
	 * @param testFileName The name of the inference file to test.
	 */
	public PrologTest(String testFileName) {
		File testFile = new File(testFileName);
		if (!testFile.exists()) {
			println("Can't find file: "+testFileName);
			System.exit(-1);
		}
		// The test file and data files should be in the same directory.
		String sep = System.getProperty("file.separator");
		if (testFileName.contains(sep)) {
			pprogPath = testFileName.substring(0,testFileName.lastIndexOf(sep));
		}
		
		for (Test t: tests) {
			if (t.name.equals("related(X,Y)."))
				System.out.println("here.");
			t.run();
		}
		stopProc();
		report(tests);
	}
	
	/**
	 * The main program.  Checks for a filename (to test) argument and prompts for one if
	 * it isn't on the command line. Then passes control to the class constructor.
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("CPSC 449 W15 Prolog test program, version "+version+".");
		if (args.length>0 && args[0]!=null && args[0].length()>0) {
			testFileName = args[0];
		}

		if (testFileName==null) {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			try {
				if (testFileName==null) {
					System.out.print("prolog source file to test: ");
					testFileName = in.readLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		new PrologTest(testFileName);
	}

	/**
	 * Encapsulates a test as on object. 
	 */
	class Test {
		/** The name of the test */
		String name;
		/** The actual code to run for the test. */
		TestCode code;
		/** The status the test returned after it ran. */
		StatusReturn status;
		/** Whether the test strictly tests for multiple of the same answer */
		boolean strict = false;

        double points = 0;
        double pointsEarned = 0;
		/**
		 * Constructor.
		 * @param name The name of the test.
		 * @param code The code to run for the test.
		 * @param strict TODO
		 */
		Test(String name, TestCode code, boolean strict, double points) {
		    this.name = name;
		    this.code = code;
			this.strict = strict;
            this.points = points;
		}
		/**
		 * Runs the test: The output is a block describing the test run.
		 */
		public void run() {
			println("---------------------------------------------------------------");
			println("Running test '"+name+"'...");
			indentLevel++;
			status = code.code(this);
            // start of shameful code !!!!
            String dirty = "Incorrect output: Expected bag of [jane,jed,sally], but got [[";
            HashMap<String, String> map = new HashMap();
            map.put("jane",null);
            map.put("jed",null);
            map.put("sally",null);
            if (this.name.equals("hasChildren(mary,X).")){
                String res = status.toString();
                if (res.startsWith(dirty)){
                    String got = res.substring(dirty.length(), res.length()-1);
                    String answers[] = split(got);
                    boolean correct = true;
                    for (int i=answers.length-1; i>=0; i--) {
                        answers[i] = answers[i].trim();
                        if (!map.containsKey(answers[i])){
                            println("Test '"+name+"': "+status.toString());//+(status.msg==null?"":(" - "+status.msg)));
                            correct = false;
                            break;
                        }
                    }
                    if (correct){
                        status.status = Status.SUCCESS;
                        this.pointsEarned = this.points;
                    }
                }else{
                    println("Test '"+name+"': "+status.toString());//+(status.msg==null?"":(" - "+status.msg)));
                }
            }else if (this.name.equals("hasChildren(mary,X). strict")){
                 String res = status.toString();
                if (res.startsWith(dirty)){
                    String got = res.substring(dirty.length(), res.length()-1);
                    String answers[] = split(got);
                    boolean correct = true;
                    for (int i=answers.length-1; i>=0; i--) {
                        answers[i] = answers[i].trim();
                        if (!map.containsKey(answers[i])){
                            correct = false;
                            break;
                        }
                    }
                    if (correct && answers.length == 3){
                        status.status = Status.SUCCESS;
                        this.pointsEarned = this.points;
                    }else{
                        println("Test '"+name+"': "+status.toString());//+(status.msg==null?"":(" - "+status.msg)));
                    }
                }else{
                    println("Test '"+name+"': "+status.toString());//+(status.msg==null?"":(" - "+status.msg)));
                }            }else{
                println("Test '"+name+"': "+status.toString());//+(status.msg==null?"":(" - "+status.msg)));
                if (status.status == Status.SUCCESS){
                    this.pointsEarned = this.points;
                }
            }
            /// end of shameful code !!!!!!!
            //println("Test '"+name+"': "+status.toString());//+(status.msg==null?"":(" - "+status.msg)));
            //if (status.status == Status.SUCCESS){
            //this.pointsEarned = this.points;
            //}
			indentLevel--;
		}
	}
	
	/**
	 * A simple abstract class to be instantiated as an anonymous class.  The
	 * code is carried in instantiating the code(Test) method.  Used by class {@link Test}.
	 */
	abstract class TestCode {
		abstract public StatusReturn code(Test t);
	}
	
	/** A simple Timer class used by {@link PrologTest#checkProcessTermination(Process, boolean, long)}
	 * to timeout a join.
	 */
	class Timer extends Thread {
		int waitTime;
		Thread thread;
		public boolean done = false;
		public Timer(int waitTime, Thread thread) {
			this.waitTime = waitTime;
			this.thread = thread;
		}
		public void run() {
			sleepIgnoringInterrupts(waitTime); 
			if (!done) thread.interrupt();
			}; 
		}

	/**
	 * @param p The process to check for termination.
	 * @param kill Set to true to kill the process forcably if it hasn't terminated by the time <em>waitTime</em> had expired.
	 * @param waitTime The time (in milliseconds) to wait or the process to terminate.
	 * @return true iff the process is terminated, or it had terminated by the time <em>waitTime</em> had expired.
	 */
	public int checkProcessTermination(Process p, boolean kill, final long waitTime) {
		boolean dead = false;
		long until = System.currentTimeMillis()+waitTime;
		Timer timer = new Timer(2000, Thread.currentThread());
		timer.start();
		int exitValue = Integer.MAX_VALUE;
		while (!dead && System.currentTimeMillis()<until)
		  try {
			  exitValue = p.waitFor();// or for java 8: (until-System.currentTimeMillis(), TimeUnit.MILLISECONDS);
		  } catch (InterruptedException e) {if (kill) p.destroy();}
		timer.done = true;
	  return exitValue;
	}
	
	/**
	 * Write to the sysin of a subprocess.
	 * @param bw The writer to write to.
	 * @param line The contents to write.
	 */
	public void writeln(BufferedWriter bw, String line) {
		try {
			bw.write(line+"\n", 0, line.length()+1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Handle printing to sysout but with indenting.
	 * @param s The String to write out.
	 */
	public void println(String s) {
		print(s+"\n");
		indent = true;
	}
	
	/**
	 * Handle printing to sysout but with indenting.
	 * @param s The String to write out.
	 */
	public void print(String s) {
		String prefix = "";
		if (indent)
			for (int i=0; i<indentLevel; i++)
				prefix = prefix + "  ";
		s = s.replace("\n", "\n"+prefix);
		//avoid appending an indent without a newline... 
		int i;
		for (i=s.length()-1; i>=0 && s.charAt(i)==' '; i--);
		if (i>=0 && s.charAt(i)=='\n')
		  s = s.substring(0,i+1);
		System.out.print(prefix+s);
		indent = false;
	}
	
	/**
	 * Read from a subprocess's sysout or syserr.  Input is terminated and this method returns when
	 * either the <em>waitTime</em> (milliseconds) expires or the <em>terminator</em> string is 
	 * recognized.
	 * @param reader The reader -- the subprocess's sysout or syserr.
	 * @param terminator If this is non-null, input will terminate immediately after this string is recognized.
	 * @param waitTime A timeout period in milliseconds.
	 * @return The data read.
	 */
	public String readInput(BufferedReader reader, String terminator, long waitTime) {
		if (terminator!=null && terminator.length()==0)
			terminator = null;
		StringBuilder b = new StringBuilder();
		long until = System.currentTimeMillis() + waitTime;
		try {
			readLoop:
			while (System.currentTimeMillis() < until) {
				int i=0;
				while (reader.ready()) {
					char c = (char)reader.read();
					b.append(c);
					if (terminator!=null) {
						if (terminator.charAt(i++)!=c)
							i = 0;
						if (i>=terminator.length())
							break readLoop;
					}
				}
				Thread.yield();
			}
		} catch (IOException e) {
			return b.toString();
		}
		return b.toString();
	}
	
	/**
	 * Error categories. 
	 */
	enum Status {
		SUCCESS("Success"), 
		FAILED_TO_START_PROC("Failed to start process"), 
		INCORRECT_OUTPUT("Incorrect output"), 
		FAILED_TO_TERMINATE("Failed to terminate"), 
		UNEXPECTED_OUTPUT("Unexpected output"), 
		TERMINATED_UNEXPECTEDLY("Terminated unexpectedly"),
		UNIMPLEMENTED("Unimplemented term");
		Status(String name) {this.name = name;}
		String name;
		int count=0;
		public void inc() {count++;}
	}
	
	/**
	 * Encapsulates a status and a corresponding detail message. 
	 */
	class StatusReturn {
		Status status = null;
		String msg = null;
		public StatusReturn(Status stat, String msg) {
			this.status = stat;
			this.msg = msg;
		}
		public StatusReturn(Status stat) {
			this.status = stat;
		}
		@Override
		public String toString() {return status.name + (msg==null?"":(": "+msg));}
	}
	
	/**
	 * Encapsulates data about the subprocess. 
	 */
	protected class ProcDesc {
		public Process process;
		public BufferedWriter in;
		public BufferedReader out;
		public BufferedReader err;
		public ProcDesc(Process process, OutputStream inStream, InputStream outStream, InputStream errStream) {
			this.process = process;  
			in  = new BufferedWriter(new OutputStreamWriter(inStream));
			out = new BufferedReader(new InputStreamReader(outStream));
			err = new BufferedReader(new InputStreamReader(errStream));
		}
	}
	
	/**
	 * Runs GNU Prolog as a subprocess with the {@link #testFileName} loaded using --consultFile, 
	 * returning the {@link ProcDesc}.
	 * @param params Any additional parameters to the gprolog command.
	 * @return the {@link ProcDesc}.
	 */
	public ProcDesc runSubprocess(String[] params) {
			Process proc;
			
			int commandLength = 3 + (params==null?0:params.length);
			String command[] = new String[commandLength];
			int i=0;
			command[i++] = PROLOG;
			command[i++] = "--c";
			command[i++] = testFileName;
			if (params!=null)
			  for (String p: params)
				  command[i++] = p;
			
			print("Executing:");
			for (String s: command) print(" "+s);
			println("");
			
			// run the subprocess;
			ProcessBuilder pb = new ProcessBuilder(command);
			Map<String,String> env = pb.environment();
			String path = env.get("PATH");
			env.put("PATH", path+":/opt/local/bin");
			pb.redirectErrorStream(false);
			try {
				proc = pb.start();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return new ProcDesc(proc, proc.getOutputStream(), proc.getInputStream(), proc.getErrorStream());
	}
	
	/**
	 * Wait <em>time</em> milliseconds ignoring interrupts.
	 * @param time The time to wait in milliseconds.
	 * @param code If an intterrupt happens during the sleep,
	 *  {@link Runnable1#run(Object)} on <em>code</em> is called.  If the run() returns
	 *  true, this method aborts the sleep and returns false.  This parameter may be null,
	 *  in which case, this method sleeps the entire time. 
	 * @return True if this method waited the full <em>time</em>; false if run() is executed
	 *  and returns true.
	 */
	public static boolean sleepIgnoringInterrupts(long time) {
		long doneTime = System.currentTimeMillis()+time;
		while (System.currentTimeMillis()<doneTime) {
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {}
		}
		return true;
	}
	
	/**
	 * Output a report on the already-run tests.
	 * @param tests The tests to report on. They must have already been run.
	 */
	public void report(Test[] tests) {
		println("\n=========================================================================");
		println(String.format("%-60s %-50s %-40s", "Points", "Test", "Result"));
		println(String.format("%-60s %-50s %-40s", "------------", "------------", "------------"));
		int strict = 0;
		int passedStrict = 0;
        double total = 0;
		for (Test test: tests) {
			println(String.format("%-60s %-40f %-40s", test.name, test.pointsEarned, test.status.toString()));
            total += test.pointsEarned;
			test.status.status.inc();
			if (test.strict) {
				strict++;
				if (test.status.status==Status.SUCCESS)
					passedStrict++;
			}
		}
		println("\nSummary:");
		for (Status stat: Status.values()) {
			if (stat.count!=0)
			  println(String.format("%3d %-40s", stat.count, stat.name));
		}
	  println(String.format("---\n%3d     %-40s", tests.length, "Total Tests (version "+version+")"));
	  println(String.format(     "%3d/%-3d %-40s", passedStrict, strict, "Strict Tests Passed"));
	  println(String.format(     "%3d/%-3d %-40s", Status.SUCCESS.count-passedStrict, tests.length-strict, "Non-Strict Tests Passed"));
      println("");
	  println(String.format(     "%3f/%-3f %-40s", total, 100 + 7 * easy + 10 * hard, "Total points"));
	}

  /**
   * Send a query to the subprocess and return the result. If the subprocess offers
   * more results, collect them all.<br>
   * As a side effect, if anything is in the subprocess's syserr, then print it to this
   * process's sysout.  
   * @param query
   * @return
   */
	public String doQuery(String query) {
		checkProcAndRestart();
		writeln(curProc.in, query);
		String in = readInput(curProc.out, P_PROMPT, 500);
		String err = readInput(curProc.err, null, 100);
		if (err!=null && err.length()>0) {
			println("syserr:");
			indentLevel++;
			println(err);
			indentLevel--;
		}
		in = in.trim();
		if (in.endsWith("?")) {
			writeln(curProc.in, "a");
			String more = readInput(curProc.out, P_PROMPT, 500);
			in += "\n"+more;
		}
		println(in);
		return in;
  }
	
	/**
	 * Find the closing bracket (]) in String <em>s</em> which must have an
	 * opening [.  Account for nested [...] pairs.
	 * @param s
	 * @return
	 */
	public int indexOfClosingBracket(String s) {
		int length = s.length();
		int nested = 0;
		int i;
		char c;
		for (i=0; i<length; i++) {
			c = s.charAt(i);
			if (c=='[') {
				++i;
				break;
			}
		}
		for (; i<length; i++) {
			c = s.charAt(i);
			if      (c=='[') nested++;
			else if (c==']') nested--;
			if (nested<0)
				return i;
		}
		return -1;
	}
	
	private final static String reflexiveIndicator = "[A,A]";
	
	static public boolean isReflexive(String s) {
		return s.matches("\\[(\\w*),\\1\\]");
	}
	
	public boolean equalArrays(String[] result, String[] answer) {
		if (result==answer)
			return true;
		if (result==null || answer==null)
			return false;
		int reflInsertionPoint = Arrays.binarySearch(answer, reflexiveIndicator);
		boolean reflexive = reflInsertionPoint>=0;
		if (reflexive) {
			if (Arrays.binarySearch(result, reflexiveIndicator)>=0) {
				reflexive = false; // if it also has the reflective indicator we can treat it as non-reflexive. 
			}
		}
		for (int i=0,j=0; i<result.length; ) {
			String res = result[i++];
			if (reflexive) {
				if (isReflexive(res)) {
				  continue;
			  }
			}
			String ans = answer[j++];
			if (reflexive && ans.equals(reflexiveIndicator) && j<answer.length)
				ans = answer[j++];
			if (!(res==null ? ans==null : res.equals(ans)))
				return false;
		}
		return true;
	}
  
  /**
   * Verify that the command-delimieted String list in <em>str</em> (a prolog list) is the same
   * as the strings in <em>answer</em>.
   * @param str The prolog-output format string for a set.
   * @param prefix The profix, such as "L = [".
   * @param answer The correct answer set, does not need to be sorted.
   * @return null if the sets are the same, some appropriate error message otherwise.
   */
  public String verifySet(String str, String prefix, String[] answer) {
  	try {
			String setStr = str.substring(str.indexOf(prefix)+prefix.length(), str.indexOf("]"));
			String set[] = split(setStr);
			Arrays.sort(set);
			Arrays.sort(answer); 
			if (!Arrays.equals(set,answer)) {
				return "Expected bag of "+Array2StringCompressed(answer)+", but got "+Array2StringCompressed(set);
			}
	  	return null;
		} catch (Throwable e) {
			return "Expected list starting with '"+prefix+"' and ending with ']', but got '"+str+"'.";
		}
  }
  
  /**
   * Converts an array to compressed printable format.  That is all spaces removed and duplicate
   * elements are printed only once with "*<int>" appended, indicating the number of repeats.
   * @param array An array.
   * @return The array as a single printable string.
   */
  public String Array2StringCompressed(String[] array) {
  	StringBuilder buf = new StringBuilder("[");
  	String last = null;
  	int acc = -1;
  	for (String s: array) {
  		if (s.equals(last))
  			acc++;
  		else {
  			if (acc>1) buf.append("*"+acc);
  			if (acc>=0) buf.append(",");
  			buf.append(s);
  			acc = 1;
  		}
  		last = s;
  	}
		if (acc>1) buf.append("*"+acc);
  	buf.append("]");
  	return buf.toString();
  }

  /**
   * Verify that the command-delimieted list (of lists in [] format) in <em>str</em> (a prolog list) is the same
   * as the strings in <em>answer</em>.
   * @param str The prolog-output format string for a set.
   * @param prefix The profix, such as "L = [".
   * @param answer The correct answer set, does not need to be sorted.
   * @return null if the sets are the same, some appropriate error message otherwise.
   */
  public String verifySet2(String str, String prefix, String[] answer) {
  	try {
			String setStr = str.substring(str.indexOf(prefix)+prefix.length(), indexOfClosingBracket(str));
			String set[] = splitArray(setStr);
			Arrays.sort(set);
			Arrays.sort(answer); 
			if (!equalArrays(set,answer)) {
				return "Expected bag of "+Array2StringCompressed(answer)+", but got "+Array2StringCompressed(set);
			}
			return null;
		} catch (Throwable e) {
			return "Expected list starting with '"+prefix+"' and ending with ']', but got '"+str+"'.";
		}
  }
  
  /**
   * Replace newlines with HTML-style breaks.
   * @param s The input string.
   * @return A copy of <em>s</em> with newlines replaced with the HTML break code.
   */
  public String makePrintable(String s) {
  	return s.replace("\n", "<br>");
  }
  
  public String[] split(String commaList) {
  	Vector<String> ret = new Vector<String>();
  	boolean scanningList = false;
  	int start = 0;
  	for (int i=0, len=commaList.length(); i<len; i++) {
  		if (scanningList) {
  			if (commaList.charAt(i)==']');
  			scanningList = false;
  		}
  		else {
  			if (commaList.charAt(i)=='[')
  				scanningList=true;
  			if (commaList.charAt(i)==',') {
  				ret.add(commaList.substring(start,i));
  				start = i+1;
  			}
  		}
  	}
		ret.add(commaList.substring(start));
  	return ret.toArray(new String[ret.size()]);
  }

	/**
   * Constructs and Test with multiple answers using either the setof or bagof build-ins. The name 
   * of the test is the query followed by the comment (if there is one).
   * @param query The query, SHOULD NOT END IN A DOT (.). The query must have X as the answer variable.
   * @param answersAsCommaList A comma-separated list of identifiers; no spaces allowed.
   * @param strict If true, the answers may NOT contain duplicates, otherwise duplicates are allowed. 
   * @param comment A short comment to be appended to the name (may be null).
   * @return The test object.
   */
  public Test makeListTest(final String query, final String answersAsCommaList, final boolean strict, final String comment, double points) {
  	String answers[] = split(answersAsCommaList);
  	for (int i=answers.length-1; i>=0; i--) {
  		answers[i] = answers[i].trim();
  	}
  	return makeListTest(query, answers, strict, comment, points);
  }

	/**
   * Constructs and Test with multiple answers using either the setof or bagof build-ins. The name 
   * of the test is the query followed by the comment (if there is one).
   * @param query The query, SHOULD NOT END IN A DOT (.). The query must have X as the answer variable.
   * @param answers The set of identifiers we expect as the answers.
   * @param strict If true, the answers may NOT contain duplicates, otherwise duplicates are allowed. 
   * @param comment A short comment to be appended to the name (may be null).
   * @return The test object.
   */
  public Test makeListTest(final String query, final String[] answers, final boolean strict, final String comment, double points) {
  	return 
  			new Test(query+"."+(comment==null?(strict?" strict":""):(" "+comment)),
  					new TestCode() {
  				@Override public StatusReturn code(Test t) {
  					String q = (strict?"bag":"set")+"of(X,"+query+",L).";
  					println(q);
  					String in = trim(doQuery(q));
  					String expected = "L = [";
  					if (!in.startsWith(expected) && answers.length>0) {
  						return makeStatusReturn("Expected starts-with of "+makePrintable(expected), in);
  					}
  					if (in.startsWith("no") && answers.length==0) {
  						return new StatusReturn(Status.SUCCESS);
  					}
  					
  					String verify = verifySet(in, expected, answers); 
  					if (verify!=null)
  						return new StatusReturn(Status.INCORRECT_OUTPUT, verify);
  					return new StatusReturn(Status.SUCCESS);
  				}
  			}, strict, points);
  }
  
  public StatusReturn makeStatusReturn(String error, String outputString) {
  	Status stat;
  	if (outputString.contains("existence_error")) 
  		stat = Status.UNIMPLEMENTED;
  	else 
  		stat = Status.INCORRECT_OUTPUT;
  	return new StatusReturn(stat, error);
  }
  
  public String[] splitArray(String s) {
  	String answers[] = s.split("]\\s*,\\s*\\[");
  	for (int i=answers.length-1; i>=0; i--) {
  		answers[i] = answers[i].trim();
  		if (!answers[i].startsWith("[")) answers[i] = "["+answers[i];
  		if (!answers[i].endsWith  ("]")) answers[i] = answers[i]+"]";
  	}
  	return answers;
  }
	
	/**
   * Constructs and Test with multiple answers using either the setof or bagof build-ins. The name 
   * of the test is the query followed by the comment (if there is one).
   * @param query The query, SHOULD NOT END IN A DOT (.). The query must have X as the answer variable.
   * @param answersAsCommaList A comma-separated list of []-delimited lists identifiers; no spaces allowed.
   * @param strict If true, the answers may NOT contain duplicates, otherwise duplicates are allowed. 
   * @param comment A short comment to be appended to the name (may be null).
   * @return The test object.
   */
  public Test makeLis2Test(final String query, final String answersAsCommaList, final boolean strict, final String comment, double points) {
  	String answers[] = splitArray(answersAsCommaList);
  	return makeLis2Test(query, answers, strict, comment, points);
  }

	/**
   * Constructs and Test with multiple answers using either the setof or bagof build-ins. The name 
   * of the test is the query followed by the comment (if there is one).
   * @param query The query, SHOULD NOT END IN A DOT (.). The query must have X as the answer variable.
   * @param answers The set of identifiers we expect as the answers.
   * @param strict If true, the answers may NOT contain duplicates, otherwise duplicates are allowed. 
   * @param comment A short comment to be appended to the name (may be null).
   * @return The test object.
   */
  public Test makeLis2Test(final String query, final String[] answers, final boolean strict, final String comment, double points) {
  	return 
  			new Test(query+"."+(comment==null?(strict?" strict":""):(" "+comment)),
  					new TestCode() {
  				@Override public StatusReturn code(Test t) {
  					String q = (strict?"bag":"set")+"of([X,Y],"+query+",L).";
  					println(q);
  					String in = trim(doQuery(q));
  					String expected = "L = [";
  					if (!in.startsWith(expected) && answers.length>0) {
  						return makeStatusReturn("Expected starts-with of "+makePrintable(expected), in);
  					}
  					if (in.startsWith("no") && answers.length==0) {
  						return new StatusReturn(Status.SUCCESS);
  					}
  					String verify = verifySet2(in, expected, answers); 
  					if (verify!=null)
  						return new StatusReturn(Status.INCORRECT_OUTPUT, verify);
  					return new StatusReturn(Status.SUCCESS);
  				}
  			}, strict, points);
  }
	
  /**
   * Constructs and Test with a boolean answer, ie: one with no variables. The name
   * of the test is the query followed by the comment (if there is one).
   * @param comment A short comment to be appended to the name (may be null).
   * @param query The query, SHOULD NOT END IN A DOT (.).
   * @param answers The set of identifiers we expect as the answers.
   * @return The test object.
   */
  public Test makeBoolTest(final String query, final boolean answer, final String comment, double points) {
		final String expected = answer?"true":"false";
		final String altExpected = answer?"yes":"no";
  	return 
  			new Test(query+"."+(comment==null?(" "+expected):(" "+comment)),
  					new TestCode() {
  				@Override public StatusReturn code(Test t) {
  					String in = doQuery(query+(query.endsWith(".")?":":"."));
  					in = trim(in);
  					if (!in.startsWith(expected) && !in.startsWith(altExpected)) {
  						return makeStatusReturn("Expected starts-with of "+makePrintable(expected+" or "+altExpected), in);
  					}
  					return new StatusReturn(Status.SUCCESS);
  				}
  			}, false, points);
  }
	
  /**
   * GNU Prolog just randomly inserts "(<int> ms)" in front of answers, so get rid 
   * of this from s.
   * @param s The string to trim.
   * @return The trimmed version of s.
   */
  public String trim(String s) {
		if (s.startsWith("(")) { // there could be an answer like "(1 ms) yes".
			int i = s.indexOf(')');
			if (i>0 && ++i<s.length()) {
				s = s.substring(i).trim();
			}
		}
		return s;
  }
  
  public boolean checkProcStillRunning() {
  	try {
			curProc.process.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
  }
  
  public void checkProcAndRestart() {
  	if (curProc!=null) {
  		if (checkProcStillRunning())
  			return;
  		println("****Process unexpectedly terminated. Restarting...");
  	}
		curProc = runSubprocess(new String[] {"--c", pprogPath+"/families.pl"});
		if (curProc==null) {
			println("****Failed to run test program "+testFileName);
		}
		if (checkProcessTermination(curProc.process, false, 2000)!=Integer.MAX_VALUE) {
			println("****New process unexpectedly terminated.");
			System.exit(-1);
		}
		println(readInput(curProc.out, P_PROMPT, 500));
  }
  
  public void stopProc() {
		writeln(curProc.in, "halt.");
		println(readInput(curProc.out, null, 1000));
		if (checkProcessTermination(curProc.process, true, 2000)==Integer.MAX_VALUE) {
			println("**** Process failed to terminate as expected.  It had to be terminated forcibly.");
		}
  }
  
  double easy = 100.0/71.0;
  double medium = 2 * easy;
  double hard = 5 * easy;
  /**
   * The list of tests to run.
   */
	Test tests[] = {
//			new Test("run with families",
//					new TestCode() {
//				@Override public StatusReturn code(Test t) {
//					curProc = runSubprocess(new String[] {"--c", pprogPath+"/families.pl"});
//					if (curProc==null) {
//						println("Failed to run test program "+testFileName);
//					}
//					if (checkProcessTermination(curProc.process, false, 2000)!=Integer.MAX_VALUE) {
//						println("Test "+t.name+": Process unexpectedly terminated.");
//						return new StatusReturn(Status.TERMINATED_UNEXPECTEDLY);
//					}
//					println(readInput(curProc.out, P_PROMPT, 500));
//					return new StatusReturn(Status.SUCCESS);
//				}
//			}, false),
			
			//========== hasChild/2 (this should always work as it's given in families.pl ===========================
			// makeBoolTest("hasChild(fred, jed)", true, null),
			//========== parentOf/2 =================================================================================
            makeBoolTest("parentOf(freida,jason)", true, null, easy/5),
			makeBoolTest("parentOf(jason,freida)", false, null, easy/5),
			makeListTest("parentOf(freida,X)", "jason", false, null, easy/5),
			makeListTest("parentOf(mary,X)", "jed,sally,jane", false, null, easy/5),
			makeListTest("parentOf(X,terry)", "harry,jan", false, null, easy/5),

			makeListTest("parentOf(X,terry)", "harry,jan", true, null, easy),
			//========== motherOf/2 =================================================================================
			makeBoolTest("motherOf(jane,mavis)", false, null, easy/3),
			makeBoolTest("motherOf(jane,jack)", true, null, easy/3),
			makeListTest("motherOf(X,terry)", "jan", false, null, easy/3),
            
			makeListTest("motherOf(jane,X)", "george,jack", true, null, easy),
			//========== fatherOf/2 =================================================================================
			makeBoolTest("fatherOf(joe,jed)", false, null, easy/3),
			makeBoolTest("fatherOf(joe,jane)", true, null, easy/3),
			makeListTest("fatherOf(X,jack)", "sam", false, null, easy/3),

			makeListTest("fatherOf(sam,X)", "george,jack", true, null, easy),
			//========== grandparentOf/2 ============================================================================
			makeBoolTest("grandparentOf(harry,jill)", true, null, easy/6),
			makeBoolTest("grandparentOf(harry,terry)", false, null, easy/6),
			makeBoolTest("grandparentOf(harry,jed)", false, null, easy/6),
			makeListTest("grandparentOf(harry,X)", "jill", false, null, easy/6),
			makeListTest("grandparentOf(mary,X)", "george,jack", false, null, easy/6),
			makeListTest("grandparentOf(X,mavis)", "jason,terry,jane,sam", false, null, easy/6),

			makeListTest("grandparentOf(X,mavis)", "jason,terry,jane,sam", true, null, easy),
			//========== grandmotherOf/2 ============================================================================
			makeBoolTest("grandmotherOf(jane,mavis)", true, null, easy/3),
			makeBoolTest("grandmotherOf(jane,jack)", false, null, easy/3),
			makeListTest("grandmotherOf(jane,X)", "mavis", false, null, easy/3),

			makeListTest("grandmotherOf(X,jill)", "jan,freida", true, null, easy),
			//========== grandfatherOf/2 ============================================================================
			makeBoolTest("grandfatherOf(joe,jane)", false, null, easy/3),
			makeBoolTest("grandfatherOf(joe,george)", true, null, easy/3),
			makeListTest("grandfatherOf(joe,X)", "george,jack", false, null, easy/3),

			makeListTest("grandfatherOf(X,jill)", "tim,harry", true, null, easy),
			//========== greatgrandparentOf/2 =======================================================================
			makeBoolTest("greatgrandparentOf(tim,mavis)", true, null, easy/5),
			makeBoolTest("greatgrandparentOf(tim,jill)", false, null, easy/5),
			makeListTest("greatgrandparentOf(tim,X)", "mavis", false, null, easy/5),
			makeListTest("greatgrandparentOf(mary,X)", "mavis", false, null, easy/5),
			makeListTest("greatgrandparentOf(X,mavis)", "tim,freida,jan,harry,mary,joe", false, null, easy/5),

			makeListTest("greatgrandparentOf(X,mavis)", "tim,freida,jan,harry,mary,joe", true, null, easy),
			//========== greatgrandmotherOf/2 =======================================================================
			makeBoolTest("greatgrandmotherOf(freida,mavis)", true, null, easy/5),
			makeBoolTest("greatgrandmotherOf(jane,mavis)", false, null, easy/5),
			makeListTest("greatgrandmotherOf(jan,X)", "mavis", false, null, easy/5),
			makeBoolTest("greatgrandmotherOf(jane,X)",false, null, easy/5),
			makeListTest("greatgrandmotherOf(X,mavis)", "jan,freida,mary", false, null, easy/5),

			makeListTest("greatgrandmotherOf(X,mavis)", "jan,freida,mary", true, null, easy),
			//========== greatgrandfatherOf/2 =======================================================================
			makeBoolTest("greatgrandfatherOf(mary,mavis)", false, null, easy/4),
			makeBoolTest("greatgrandfatherOf(joe,mavis)", true, null, easy/4),
			makeListTest("greatgrandfatherOf(joe,X)", "mavis", false, null, easy/4),
			makeListTest("greatgrandfatherOf(X,mavis)", "tim,harry,joe", false, null, easy/4),

			makeListTest("greatgrandfatherOf(X,mavis)", "tim,harry,joe", true, null, easy),
            //========== childOf/2 ============================================
 			makeBoolTest("childOf(rover,lady)", true, null, easy/4),
			makeBoolTest("childOf(fred,mary)", false, null, easy/4),
			makeListTest("childOf(X,fred)", "jed,sally", false, null, easy/4),
			makeListTest("childOf(terry,X)", "jan,harry", false, null, easy/4),

			makeListTest("childOf(terry,X)", "jan,harry", true, null, easy),
            //========== daughterOf/2 ============================================
 			makeBoolTest("daughterOf(jill,jason)", true, null, easy/3),
			makeBoolTest("daughterOf(jill,tim)", false, null, easy/3),
			makeListTest("daughterOf(X,mary)", "jane,sally", false, null, easy/3),

			makeListTest("daughterOf(X,mary)", "jane,sally", true, null, easy),
            //========== sonOf/2 ============================================
 			makeBoolTest("sonOf(jason,freida)", true, null, easy/3),
			makeBoolTest("sonOf(jason,jan)", false, null, easy/3),
			makeListTest("sonOf(X,jane)", "george,jack", false, null, easy/3),

			makeListTest("sonOf(X,jane)", "george,jack", true, null, easy),
            //========== grandchildOf/2 ============================================
 			makeBoolTest("grandchildOf(jill,freida)", true, null, easy/3),
			makeBoolTest("grandchildOf(jason,jan)", false, null, easy/3),
			makeListTest("grandchildOf(X,mary)", "george,jack", false, null, easy/3),

			makeListTest("grandchildOf(X,mary)", "george,jack", true, null, easy),
            //========== granddaughterOf/2 ============================================
 			makeBoolTest("granddaughterOf(jill,freida)", true, null, easy/3),
			makeBoolTest("granddaughterOf(george,mary)", false, null, easy/3),
			makeListTest("granddaughterOf(jill, X)", "tim,freida,jan,harry", false, null, easy/3),

			makeListTest("granddaughterOf(jill, X)", "tim,freida,jan,harry", true, null, easy),
            //========== grandsonOf/2 ============================================
 			makeBoolTest("grandsonOf(jack,mary)", true, null, easy/3),
			makeBoolTest("grandsonOf(jill,jan)", false, null, easy/3),
			makeListTest("grandsonOf(jack, X)", "joe,mary", false, null, easy/3),

			makeListTest("grandsonOf(jack, X)", "joe,mary", true, null, easy),
            //========== greatgrandchildOf/2 ============================================
 			makeBoolTest("greatgrandchildOf(mavis,joe)", true, null, easy/3),
			makeBoolTest("greatgrandchildOf(mavis,jed)", false, null, easy/3),
			makeListTest("greatgrandchildOf(mavis,X)", "tim,freida,jan,harry,mary,joe", false, null, easy/3),

			makeListTest("greatgrandchildOf(mavis,X)", "tim,freida,jan,harry,mary,joe", true, null, easy),
            //========== greatgranddaughterOf/2 ============================================
 			makeBoolTest("greatgranddaughterOf(mavis,joe)", true, null, easy/3),
			makeBoolTest("greatgranddaughterOf(mavis,jed)", false, null, easy/3),
			makeListTest("greatgranddaughterOf(mavis,X)", "tim,freida,jan,harry,mary,joe", false, null, easy/3),

			makeListTest("greatgranddaughterOf(mavis,X)", "tim,freida,jan,harry,mary,joe", true, null, easy),
            //========== greatgrandsonOf/2 ============================================
 			makeBoolTest("greatgrandsonOf(mavis,joe)", false, null, easy/2),
			makeBoolTest("greatgrandsonOf(mavis,jed)", false, null, easy/2),
			makeBoolTest("greatgrandsonOf(mavis,X)", false, null, easy),
			//========== ancestorOf/2 ===============================================================================
			makeBoolTest("ancestorOf(joe,mavis)", true, null, medium/2),
			makeListTest("ancestorOf(joe,X)", new String[]{"george","jack","jane","mavis"}, false, null, medium/2),

			makeListTest("ancestorOf(X,mavis)", new String[]{"freida", "george", "harry", "jan", "jane", "jason", "jill", "joe", "mary", "sam", "terry", "tim"}, false, null, medium),
			//========== ancestorOf/3 ===============================================================================
			makeBoolTest("ancestorOf(X,joe,1)", false, null, medium/10),
			makeListTest("ancestorOf(X,jane,0)" , "jane", false, "duplicates OK", medium/10),
			makeListTest("ancestorOf(X,mavis,1)", "george,jill", false, "duplicates OK", medium/10),
			makeListTest("ancestorOf(X,mavis,2)", "jane,jason,sam,terry", false, "duplicates OK", medium/10),
			makeListTest("ancestorOf(X,mavis,3)", "freida,harry,jan,joe,mary,tim", false, "duplicates OK", medium/10),
			makeBoolTest("ancestorOf(mavix,X,1)", false, null, medium/10),
			makeListTest("ancestorOf(jane,X,0)" , "jane", false, "duplicates OK", medium/10),
			makeListTest("ancestorOf(mary,X,1)", "jane, jed, sally", false, "duplicates OK", medium/10),
			makeListTest("ancestorOf(mary,X,2)", "george, jack", false, "duplicates OK", medium/10),
			makeListTest("ancestorOf(mary,X,3)", "mavis", false, "duplicates OK", medium/10),
            
			makeListTest("ancestorOf(X,jane,0)" , "jane", true, "duplicates not accepted", medium/8),
			makeListTest("ancestorOf(X,mavis,1)", "george,jill", true, "duplicates not accepted", medium/8),
			makeListTest("ancestorOf(X,mavis,2)", "jane,jason,sam,terry", true, "duplicates not accepted", medium/8),
			makeListTest("ancestorOf(X,mavis,3)", "freida,harry,jan,joe,mary,tim", true, "duplicates not accepted", medium/8),
			makeListTest("ancestorOf(jane,X,0)" , "jane", true, "duplicates not accepted", medium/8),
			makeListTest("ancestorOf(mary,X,1)", "jane, jed, sally", true, "duplicates not accepted", medium/8),
			makeListTest("ancestorOf(mary,X,2)", "george, jack", true, "duplicates not accepted", medium/8),
			makeListTest("ancestorOf(mary,X,3)", "mavis", true, "duplicates not accepted", medium/8),
            
            //-------

			makeBoolTest("ancestorOf(terry,jan,X)", false, null, medium/5),
			makeListTest("ancestorOf(jan,jan,X)", "0", false, "duplicates OK", medium/5),
			makeListTest("ancestorOf(jan,terry,X)", "1", false, "duplicates OK", medium/5),
			makeListTest("ancestorOf(jan,jill,X)", "2", false, "duplicates OK", medium/5),
			makeListTest("ancestorOf(jan,mavis,X)", "3", false, "duplicates OK", medium/5),

			makeListTest("ancestorOf(jan,jan,X)", "0", true, "duplicates not accepted", medium/4),
			makeListTest("ancestorOf(jan,terry,X)", "1", true, "duplicates not accepted", medium/4),
			makeListTest("ancestorOf(jan,jill,X)", "2", true, "duplicates not accepted", medium/4),
			makeListTest("ancestorOf(jan,mavis,X)", "3", true, "duplicates not accepted", medium/4),
			//========== descendantOf/3 ===============================================================================
			makeListTest("descendantOf(jack,X,2)", "mary,joe", false, "duplicates OK", 2*easy),

			makeListTest("descendantOf(mavis,jan,X)", "3", true, "duplicates not accepted", 2*easy),
			//========== related/2 =======================================================================
			makeBoolTest("related(george,jack)", false, "siblings may not be genetically related", medium/6),
			makeBoolTest("related(jane,jason)", false, "different family lines", medium/6),
			makeBoolTest("related(mavis,joe)", true, null, medium/6),
			makeListTest("related(jane,X)", "george,jack,jane,joe,mary,mavis", false, null, medium/6),
			makeListTest("related(X,jane)", "george,jack,jane,joe,mary,mavis", false, null, medium/6),
			makeLis2Test("related(X,Y)", "[A,A],[fred,jed],[fred,sally],[freida,jason],[freida,jill],[freida,mavis],[george,jane],[george,joe],[george,mary],[george,mavis],[george,sam],[harry,jill],[harry,mavis],[harry,terry],[jack,jane],[jack,joe],[jack,mary],[jack,sam],[jan,jill],[jan,mavis],[jan,terry],[jane,george],[jane,jack],[jane,joe],[jane,mary],[jane,mavis],[jason,freida],[jason,jill],[jason,mavis],[jason,tim],[jed,fred],[jed,mary],[jill,freida],[jill,harry],[jill,jan],[jill,jason],[jill,mavis],[jill,terry],[jill,tim],[joe,george],[joe,jack],[joe,jane],[joe,mavis],[lady,lassie],[lady,rover],[lady,snoopy],[lassie,lady],[lassie,snoopy],[lassie,tramp],[mary,george],[mary,jack],[mary,jane],[mary,jed],[mary,mavis],[mary,sally],[mavis,freida],[mavis,george],[mavis,harry],[mavis,jan],[mavis,jane],[mavis,jason],[mavis,jill],[mavis,joe],[mavis,mary],[mavis,sam],[mavis,terry],[mavis,tim],[rover,lady],[rover,snoopy],[sally,fred],[sally,mary],[sam,george],[sam,jack],[sam,mavis],[snoopy,lady],[snoopy,lassie],[snoopy,rover],[snoopy,tramp],[terry,harry],[terry,jan],[terry,jill],[terry,mavis],[tim,jason],[tim,jill],[tim,mavis],[tramp,lassie],[tramp,snoopy]", false, null, medium/6),

			makeListTest("related(jane,X)", "george,jack,jane,joe,mary,mavis", true, null, hard/3),
			makeListTest("related(X,jane)", "george,jack,jane,joe,mary,mavis", true, null, hard/3),
			makeLis2Test("related(X,Y)", "[A,A],[fred,jed],[fred,sally],[freida,jason],[freida,jill],[freida,mavis],[george,jane],[george,joe],[george,mary],[george,mavis],[george,sam],[harry,jill],[harry,mavis],[harry,terry],[jack,jane],[jack,joe],[jack,mary],[jack,sam],[jan,jill],[jan,mavis],[jan,terry],[jane,george],[jane,jack],[jane,joe],[jane,mary],[jane,mavis],[jason,freida],[jason,jill],[jason,mavis],[jason,tim],[jed,fred],[jed,mary],[jill,freida],[jill,harry],[jill,jan],[jill,jason],[jill,mavis],[jill,terry],[jill,tim],[joe,george],[joe,jack],[joe,jane],[joe,mavis],[lady,lassie],[lady,rover],[lady,snoopy],[lassie,lady],[lassie,snoopy],[lassie,tramp],[mary,george],[mary,jack],[mary,jane],[mary,jed],[mary,mavis],[mary,sally],[mavis,freida],[mavis,george],[mavis,harry],[mavis,jan],[mavis,jane],[mavis,jason],[mavis,jill],[mavis,joe],[mavis,mary],[mavis,sam],[mavis,terry],[mavis,tim],[rover,lady],[rover,snoopy],[sally,fred],[sally,mary],[sam,george],[sam,jack],[sam,mavis],[snoopy,lady],[snoopy,lassie],[snoopy,rover],[snoopy,tramp],[terry,harry],[terry,jan],[terry,jill],[terry,mavis],[tim,jason],[tim,jill],[tim,mavis],[tramp,lassie],[tramp,snoopy]", true, null, hard/3),
    		//========== parent/1 ===================================================================================
			makeBoolTest("parent(terry)", true, null, easy/3),
			makeBoolTest("parent(jack)", false, null, easy/3),
			makeListTest("parent(X)", "fred,freida,george,harry,jan,jane,jason,jill,joe,lady,lassie,mary,rover,sam,terry,tim,tramp", false, null, easy/3),

			makeListTest("parent(X)", "fred,freida,george,harry,jan,jane,jason,jill,joe,lady,lassie,mary,rover,sam,terry,tim,tramp", true, null, hard),
			//========== childless/1 ================================================================================
			makeBoolTest("childless(jed)", true, null, easy),
			makeBoolTest("childless(george)", false, null, easy),
            
            //---------

			makeListTest("childless(X)", "felix,jack,jed,mavis,sally,snoopy", false, null, medium),

			makeListTest("childless(X)", "felix,jack,jed,mavis,sally,snoopy", true, null, medium),
			
			//========== hasChildren/2 ================================================================================
			makeBoolTest("hasChildren(lady,[lassie,rover])", true, null, easy/5),
			makeBoolTest("hasChildren(fred,[sally,jed])", true, null, easy/5),
			makeBoolTest("hasChildren(fred,[jed])", false, null, easy/5),
			makeBoolTest("hasChildren(fred,[sally,jed,jane])", false, null, easy/5),
			makeListTest("hasChildren(mary,X)", "jed,sally,jane", false, null, easy/5),

			makeListTest("hasChildren(mary,X)", "jed,sally,jane", true, null, hard),
			//========== countChildren/2 ================================================================================
			makeBoolTest("countChildren(mary,3)", true, null, medium/2),
			makeListTest("countChildren(lady,X)", "2", false, null, medium/2),

			makeListTest("countChildren(lady,X)", "2", true, null, hard),

            //---------
			makeListTest("countChildren(X,1)", "tim,freida,jan,harry,joe,jason,terry,jill,george,tramp,rover,lassie", false, null, hard),

			makeListTest("countChildren(X,1)", "tim,freida,jan,harry,joe,jason,terry,jill,george,tramp,rover,lassie", true, null, hard),
			//========== sibling/2 =======================================================================
			makeBoolTest("sibling(jed,terry)", false, "different families", medium/6),
			makeBoolTest("sibling(jed,jane)", false, "step siblings", medium/6),
			makeBoolTest("sibling(jed,sally)", true, null, medium/6),
			makeListTest("sibling(jed,X)", "sally", false, null, medium/6),
			makeListTest("sibling(X,george)", "jack", false, null, medium/6),
			makeLis2Test("sibling(X,Y)", "[george,jack],[jack,george],[jed,sally],[sally,jed]", false, null, medium/6),
			
			makeListTest("sibling(jed,X)", "sally", true, null, hard/3),
			makeListTest("sibling(X,george)", "jack", true, null, hard/3),
			makeLis2Test("sibling(X,Y)", "[george,jack],[jack,george],[jed,sally],[sally,jed]", true, null, hard/3),
			//========== sisterOf/2 =======================================================================
			makeBoolTest("sisterOf(sally,jane)", false, "step sister", easy/6),
			makeBoolTest("sisterOf(jed,sally)", false, "wrong way", easy/6),
			makeBoolTest("sisterOf(sally,jed)", true, null, easy/6),
			makeListTest("sisterOf(sally,X)", "jed", false, null, easy/6),
			makeListTest("sisterOf(X,jed)", "sally", false, null, easy/6),
			makeLis2Test("sisterOf(X,Y)", "[sally,jed]", false, null, easy/6),

			makeListTest("sisterOf(sally,X)", "jed", true, null, easy/3),
			makeListTest("sisterOf(X,jed)", "sally", true, null, easy/3),
			makeLis2Test("sisterOf(X,Y)", "[sally,jed]", true, null, easy/3),
			//========== brotherOf/2 =======================================================================
			makeBoolTest("brotherOf(jed,jane)", false, "step brother", easy/6),
			makeBoolTest("brotherOf(sally,jed)", false, "wrong way", easy/6),
			makeBoolTest("brotherOf(jed,sally)", true, null, easy/6),
			makeListTest("brotherOf(jed,X)", "sally", false, null, easy/6),
			makeListTest("brotherOf(X,sally)", "jed", false, null, easy/6),
			makeLis2Test("brotherOf(X,Y)", "[george,jack],[jack,george],[jed,sally]", false, null, easy/6),

			makeListTest("brotherOf(jed,X)", "sally", true, null, easy/3),
			makeListTest("brotherOf(X,sally)", "jed", true, null, easy/3),
			makeLis2Test("brotherOf(X,Y)", "[george,jack],[jack,george],[jed,sally]", true, null, easy/3),
			//========== stepSibling/2 =======================================================================
		   // makeBoolTest("stepSibling(jed,terry)", false, "different families"),
		   // makeBoolTest("stepSibling(jed,sally)", false, "siblings"),
		   // makeBoolTest("stepSibling(jed,jane)", true, null),
		   // makeListTest("stepSibling(jed,X)", "jane", false, null),
		   // makeListTest("stepSibling(jed,X)", "jane", true, null),
		   // makeListTest("stepSibling(X,jed)", "jane", false, null),
		   // makeListTest("stepSibling(X,jed)", "jane", true, null),
		   // makeLis2Test("stepSibling(X,Y)", "[jane,jed],[jane,sally],[jed,jane],[sally,jane]", false, null),
		   // makeLis2Test("stepSibling(X,Y)", "[jane,jed],[jane,sally],[jed,jane],[sally,jane]", true, null),

			//========== stepSisterOf/2 =======================================================================
			makeBoolTest("stepSisterOf(jane,terry)", false, "different families", medium/6),
			makeBoolTest("stepSisterOf(jed,jane)", false, "male", medium/6),
			makeBoolTest("stepSisterOf(jane,sally)", true, null, medium/6),
			makeListTest("stepSisterOf(jane,X)", "sally,jed", false, null, medium/6),
			makeListTest("stepSisterOf(X,jed)", "jane", false, null, medium/6),
			makeLis2Test("stepSisterOf(X,Y)", "[jane,jed],[jane,sally],[sally,jane]", false, null, medium/6),

			makeListTest("stepSisterOf(jane,X)", "sally,jed", true, null, hard/3),
			makeListTest("stepSisterOf(X,jed)", "jane", true, null, hard/3),
			makeLis2Test("stepSisterOf(X,Y)", "[jane,jed],[jane,sally],[sally,jane]", true, null, hard/3),
			//========== stepBrotherOf/2 =======================================================================
			makeBoolTest("stepBrotherOf(jed,terry)", false, "different families", easy/6),
			makeBoolTest("stepBrotherOf(jane,jed)", false, "female", easy/6),
			makeBoolTest("stepBrotherOf(jed,jane)", true, null, easy/6),
			makeListTest("stepBrotherOf(jed,X)", "jane", false, null, easy/6),
			makeListTest("stepBrotherOf(X,jane)", "jed", false, null, easy/6),
			makeLis2Test("stepBrotherOf(X,Y)", "[jed,jane]", false, null, easy/6),

			makeListTest("stepBrotherOf(jed,X)", "jane", true, null, easy/3),
			makeListTest("stepBrotherOf(X,jane)", "jed", true, null, easy/3),
			makeLis2Test("stepBrotherOf(X,Y)", "[jed,jane]", true, null, easy/3),
			//========== cousin/2 =======================================================================
			//makeBoolTest("cousin(george,jill)", false, "different families"),
			//makeListTest("cousin(george,X)", new String[]{}, false, null),
			//makeListTest("cousin(X,jed)", new String[]{}, false, null),
			//makeLis2Test("cousin(X,Y)", new String[]{}, false, null),

    		//========== getSpecies/2 =======================================================================
			makeBoolTest("getSpecies(tim,dog)", false, null, hard/8),
			makeBoolTest("getSpecies(sally,cat)", false, null, hard/8),
			makeBoolTest("getSpecies(tim,human)", true, null, hard/8),
			makeListTest("getSpecies(tim,X)", "human", false, null, hard/8),
			makeListTest("getSpecies(X,human)", "fred,freida,george,harry,jack,jan,jane,jason,jed,jill,joe,mary,mavis,sally,sam,terry,tim", false, null, hard/8),
			makeListTest("getSpecies(X,dog)", "lady,tramp,rover,lassie,snoopy", false, null, hard/8),
			makeListTest("getSpecies(X,cat)", "felix", false, null, hard/8),
			makeLis2Test("getSpecies(X,Y)", "[car1,car],[car2,car],[car3,car],[felix,cat],[fred,human],[freida,human],[george,human],[harry,human],[house1,house],[house2,house],[house3,house],[house4,house],[jack,human],[jan,human],[jane,human],[jason,human],[jed,human],[jill,human],[joe,human],[lady,dog],[lassie,dog],[mary,human],[mavis,human],[rover,dog],[sally,human],[sam,human],[snoopy,dog],[terry,human],[tim,human],[tramp,dog]", false, null, hard/8),

			makeListTest("getSpecies(tim,X)", "human", true, null, hard/3),
			makeListTest("getSpecies(X,human)", "fred,freida,george,harry,jack,jan,jane,jason,jed,jill,joe,mary,mavis,sally,sam,terry,tim", true, null, hard/3),
			makeLis2Test("getSpecies(X,Y)", "[car1,car],[car2,car],[car3,car],[felix,cat],[fred,human],[freida,human],[george,human],[harry,human],[house1,house],[house2,house],[house3,house],[house4,house],[jack,human],[jan,human],[jane,human],[jason,human],[jed,human],[jill,human],[joe,human],[lady,dog],[lassie,dog],[mary,human],[mavis,human],[rover,dog],[sally,human],[sam,human],[snoopy,dog],[terry,human],[tim,human],[tramp,dog]", true, null, hard/3),
//			//========== isMale/1 =======================================================================
//			makeBoolTest("isMale(lady)", false, null),
//			makeBoolTest("isMale(terry)", false, null),
//			makeBoolTest("isMale(snoopy)", true, null),
//			makeBoolTest("isMale(tim)", true, null),
//			makeListTest("isMale(X)", "felix,fred,george,harry,jack,jason,jed,joe,rover,sam,snoopy,tramp,tim", false, null),
//			makeListTest("isMale(X)", "felix,fred,george,harry,jack,jason,jed,joe,rover,sam,snoopy,tramp,tim", true, null),

//			//========== isFemale/1 =======================================================================
//			makeBoolTest("isFemale(snoopy)", false, null),
//			makeBoolTest("isFemale(tim)", false, null),
//			makeBoolTest("isFemale(lassie)", true, null),
//			makeBoolTest("isFemale(terry)", true, null),
//			makeListTest("isFemale(X)", "freida,jan,jane,jill,lady,lassie,mary,mavis,sally,terry", false, null),
//			makeListTest("isFemale(X)", "freida,jan,jane,jill,lady,lassie,mary,mavis,sally,terry", true, null),

			//========== pet/1 =======================================================================
			makeBoolTest("pet(jan)", false, null, easy/5),
			makeBoolTest("pet(lassie)", false, null, easy/5),
			makeBoolTest("pet(rover)", true, null, easy/5),
			makeBoolTest("pet(lady)", true, null, easy/5),
			makeListTest("pet(X)", "felix,snoopy,lady,rover", false, null, easy/5),

			makeListTest("pet(X)", "felix,snoopy,lady,rover", true, null, easy),

			//========== feral/1 =======================================================================
			makeBoolTest("feral(mary)", false, null, easy/5),
			makeBoolTest("feral(lady)", false, null, easy/5),
			makeBoolTest("feral(tramp)", true, null, easy/5),
			makeBoolTest("feral(lassie)", true, null, easy/5),
			makeListTest("feral(X)", "tramp,lassie", false, null, easy/5),

			makeListTest("feral(X)", "tramp,lassie", true, null, easy),
//			new Test("quit Prolog",
//					new TestCode() {
//				@Override public StatusReturn code(Test t) {
//					writeln(curProc.in, "halt.");
//					println(readInput(curProc.out, null, 1000));
//					if (checkProcessTermination(curProc.process, true, 2000)==Integer.MAX_VALUE) {
//						println("Test "+t.name+": Process failed to terminate as expected.  It had to be terminated forcibly.");
//						return new StatusReturn(Status.FAILED_TO_TERMINATE);
//					}
//					return new StatusReturn(Status.SUCCESS);
//				}
//			}, false),
	};
		
}
