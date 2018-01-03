package player;

/**
 * Thrown when the an exception is caused by an interaction with a Player
 * 
 * @author Brodie Robertson
 * @version 1.0.0
 * @since 1.0.0
 */
public class PlayerException extends Exception 
{
	/**
	 * Constructs a PlayerException with a basic detail message.
	 */
	public PlayerException()
	{
		super("Exception in Player");
	}
	
	/**
	 * Constructs a PlayerException with a custom detail message.
	 * 
	 * @param str The detail message.
	 */
	public PlayerException(String str)
	{
		super(str);
	}
}