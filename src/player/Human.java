package player;

/**
 * The representation of a human, used as a flag for game logic.
 * 
 * @author Brodie Robertson
 *
 */
public class Human extends Player implements Cloneable
{
	/**
	 * The constructor for the human, takes the name of the human as an
	 * argument.
	 */
	public Human() 
	{
		
	}
	
	/**
	 * String representation of the human in the form
	 * <br><pre>
	 * Human Total Money: 968.0 Wager: 32.0 Insurance: 0.0 Human 0
     * Hand:
	 * Hand Score: 15
	 * BLACK SIX of SPADES 6
	 * RED NINE of DIAMONDS 9
	 * </pre>
	 * 
	 * @return String representation of the human.
	 * 
	 * (non-Javadoc)
	 * @see player.Player#toString()
	 */
	@Override
	public String toString()
	{
		return "Human " + super.toString();
	}
	
	/**
	 * Clone method for the human.
	 * 
	 * @return A clone of the human.
	 * 
	 * (non-Javadoc)
	 * @see player.Player#clone()
	 */
	@Override
	public Human clone()
	{
		return (Human) super.clone();
	}
}
