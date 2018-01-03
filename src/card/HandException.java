package card;

/**
 * Thrown when an exception is caused by an interaction with a hand.
 * 
 * @author Brodie Robertson
 * @version 1.0.0
 * @since 1.0.0
 */
public class HandException extends Exception
{
	/**
	 * Constructs a HandException with a basic detail message.
	 */
	public HandException()
	{
		super("Exception in hand");
	}
	
	/**
	 * Constructs a HandException with a custom detail message.
	 * 
	 * @param str The detail message.
	 */
	public HandException(String str)
	{
		super(str);
	}
}