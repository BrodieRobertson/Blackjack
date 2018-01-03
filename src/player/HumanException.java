package player;

/**
 * Thrown when an exception is caused by an interaction with a human.
 * 
 * @author Brodie Robertson
 * @version 1.0.0
 * @since 1.0.0
 */
public class HumanException extends Exception
{
	/**
	 * Constructs a HumanException with a basic detail message.
	 * 
	 * @since 1.0.0
	 */
	public HumanException() 
	{
		super("Exception in Human");
	}
	
	/**
	 * Constructs a HumanException with a custom detail message.
	 * 
	 * @param str The detail message.
	 * @since 1.0.0
	 */
	public HumanException(String str)
	{
		super(str);
	}
}