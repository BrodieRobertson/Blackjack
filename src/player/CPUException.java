package player;

/**
 * Thrown when an exception is caused by an interaction a CPU.
 * 
 * @author Brodie Robertson
 * @version 1.0.0
 * @since 1.0.0
 */
public class CPUException extends Exception
{
	/**
	 * Constructs a CPUException with a basic detail message.
	 * 
	 * @since 1.0.0
	 */
	public CPUException() 
	{
		super("Exception in CPU");
	}
	
	/**
	 * Constructs a CPUException with a custom detail message.
	 * 
	 * @param str The detail message.
	 * @since 1.0.0
	 */
	public CPUException(String str)
	{
		super(str);
	}
}
