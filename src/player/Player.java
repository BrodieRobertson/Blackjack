package player;

/**
 * The representation of the player, this class defines a player with 
 * attributes representing the player's wager, insurance, total money, whether
 * the player has bust, the number of times the player has, achieved blackjack,
 * a win, a push, a loss, a bust or surrender, whether the player has 
 * surrendered and there starting money.
 * 
 * @author Brodie Robertson
 *
 */
public class Player extends Person implements Cloneable
{
	/**
	 * The player's wager.
	 */
	private double wager;
	/**
	 * The player's insurance.
	 */
	private double insurance;
	/**
	 * The player's total money
	 */
	private double totalMoney;
	/**
	 * Whether the player has bust.
	 */
	private boolean busted;
	/**
	 * Number of times blackjack has been achieved.
	 */
	private int blackjack;
	/**
	 * Number of wins.
	 */
	private int win;
	/**
	 * Number of pushes.
	 */
	private int push;
	/**
	 * Number of losses.
	 */
	private int loss;
	/**
	 * Number of busts.
	 */
	private int bust;
	/**
	 * Number of surrenders.
	 */
	private int surrender;
	/**
	 * Whether the player has surrendered.
	 */
	private boolean surrendered;
	/**
	 * The player's starting money.
	 */
	private static final int STARTINGMONEY = 1000;
	
	/**
	 * Constructor for the player, takes the name of the player as an argument.
	 */
	public Player(String name)
	{
		super(name);
		try 
		{
			setTotalMoney(STARTINGMONEY);
		} 
		catch (PlayerException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
		busted = false;
	}
	
	/**
	 * The String representation of the player in the form
	 * <br><pre>
	 * Total Money: 968.0 Wager: 32.0 Insurance: 0.0 Human 0
     * Hand:
	 * Hand Score: 15
	 * BLACK SIX of SPADES 6
	 * RED NINE of DIAMONDS 9
	 * </pre>
	 * 
	 * @return String represention of the player.
	 * 
	 * (non-Javadoc)
	 * @see player.Person#toString()
	 */
	@Override
	public String toString()
	{
		return "Total Money: " + totalMoney +  " Wager: " + wager + 
				" Insurance: " + insurance + " " + super.toString();
	}

	/**
	 * Checks if the player is equal to another object.
	 * 
	 * @param obj The object to compare the player with.
	 * @return Whether the objects are equal.
	 * 
	 * (non-Javadoc)
	 * @see player.Person#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
		{
			return true;
		}
		if (!super.equals(obj))
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		Player other = (Player) obj;
		if (blackjack != other.blackjack)
		{
			return false;
		}
		if (bust != other.bust)
		{
			return false;
		}
		if (busted != other.busted)
		{
			return false;
		}
		if (Double.doubleToLongBits(insurance) != Double.doubleToLongBits(other.insurance))
		{
			return false;
		}
		if (loss != other.loss)
		{
			return false;
		}
		if (push != other.push)
		{
			return false;
		}
		if (surrender != other.surrender)
		{
			return false;
		}
		if (surrendered != other.surrendered)
		{
			return false;
		}
		if (Double.doubleToLongBits(totalMoney) != Double.doubleToLongBits(other.totalMoney))
		{
			return false;
		}
		if (Double.doubleToLongBits(wager) != Double.doubleToLongBits(other.wager))
		{
			return false;
		}
		if (win != other.win)
		{
			return false;
		}
		return true;
	}

	/**
	 * Clone method for the player.
	 * 
	 * @return A copy of the player.
	 * 
	 * (non-Javadoc)
	 * @see player.Person#clone()
	 */
	@Override
	public Player clone()
	{
		return (Player) super.clone();
	}
	
	/**
	 * Gets the player's wager.
	 * 
	 * @return The player's wager.
	 */
	public double getWager()
	{
		return wager;
	}
	
	/**
	 * Set the wager of the player, throws an exception if the wager is 
	 * negative.
	 * 
	 * @param wager The player's new wager.
	 * @throws PlayerException If the wager is negative.
	 */
	public void setWager(double wager) throws PlayerException
	{
		try
		{
			if(wager < 0)
			{
				throw new PlayerException();
			}
		}
		catch(PlayerException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		//If the current wager minus the new wager is greater than or equal to
		//zero.
		if(this.wager - wager >= 0)
		{;
			setTotalMoney(totalMoney - wager);
			this.wager = wager;
		}
		//If the current wager minus the new wager less than zero.
		else
		{
			setTotalMoney(totalMoney - (wager - this.wager));
			this.wager = wager;
		}
	}
	
	/**
	 * Gets the player's insurance.
	 * 
	 * @return The player's insurance.
	 */
	public double getInsurance()
	{
		return insurance;
	}
	
	/**
	 * Sets the player's insurance, throws an exception if the insurance is
	 * negative.
	 * 
	 * @param insurance The player's new insurance.
	 * @throws PlayerException If the insurance is negative.
	 */
	public void setInsurance(double insurance) throws PlayerException
	{
		if(insurance < 0)
		{
			throw new PlayerException("Invalid amount of insurance: " + insurance);
		}
		
		setTotalMoney(totalMoney - insurance);
		this.insurance = insurance;
	}
	
	/**
	 * Gets the player's total money.
	 * 
	 * @return The player's total money.
	 */
	public double getTotalMoney()
	{
		return totalMoney;
	}
	
	/**
	 * Sets the player's total money.
	 * 
	 * @param totalMoney The player's new total money.
	 * @throws If the total money is negative.
	 */
	public void setTotalMoney(double totalMoney) throws PlayerException
	{
		if(totalMoney < 0)
		{
			throw new PlayerException("Total money less than 0: " + 
						totalMoney);	
		}
		
		this.totalMoney = totalMoney;
	}
	
	/**
	 * Gets whether the player is bust.
	 * 
	 * @return Whether the player is bust.
	 */
	public boolean getBusted()
	{
		return busted;
	}
	
	/**
	 * Sets whether the player is bust.
	 * 
	 * @param bust Whether the player is now bust.
	 */
	public void setBust(boolean busted)
	{
		this.busted = busted;
	}
	
	/**
	 * Gets the number of times the player has achieved blackjack.
	 * 
	 * @return The number of times the player has achieved blackjack.
	 */
	public int getBlackjack()
	{
		return blackjack;
	}
	
	/**
	 * Sets the number of times the player has achieved blackjack, the program
	 * ends if the number of blackjack is negative.
	 * 
	 * @param blackjack The new number of times the player has achieved 
	 * blackjack.
	 */
	public void setBlackjack(int blackjack)
	{
		try
		{
			if(blackjack < 0)
			{
				throw new PersonException("Number of blackjack must be greater than 0");
			}
		}
		catch(PersonException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		this.blackjack = blackjack;
	}
	
	/**
	 * Gets the number wins. 
	 * 
	 * @return The number of wins.
	 */
	public int getWin()
	{
		return win;
	}
	
	/**
	 * Sets the number of wins, the program ends if the number of wins is 
	 * negative.
	 * 
	 * @param win The new number of wins.
	 */
	public void setWin(int win)
	{
		try
		{
			if(win < 0)
			{
				throw new PersonException("Number of wins must be greater than 0");
			}
		}
		catch(PersonException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		this.win = win;
	}
	
	/**
	 * Gets the number of push.
	 * 
	 * @return The number of push.
	 */
	public int getPush()
	{
		return push;
	}
	
	/**
	 * Sets the number of push, the program ends if the number of push is
	 * negative.
	 * 
	 * @param push The new number of push.
	 */
	public void setPush(int push)
	{
		try
		{
			if(push < 0)
			{
				throw new PersonException("Number of pushes must be greater than 0");
			}
		}
		catch(PersonException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		this.push = push;
	}
	
	/**
	 * Gets the number of loss.
	 * 
	 * @return The number of loss.
	 */
	public int getLoss()
	{
		return loss;
	}
	
	/**
	 * Sets the number of loss, the program ends if the number of loss is
	 * negative.
	 * 
	 * @param loss The new number of loss.
	 */
	public void setLoss(int loss)
	{
		try
		{
			if(loss < 0)
			{
				throw new PersonException("Number of losses must be greater than 0");
			}
		}
		catch(PersonException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		this.loss = loss;
	}
	
	/**
	 * Gets the number of bust.
	 * 
	 * @return The number of bust.
	 */
	public int getBust()
	{
		return bust;
	}
	
	/**
	 * Sets the number of bust, the program ends if the number of bust is
	 * negative.
	 * 
	 * @param bust The new number of bust.
	 */
	public void setBust(int bust)
	{
		try
		{
			if(bust < 0)
			{
				throw new PersonException("Number of busts must be greater than 0");
			}
		}
		catch(PersonException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		this.bust = bust;
	}
	
	/**
	 * Gets the number of surrenders.
	 * 
	 * @return The number of surrenders.
	 */
	public int getSurrender()
	{
		return surrender;
	}
	
	/**
	 * Sets the number of surrenders, the program ends if the number of 
	 * surrenders is negative.
	 * 
	 * @param surrender The new number of surrenders.
	 */
	public void setSurrender(int surrender)
	{
		try
		{
			if(surrender < 0)
			{
				throw new PersonException("Number of surrenders must be greater than 0");
			}
		}
		catch(PersonException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		this.surrender = surrender;
	}
	
	/**
	 * Gets whether the player has surrendered.
	 * 
	 * @return Whether the player has surrendered.
	 */
	public boolean getSurrendered()
	{
		return surrendered;
	}
	
	/**
	 * Sets whether the player has surrendered.
	 * 
	 * @param surrender Whether the player has surrendered.
	 */
	public void setSurrendered(boolean surrendered)
	{
		this.surrendered = surrendered;
	}
}
