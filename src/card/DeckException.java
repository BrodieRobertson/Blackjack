package card;

/**
 * Exception type to handle deck exceptions.
 * 
 * @author Brodie Robertson
 *
 */
public class DeckException extends Exception
{
	/**
	 * Constructor for the deck exception with a string parameter.
	 * 
	 * @param str The message held by the deck exception.
	 */
	public DeckException(String str)
	{
		super(str);
	}
	
	/**
	 * Constructor for the deck exception.
	 */
	public DeckException()
	{
		super("Exception in deck");
	}
}
