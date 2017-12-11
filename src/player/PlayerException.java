package player;

/**
 * Exception to handle the player exceptions.
 * 
 * @author Brodie Robertson
 *
 */
public class PlayerException extends Exception 
{
	/**
	 * Constructor for the player exception.
	 */
	public PlayerException()
	{
		super("Exception in Player");
	}
	
	/**
	 * Constructor for the player exception with a string parameter.
	 * 
	 * @param str The message for the player exception.
	 */
	public PlayerException(String str)
	{
		super(str);
	}
}
