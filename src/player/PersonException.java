package player;

/**
 * Thrown when an exception is caused by an interaction with a Person.
 * 
 * @author Brodie Robertson
 * @version 1.0.0
 * @since 1.0.0
 */
public class PersonException extends Exception 
{
	/**
	 * Constructs a PersonException with basic detail message.
	 * 
	 * @since 1.0.0
	 */
	public PersonException() 
	{
		super("Exception in Person");
	}

	/**
	 * Constructs a PersonException with a custom detail message.
	 * 
	 * @param str The detail message.
	 * @since 1.0.0
	 */
	public PersonException(String str) 
	{
		super(str);
	}
}
