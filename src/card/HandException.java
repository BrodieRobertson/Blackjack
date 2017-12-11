package card;

/**
 * Exception type to handle hand exceptions.
 * 
 * @author Brodie Robertson
 *
 */
public class HandException extends Exception
{
	/**
	 * The constructor for the hand exception with a string parameter.
	 * 
	 * @param str The message for the hand exception.
	 */
	public HandException(String str)
	{
		super(str);
	}
	
	/**
	 * The constructor for the hand exception.
	 */
	public HandException()
	{
		super("Exception in hand");
	}

}
