package player;

/**
 * The representation of a dealer, used as a flag for game logic.
 * 
 * @author Brodie Robertson
 *
 */
public class Dealer extends Person implements Cloneable
{
	/**
	 * Constructor for the dealer, takes the name of the dealer as an argument.
	 */
	public Dealer(String name) 
	{
		try
		{
			setName(name);
		} 
		catch (PersonException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Clone method for the dealer.
	 * 
	 * @return A copy of the dealer.
	 * 
	 * (non-Javadoc)
	 * @see player.Person#clone()
	 */
	@Override
	public Dealer clone()
	{
		return (Dealer) super.clone();
	}
}
