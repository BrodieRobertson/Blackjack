package player;

/**
 * Implementation of the Player used as a logic flag in the Table.
 * 
 * @author Brodie Robertson
 * @version 1.0.0
 * @since 1.0.0
 */
public class CPU extends Player implements Cloneable
{
	/**
	 * Constructor for CPU, takes the name of the CPU as an argument.
	 * 
	 * @param name Name of the CPU.
	 * @since 1.0.0
	 */
	public CPU(String name) 
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
	 * The String representation of the CPU
	 * <br><pre>
	 * CPU Total Money: 875.0 Wager: 50.0 Insurance: 0.0 CPU 0
	 * Hand:
     * Hand Score: 14
     * BLACK SEVEN of SPADES 7
     * RED SEVEN of DIAMONDS 7
     * </pre>
     * 
     * @return String representation of the CPU.
	 * 
	 * (non-Javadoc)
	 * @see player.Player#toString()
	 * @since 1.0.0
	 */
	@Override
	public String toString()
	{
		return "CPU " + super.toString();
	}
	
	/**
	 * Clone method for the CPU.
	 * 
	 * @return A copy of the CPU.
	 * 
	 * (non-Javadoc)
	 * @see player.Player#clone()
	 * @since 1.0.0
	 */
	@Override
	public CPU clone()
	{
		return (CPU) super.clone();
	}
}
