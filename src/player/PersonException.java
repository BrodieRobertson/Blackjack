package player;

/**
 * Exception type to handle person exceptions.
 * 
 * @author Brodie Robertson
 *
 */
public class PersonException extends Exception 
{
	/**
	 * Constructor for the person exception.
	 */
	public PersonException() 
	{
		super("Exception in Person");
	}

	/**
	 * Constructor for the person exception, with string parameter.
	 * 
	 * @param str The message for the person exception.
	 */
	public PersonException(String str) 
	{
		super(str);
	}
}
