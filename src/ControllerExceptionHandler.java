/**
 * Prevents exceptions from traversing to the views by responding to them on the controller level.
 * This class allows individual exceptions' dumping to be prioritized within each catch block.
 * Then, by changing a central verbosity control, the user can customize which notifications the compiled code will display.
 * @author Sol Boucher <slb1566@rit.edu>
 */
public class ControllerExceptionHandler
{
	/** Controls the minimum verbosity level on which the system will act. */
	public static final Verbosity VERBOSITY=Verbosity.FATAL;

	/** The possible verbosities levels. */
	public static enum Verbosity
	{
		/**
		 * Announces that execution can no longer continue.
		 * First, prints the exception's stack trace to standard error.
		 * Then, immediately brings the entire program to a halt with nonzero exit status.
		 * Cannot be blocked with any <tt>VERBOSITY</tt> level.
		 */
		FATAL,

		/**
		 * Indicates a severe problem or one that the caller cannot communicate back to the view which sent it the request.
		 * First, prints the exception's stack trace to standard error.
		 * Then, continues the flow of execution; at this point, the caller likely silently ignores the request it received.
		 * Can be blocked with a <tt>VERBOSITY</tt> level of <tt>FATAL</tt>.
		 */
		ERROR,

		/**
		 * Indicates a problem that the caller will communicate back to the view which made the failing request.
		 * This is intended for use when failure at a certain point is anticipated, but hasn't been produced by any test.
		 * Prints the exception name and the top of the stack trace showing the controller method in which it occurred.
		 * Can be blocked with a <tt>VERBOSITY</tt> level of either <tt>ERROR</tt> or <tt>FATAL</tt>.
		 */
		WARN,

		/**
		 * Indicates a well-tested, relatively harmless error such as bad input that was intercepted before it could do harm.
		 * Such messages might expose the view for improperly validating user input input so as to return malformatted data.
		 * Prints only the top of the stack trace showing the controller method that experienced the warning.
		 * Can be blocked with any <tt>VERBOSITY</tt> level, with the exception of <tt>INFO</tt> itself.
		 */
		INFO;
	};

	/** The exit error code with which the program terminates upon publishing a FATAL signal. */
	private static final int ERROR_EXIT_CODE=1;
	
	/**
	 * Allows a controller class to register something that's concerning it.
	 * Depending on the verbosity level of the message, this class may be centrally configured to simply ignore it.
	 * @param classification the relative severity of the problem, dictating the steps that will be taken (if any)
	 * @param problem the exception that was originally caught within the controller
	 * @return whether or not any action was taken based on the centrally-configured verbosity interest level
	 */
	public static final boolean registerConcern(Verbosity classification, Exception problem)
	{
		if(VERBOSITY.compareTo(classification)<0) //filtered out as insignificant
			return false;
		
		System.err.print(classification.name()+": ");
		switch(classification)
		{
			case FATAL:
				problem.printStackTrace();
				System.exit(ERROR_EXIT_CODE);
				break;
			case ERROR:
				problem.printStackTrace();
				break;
			case WARN:
				System.err.println(problem);
				System.err.println(problem.getStackTrace()[0]);
				break;
			case INFO:
				System.err.println(problem.getStackTrace()[0]);
				break;
		}
		System.err.println();
		
		return false;
	}
}
