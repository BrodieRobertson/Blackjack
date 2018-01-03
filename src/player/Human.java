package player;

/**
 * Implementation of the Player used to flag game logic in the Table.
 * 
 * @author Brodie Robertson
 * @version 1.0.0
 * @since 1.0.0
 */
public class Human extends Player implements Cloneable
{	
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
	 * @since 1.0.0
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
	 * @since 1.0.0
	 */
	@Override
	public Human clone()
	{
		return (Human) super.clone();
	}
}
