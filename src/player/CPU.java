package player;

/**
 * The representation of the CPU, used as a flag for game logic.
 * 
 * @author Brodie Robertson
 *
 */
public class CPU extends Player implements Cloneable
{
	/**
	 * Constructor for CPU, takes the name of the CPU as an argument.
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
	 */
	@Override
	public CPU clone()
	{
		return (CPU) super.clone();
	}
}
