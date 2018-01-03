package player;

/**
 * Implementation of the Person used as a logic flag in the Table
 * 
 * @author Brodie Robertson
 * @version 1.0.0
 * @since 1.0.0
 */
public class Dealer extends Person implements Cloneable
{
	/**
	 * Constructor for the dealer, takes the name of the dealer as an argument.
	 * @since 1.0.0
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
	 * @since 1.0.0
	 */
	@Override
	public Dealer clone()
	{
		return (Dealer) super.clone();
	}
}
