package player;

/**
 * Abstract definition of a player providing the necessary methods for use 
 * with the table.
 * 
 * @author Brodie Robertson
 * @version 1.6.0
 * @since 1.0.0
 */
public abstract class Player extends Person implements Cloneable
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
	 * The player's total money.
	 */
	private double totalMoney;
	/**
	 * The player's total wagers.
	 */
	private double totalWager;
	/**
	 * The player's total insurance payments.
	 */
	private double totalInsurance;
	/**
	 * The player's total winnings.
	 */
	private double totalWinnings;
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
	 * Whether the player is bankrupt.
	 */
	private boolean bankrupt;
	/**
	 * Whether the player has taken insurance.
	 */
	private boolean tookInsurance;
	/**
	 * The player's current Blackjack winnings.
	 */
	private double currentBlackjack;
	/**
	 * Whether the player has Blackjack.
	 */
	private boolean hasBlackjack;
	/**
	 * The player's current standard winnings.
	 */
	private double currentWin;
	/**
	 * Whether the player currently has a standard win.
	 */
	private boolean hasWin;
	/**
	 * The player's starting money.
	 */
	public static final int STARTING_MONEY = 1000;
	
	/**
	 * Constructs the Player with a set of default values.
	 * 
	 * @since 1.0.0
	 */
	public Player()
	{
		try 
		{
			setTotalMoney(STARTING_MONEY);
		} 
		catch (PlayerException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
		busted = false;
		surrendered = false;
		bankrupt = false;
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
	 * @since 1.0.0
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
	 * @since 1.0.0
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
	 * @since 1.0.0
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
	 * @since 1.0.0
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
	 * @since 1.0.0
	 */
	public void setWager(double wager) throws PlayerException
	{
		if(this.wager > 0)
		{
			if(wager < 0 || wager - this.wager > totalMoney)
			{
				throw new PlayerException("Invalid wager: " + wager);
			}
		}
		else
		{
			if(wager < 0 || wager > totalMoney)
			{
				throw new PlayerException("Invalid wager: " + wager);
			}
		}
		
		//If the current wager minus the new wager is greater than or equal to
		//zero.
		if(this.wager - wager >= 0)
		{
			setTotalMoney(totalMoney - wager);
			setTotalWager(totalWager + wager);
			this.wager = wager;
		}
		//If the current wager minus the new wager less than zero.
		else
		{
			setTotalMoney(totalMoney - (wager - this.wager));
			setTotalWager(totalWager + (wager - this.wager));
			this.wager = wager;
		}
	}
	
	/**
	 * Gets the player's insurance.
	 * 
	 * @return The player's insurance.
	 * @since 1.0.0
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
	 * @since 1.0.0
	 */
	public void setInsurance(double insurance) throws PlayerException
	{
		if(insurance < 0 || insurance > totalMoney)
		{
			throw new PlayerException("Invalid amount of insurance: " + insurance);
		}
		
		setTotalMoney(totalMoney - insurance);
		setTotalInsurance(totalInsurance + insurance);
		this.insurance = insurance;
	}
	
	/**
	 * Gets the player's total money.
	 * 
	 * @return The player's total money.
	 * @since 1.0.0
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
	 * @since 1.0.0
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
	 * Gets the player's total wagers.
	 * 
	 * @return The player's total wagers.
	 * @since 1.0.0
	 */
	public double getTotalWager()
	{
		return totalWager;
	}
	
	/**
	 * Sets the player's total of wagers.
	 * 
	 * @param totalWager The player's new total of wager
	 * @throws PlayerException If the total wager is negative.
	 * @since 1.0.0
	 */
	private void setTotalWager(double totalWager) throws PlayerException
	{
		if(totalWager < 0)
		{
			throw new PlayerException("Total wager can't be less than 0: " + 
						totalWager);
		}
		
		this.totalWager = totalWager;
	}
	
	/**
	 * Gets the player's total insurance payments.
	 * 
	 * @return The player's total insurance payments.
	 * @since 1.0.0
	 */
	public double getTotalInsurance()
	{
		return totalInsurance;
	}
	
	/**
	 * Sets the player's total insurance.
	 * 
	 * @param totalInsurance The player's new total insurance payments.
	 * @throws PlayerException If the total insurance is negative.
	 * @since 1.0.0
	 */
	private void setTotalInsurance(double totalInsurance) throws PlayerException
	{
		if(totalInsurance < 0)
		{
			throw new PlayerException("Total insurance can't be less than 0: " + 
						totalInsurance);
		}
		
		this.totalInsurance = totalInsurance;
	}
	
	/**
	 * Gets the player's total winnings.
	 * 
	 * @return The player's total winnings.
	 * @since 1.0.0
	 */
	public double getTotalWinnings()
	{
		return totalWinnings;
	}
	
	/**
	 * Sets the player's total winnings.
	 * 
	 * @param totalWinnings The player's total winnings.
	 * @throws PlayerException If the total winning is negative.
	 * @since 1.0.0
	 */
	public void setTotalWinnnings(double totalWinnings) throws PlayerException
	{
		if(totalWinnings < 0)
		{
			throw new PlayerException("Total winnings can't be less than 0: " + 
					totalWinnings);
		}
		
		this.totalWinnings = totalWinnings;
	}
	
	/**
	 * Gets whether the player is bust.
	 * 
	 * @return Whether the player is bust.
	 * @since 1.0.0
	 */
	public boolean getBusted()
	{
		return busted;
	}
	
	/**
	 * Sets whether the player is bust.
	 * 
	 * @param bust Whether the player is now bust.
	 * @since 1.0.0
	 */
	public void setBusted(boolean busted)
	{
		this.busted = busted;
	}
	
	/**
	 * Gets the number of times the player has achieved blackjack.
	 * 
	 * @return The number of times the player has achieved blackjack.
	 * @since 1.0.0
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
	 * @since 1.0.0
	 */
	public void setBlackjack(int blackjack)
	{
		try
		{
			if(blackjack < 0)
			{
				throw new PlayerException("Number of blackjack must be greater than 0");
			}
		}
		catch(PlayerException e)
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
	 * @since 1.0.0
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
	 * @since 1.0.0
	 */
	public void setWin(int win)
	{
		try
		{
			if(win < 0)
			{
				throw new PlayerException("Number of wins must be greater than 0");
			}
		}
		catch(PlayerException e)
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
	 * @since 1.0.0
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
	 * @since 1.0.0
	 */
	public void setPush(int push)
	{
		try
		{
			if(push < 0)
			{
				throw new PlayerException("Number of pushes must be greater than 0");
			}
		}
		catch(PlayerException e)
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
	 * @since 1.0.0
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
	 * @since 1.0.0
	 */
	public void setLoss(int loss)
	{
		try
		{
			if(loss < 0)
			{
				throw new PlayerException("Number of losses must be greater than 0");
			}
		}
		catch(PlayerException e)
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
	 * @since 1.0.0
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
	 * @since 1.0.0
	 */
	public void setBust(int bust)
	{
		try
		{
			if(bust < 0)
			{
				throw new PlayerException("Number of busts must be greater than 0");
			}
		}
		catch(PlayerException e)
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
	 * @since 1.0.0
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
	 * @since 1.0.0
	 */
	public void setSurrender(int surrender)
	{
		try
		{
			if(surrender < 0)
			{
				throw new PlayerException("Number of surrenders must be greater than 0");
			}
		}
		catch(PlayerException e)
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
	 * @since 1.0.0
	 */
	public boolean getSurrendered()
	{
		return surrendered;
	}
	
	/**
	 * Gets whether the player is bankrupt.
	 * 
	 * @return Whether the player is bankrupt.
	 * @since 1.1.0
	 */
	public boolean getBankrupt()
	{
		return bankrupt;
	}
	
	/**
	 * Sets whether the player is bankrupt.
	 * 
	 * @param bankrupt Whether the player is now bankrupt.
	 * @since 1.1.0
	 */
	public void setBankrupt(boolean bankrupt)
	{
		this.bankrupt = bankrupt;
	}
	
	/**
	 * Sets whether the player has surrendered.
	 * 
	 * @param surrender Whether the player has surrendered.
	 * @since 1.0.0
	 */
	public void setSurrendered(boolean surrendered)
	{
		this.surrendered = surrendered;
	}
	
	/**
	 * Gets whether the player has taken insurance.
	 * 
	 * @return Whether the player has taken insurance.
	 * @since 1.3.0
	 */
	public boolean getTookInsurance()
	{
		return tookInsurance;
	}
	
	/**
	 * Sets whether the player has taken insurance.
	 * 
	 * @param tookInsurance Whether the player has taken insurance.
	 * @since 1.0.0 
	 */
	public void setTookInsurance(boolean tookInsurance)
	{
		this.tookInsurance = tookInsurance;
	}
	
	/**
	 * Gets the player's current standard winnings.
	 * 
	 * @return The player's current winnings.
	 * @since 1.3.0
	 */
	public double getCurrentWin()
	{
		return currentWin;
	}
	
	/**
	 * Sets the player's current standard winnings.
	 * 
	 * @param currentWin The player's current winnings.
	 * @since 1.3.0
	 */
	public void setCurrentWin(double currentWin)
	{
		try
		{
			if(currentWin < 0)
			{
				throw new PlayerException("Current winnings can't be less than"
						+ " or equal to 0");
			}
		}
		catch(PlayerException ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		
		this.currentWin = currentWin;
	}
	
	/**
	 * Gets whether the Player has a standard win.
	 * 
	 * @return Whether the Player has a standard win.
	 * @since 1.3.0
	 */
	public boolean getHasWin()
	{
		return hasWin;
	}
	
	/**
	 * Sets whether the Player has a standard win.
	 * 
	 * @param hasWin Whether the Player has a standard win.
	 * @since 1.3.0
	 */
	public void setHasWin(boolean hasWin)
	{
		this.hasWin = hasWin;
	}
	
	/**
	 * Gets Player's current Blackjack winnings.
	 * 
	 * @return Whether the Player has Blackjack.
	 * @since 1.3.0
	 */
	public double getCurrentBlackjack()
	{
		return currentBlackjack;
	}
	
	/**
	 * Sets the Player's current Blackjack winnings.
	 * 
	 * @param currentBlackjack 
	 * @since 1.3.0
	 */
	public void setCurrentBlackjack(double currentBlackjack)
	{
		try
		{
			if(currentBlackjack < 0)
			{
				throw new PlayerException("Current blackjack winnings can't be "
						+ "less than or equal to 0");
			}
		}
		catch(PlayerException ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}	
		
		this.currentBlackjack = currentBlackjack;
	}
	
	/**
	 * Gets whether the Player currently has Blackjack.
	 * 
	 * @return Whether the Player currently has Blackjack.
	 * @since 1.3.0
	 */
	public boolean getHasBlackjack()
	{
		return hasBlackjack;
	}
	
	/**
	 * Sets whether the Player currently has Blackjack.
	 * 
	 * @param hasBlackjack Whether the Player currently has Blackjack.
	 * @since 1.3.0
	 */
	public void setHasBlackjack(boolean hasBlackjack)
	{
		this.hasBlackjack = hasBlackjack;
	}
	
	/**
	 * Checks whether the Player can split their hand.
	 * 
	 * @return Whether the Player can split their hand.
	 * @since 1.6.0
	 */
	public boolean canSplit()
	{
		if(getHand(0).getCards().length == 2 && 
			(getHand(0).getCard(0).getFace() == getHand(0).getCard(1).getFace() || 
			getHand(0).getCard(0).getValue() == getHand(0).getCard(1).getValue())
			&& getTotalMoney() >= getWager())
		{
			return true;
		}
		return false;
	}
}
