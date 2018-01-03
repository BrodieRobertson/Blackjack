package card;

/**
 * Thrown when an exceptions is caused by an interaction with a deck.
 * 
 * @author Brodie Robertson
 * @version 1.0.0
 * @since 1.0.0
 */
public class DeckException extends Exception
{
	/**
	 * Constructs a deck with a basic detail message.
	 */
	public DeckException()
	{
		super("Exception in deck");
	}
	
	/**
	 * Constructs a deck with a custom detail message.
	 * 
	 * @param str The detail message.
	 */
	public DeckException(String str)
	{
		super(str);
	}
}