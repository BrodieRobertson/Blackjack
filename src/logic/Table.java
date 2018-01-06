package logic;
import java.util.ArrayList;

import card.*;
import player.*;

/**
 * The logic layer for the game, manages all of the back end data manipulation.
 * 
 * @author Brodie Robertson
 * @version 1.4.0
 * @since 1.0.0
 */
public class Table 
{
	/**
	 * Return rate of an insurance bet.
	 */
	public static final int INSURANCE_RETURN = 2;
	/**
	 * Minimum number of players.
	 */
	public static final int MINNUMPLAYERS = 1;
	/**
	 * Maximum number of player's
	 */
	public static final int MAXNUMPLAYERS = 6;
	/**
	 * Minimum allowable wager.
	 */
	public static final double MINWAGER = 25;
	/**
	 * Maximum allowable wager.
	 */
	public static final double MAXWAGER = 1000;
	/**
	 * Card value of blackjack.
	 */
	public static final int BLACKJACK = 21;
	/**
	 * Player's in the game.
	 */
	private Person[] players;
	/**
	 * Deck used by all players.
	 */
	private Deck deck;
	/**
	 * The current round.
	 */
	private int currentRound = 1;
	/**
	 * The absolute total number of rounds.
	 */
	private int totalRounds;
	/**
	 * The number of completed rounds.
	 */
	private int completedRounds;
	
	/**
	 * Constructs a Table with a single deck. Will be changed in future update.
	 * 
	 * @since 1.0.0
	 */
	public Table()
	{
		deck = new Deck(1);
	}
	
	/**
	 * Creates an array of player's with the parameters specified.
	 * 
	 * @param numOfHumanPlayers The number of human players.
	 * @param numOfCPUPlayers The number of CPU players.
	 * @since 1.0.0
	 */
	public void createPlayers(int numOfHumanPlayers, int numOfCPUPlayers) throws 
	PlayerException, HumanException, CPUException
	{
		//Throws exception if total number of players greater than or less 
		//than the required number of players.
		if(numOfHumanPlayers + numOfCPUPlayers > MAXNUMPLAYERS || numOfHumanPlayers + 
				numOfCPUPlayers < MINNUMPLAYERS)
		{
			throw new PlayerException("Invalid number of players: " + 
					(numOfHumanPlayers + numOfCPUPlayers));
		}
		
		//Throws exception if the number of human players greater than or less
		//than the required number of players.
		if(numOfHumanPlayers < MINNUMPLAYERS || numOfHumanPlayers > 
			MAXNUMPLAYERS)
		{
			throw new HumanException("Invalid number of human players: " + 
				numOfHumanPlayers);
		}
		
		//Throws exception if the number of CPU players greater than or less
		//than the required number of players.
		if(numOfCPUPlayers < 0 || numOfCPUPlayers > MAXNUMPLAYERS)
		{
			throw new CPUException("Invalid number of AI player: " + 
					numOfCPUPlayers);
		}
		
		int playerIndex = 0;
		//Creates the human players.
		players = new Person[numOfHumanPlayers + numOfCPUPlayers + 1];
		for(int i = 0; i < numOfHumanPlayers; i++)
		{
			players[playerIndex] = new Human();
			playerIndex++;
		}
		
		//Creates the AI players.
		for(int i = 0; i < numOfCPUPlayers; i++)
		{
			players[playerIndex] = new CPU("CPU " + i);
			playerIndex++;
		}
		
		players[playerIndex] = new Dealer("Dealer");
	}
	
	/**
	private void displayGameStats()
	{
		//Displays the final statistics after the end of the game.
		System.out.println("Overall Game Statistics:");
		System.out.println("Minimum Wager: " + MINWAGER);
		System.out.println("Maximum Wager: " + MAXWAGER);
		System.out.println("Number of players: " + (players.length - 1));
		System.out.println("Completed Rounds: " + (completedRounds + 1));
		for(int i = 0; i < players.length - 1; i++)
		{
			Player player = (Player)players[i];
			System.out.println();
			System.out.println("Name: " + player.getName());
			if(player.getBankrupt())
			{
				System.out.println("Bankrupt");
			}
			System.out.println("Money Remaining: " + player.getTotalMoney());
			System.out.println("Total Winnings: " + player.getTotalWinnings());
			System.out.println("Total Wagers: " + player.getTotalWager());
			System.out.println("Total Insurance: " + player.getTotalInsurance());
			System.out.println("Wins: " + player.getWin());
			System.out.println("Losses: " + player.getLoss());
			System.out.println("Pushes: " + player.getPush());
			System.out.println("Blackjacks: " + player.getBlackjack());
			System.out.println("Busts: " + player.getBust());
			System.out.println("Surrenders: " + player.getSurrender());
		}
	}
	*/
	
	/**
	 * Checks if all of the player's have gone bankrupt.
	 * 
	 * @return Whether all of the player's have gone bankrupt.
	 */
	public boolean checkIfAnyPlayerNotBankrupt()
	{
		for(int i = 0; i < players.length - 1; i++)
		{
			Player player = (Player)players[i];
			if(!player.getBankrupt())
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the number of completed rounds.
	 * 
	 * @return The number of completed rounds.
	 * @since 1.4.0
	 */
	public int getCompletedRounds()
	{
		return completedRounds;
	}
	
	/**
	 * Sets the number of completed rounds.
	 * 
	 * @param completedRounds The number of completed rounds.
	 * @since 1.4.0
	 */
	public void setCompletedRounds(int completedRounds)
	{
		try
		{
			if(completedRounds < 0)
			{
				throw new TableException("Completed round can't be less than 0: " 
						+ completedRounds);
			}
			
			if(completedRounds > totalRounds)
			{
				throw new TableException("Completed round can't be greater than "
						+ "the total rounds: " + completedRounds);
			}
			
			if(completedRounds > currentRound)
			{
				throw new TableException("Completed round can't be greater than " 
						+ "than the current round: " + currentRound);
			}
		}
		catch(TableException ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		
		this.completedRounds = completedRounds;
	}
	
	/**
	 * Gets the current round.
	 * 
	 * @return The current round.
	 * @since 1.4.0
	 */
	public int getCurrentRound()
	{
		return currentRound;
	}
	
	/**
	 * Sets the current round.
	 * 
	 * @param currentRound The current round
	 * @since 1.4.0
	 */
	public void setCurrentRound(int currentRound)
	{
		try
		{
			if(currentRound < 0)
			{
				throw new TableException("Current round can't less than 0: " + currentRound);
			}
			
			if(currentRound > totalRounds)
			{
				throw new TableException("Current round can't be greater than total rounds: " + currentRound);
			}
		}
		catch(TableException ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		
		this.currentRound = currentRound;
	}
	
	/**
	 * Gets the total number of rounds.
	 * 
	 * @return The total number of rounds.
	 * @since 1.4.0
	 */
	public int getTotalRounds()
	{
		return totalRounds;
	}
	
	/**
	 * Sets the total number of rounds, prompts the user for an input until
	 * a valid value has been received.
	 * 
	 * @since 1.0.0
	 */
	public void setTotalRounds(int totalRounds) throws TableException
	{
		if(totalRounds <= 0)
		{
			throw new TableException("Total Rounds less than or equal to 0");
		}
			
		this.totalRounds = totalRounds;
	}
	
	/**
	 * Gets the deck.
	 * 
	 * @return The deck.
	 * @since 1.0.0
	 */
	public Deck getDeck()
	{
		return new Deck(deck);
	}
	
	/**
	 * Gets the list of players.
	 * 
	 * @return The list of players.
	 * @since 1.0.0
	 */
	public Person[] getPlayers()
	{
		Person[] temp = new Person[players.length];
		for(int i = 0; i < players.length; i++)
		{
			if(players[i] != null)
			{
				temp[i] = (Person) players[i].clone();
			}
		}
		
		return temp;
	}
	
	/**
	 * Gets the person at a specified index.
	 * 
	 * @param index The index of the person.
	 * @return A copy of the person.
	 * @since 1.0.0
	 */
	public Person getPersonAtIndex(int index)
	{
		try
		{
			validatePersonIndex(index);
		}
		catch(TableException ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		
		return players[index].clone();
	}
	
	/**
	 * Sets the name of a person at specified index.
	 * 
	 * @param index The index of the person.
	 * @param name The name of the person.
	 * @throws PersonException Thrown if the name is invalid.
	 * @since 1.0.0
	 */
	public void setPersonNameAtIndex(int index, String name) throws PersonException
	{
		try
		{
			validatePersonIndex(index);
		}
		catch(TableException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		players[index].setName(name);
	}
	
	/**
	 * Deals the initial cards to each player and sets the dealers second card
	 * to face down.
	 * 
	 * @since 1.0.0
	 */
	public void deal()
	{
		final int startingCards = 2;
		//Deals 1 card to each player and then deals the 2nd.
		for(int i = 0; i < startingCards; i++)
		{
			for(int j = 0; j < players.length; j++)
			{
				Card card = deck.getCard();
				//If the card being added is the dealer's second card, flip it.
				if(j == players.length - 1 && players[j].getHand(0).
						getCardsRemaining() == 1)
				{
					card.flipCard();
				}
						
				players[j].addToHand(card, 0);
			}
		}
	}
	
	/**
	 * Sets the wager of a human a specific index.
	 * 
	 * @param index The index of the human.
	 * @param wager The wager of the of the human.
	 * @throws PlayerException Thrown if the wager is invalid for the player.
	 * @throws TableException Thrown if the wager is invalid in the game logic.
	 * @since 1.2.0
	 */
	public void setHumanWager(int index, double wager) throws PlayerException, 
	TableException
	{
		try
		{
			validatePersonIndex(index);
			if(!(players[index] instanceof Human))
			{
				throw new PersonException("Error this is not a human");
			}
		}
		catch(TableException | PersonException ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		
		//Throws exception if the wager is less than the minimum or greater
		//the maximum wager.
		if(wager >= MINWAGER && wager <= MAXWAGER)
		{
			Player player = (Player)players[index];
			player.setWager(wager);
		}
		else
		{
			throw new TableException("Invalid wager: " + wager);
		}
	}
	
	/**
	 * Sets the wager of a CPU at a specified index.
	 * 
	 * @param index The index of the CPU.
	 * @return The wager of the CPU.
	 * @since 1.2.0
	 */
	public double setCPUWager(int index)
	{
		try
		{
			validatePersonIndex(index);
			if(!(players[index] instanceof CPU))
			{
				throw new PersonException("Error this is not a cpu");
			}
		}
		catch(TableException | PersonException ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		
		double wager = 50;
		boolean validWager = false;
		Player player = (Player)players[index];
		while(!validWager)
		{
			//Valid wager if it's between the min an max wager and wager less 
			//than or equal to the players total money.
			if(wager >= MINWAGER && wager <= MAXWAGER && wager <= player.
					getTotalMoney())
			{
				validWager = true;
			}
			else
			{
				wager--;
			}
		}
		
		if(wager > MINWAGER)
		{
			try 
			{
				player.setWager(wager);
			} 
			catch (PlayerException ex) 
			{
				ex.printStackTrace();
				System.exit(0);
			}
		}
		
		return wager;
	}
	
	/**
	 * Sets a human wager at a specified index.
	 * 
	 * @param index The index of the human.
	 * @param insurance The insurance of the human.
	 * @throws TableException Thrown if the insurance is invalid in the game logic.
	 * @throws PlayerException Thrown if the insurance is invalid for the player.
	 * @since 1.2.0
	 */
	public void setHumanInsurance(int index, double insurance) throws 
	TableException, PlayerException
	{
		Player player = null;
		try
		{
			validatePersonIndex(index);
			if(!(players[index] instanceof Human))
			{
				throw new PersonException("Error this is not a human");
			}
		}
		catch(TableException | PersonException ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		
		player = (Player) players[index];
		
		//If the Player's hand score is less than Blackjack.
		if(player.getHand(0).getHandScore() < BLACKJACK)
		{
			if(insurance <= player.getWager() / 2)
			{
				player.setInsurance(insurance);
				player.setTookInsurance(true);
			}
			else
			{
				throw new TableException("Invalid insurance: " + insurance);
			}
		}
		//Throws exception if the Player already has Blackjack.
		else if(player.getHand(0).getHandScore() == BLACKJACK)
		{
			throw new PlayerException("Player already has Blackjack");
		}
		//Throws exception if the Player's score is greater than Blackjack.
		else
		{
			throw new PlayerException("Player's score greater than Blackjack");
		}
	}
	
	/**
	 * Sets a CPU wager at a specified index.
	 * 
	 * @param index The index of a CPU.
	 * @return The insurance of the CPU.
	 * @since 1.2.0
	 */
	public double setCPUInsurance(int index)
	{
		Player player = null;
		try 
		{
			validatePersonIndex(index);
			if(!(players[index] instanceof CPU))
			{
				throw new PlayerException("Error this is not a cpu");
			}
		} 
		catch (PlayerException | TableException ex) 
		{
			ex.printStackTrace();
			System.exit(0);
		}
		
		player = (Player)players[index];
		double insurance = player.getWager() / 2;
	
		//Valid insurance if it's less than total money and greater than 0.
		while(insurance > player.getTotalMoney() && insurance > 0)
		{
			if(insurance > player.getTotalMoney())
			{
				insurance--;
			}
		}
		
		if(insurance > 0)
		{
			player.setTookInsurance(true);
			try 
			{
				player.setInsurance(insurance);
			} 
			catch (PlayerException ex) 
			{
				ex.printStackTrace();
				System.exit(0);
			}
		}
		
		return insurance;
	}
	
	/**
	 * Validates the index of a Person.
	 * 
	 * @param index The index of the Person.
	 * @throws TableException Thrown if the index is less than 0 or greater 
	 * than the maximum index.
	 */
	private void validatePersonIndex(int index) throws TableException
	{
		if(index < 0 || index > players.length)
		{
			throw new TableException("Index outside of valid player range");
		}
	}
	
	/**
	 * @return
	 */
	public boolean attemptDealerCardFlip()
	{
		Hand dealerHand = players[players.length - 1].getHand(0);
		//If the dealer's 2nd card's value is 10, flip it.
		if(dealerHand.getCard(1).getValue() == 10)
		{
			players[players.length - 1].flipCardInHand(1, 0);
			return true;
		}
		return false;
	}
	
	/**
	 * Pays outs insurance bets if the dealer has blackjack in his first 2 cards
	 * and sets any insurance bets back to 0 regardless of the result.
	 * 
	 * @since 1.0.0
	 */
	public String insurancePayout(int index)
	{	
		Player player = (Player)players[index];

		//If player has insurance bet
		if(player.getTookInsurance())
		{
			try 
			{
				player.setTotalMoney(player.getTotalMoney() + player.getInsurance() * 
						INSURANCE_RETURN);
				player.setInsurance(0);
				player.setTookInsurance(false);
			} 
			catch (PlayerException e) 
			{
				e.printStackTrace();
				System.exit(0);
			}
			return player.getName() + " wins the insurance bet\n";
		}
		//If player does not have insurance
		else
		{
			//If the player has Blackjack
			if(player.getHand(0).getHandScore() == BLACKJACK)
			{
				try 
				{
					player.setTotalMoney(player.getTotalMoney() + player.getWager());
					player.setPush(player.getPush() + 1);
				} 
				catch (PlayerException e) 
				{
					e.printStackTrace();
					System.exit(0);
				}
				return "Push: " + player.getName() + "'s wager is returned\n";
			}
			//If the player does not have Blackjack
			else
			{
				return player.getName() + " loses this round\n";
			}
		}
	}
	
	/**
	 * Resets the insurance of a Player at a specified index.
	 * 
	 * @param index The index of the Player.
	 * @since 1.0.0
	 */
	public void resetInsurance(int index)
	{
		try
		{
			validatePersonIndex(index);
			Player player = (Player)players[index];
			player.setInsurance(0);
			player.setTookInsurance(false);
		}
		catch(TableException | PlayerException ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * Deals the player a new card and if the the player hasn't bust or reached 
	 * blackjack, they are prompted for a new choice of hit again or stand.
	 * 
	 * @param player The current player.
	 * @param handIndex The index of the hand.
	 * @since 1.0.0
	 */
	public Card hit(int newIndex, int handIndex)
	{
		try 
		{
			validatePersonIndex(newIndex);
		} 
		catch (TableException ex) 
		{
			ex.printStackTrace();
			System.exit(0);
		}
		
		Player player = (Player)players[newIndex];
		Card card = player.addToHand(deck.getCard(), handIndex);
		
		if(player.getHand(handIndex).getHandScore() > BLACKJACK)
		{
			player.setBusted(true);
		}
		
		return new Card(card);
	}
	
	/**
	 * Doubles the player's wager and deals them a new to the specified hand, if 
	 * the player passes blackjack they are bust.
	 * 
	 * @param player The current player.
	 * @param index The index of the hand.
	 * @since 1.0.0
	 */
	public Card doubleDown(int index, int handIndex)
	{
		Player player = null;
		try
		{
			validatePersonIndex(index);
			player = (Player)players[index];
			player.setWager(player.getWager() * 2);
		} 
		catch (PlayerException | TableException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		Card card = player.addToHand(deck.getCard(), handIndex);
		
		//If player surpasses blackjack
		if(player.getHand(handIndex).getHandScore() > BLACKJACK)
		{
			player.setBusted(true);
		}
		
		return card;
	}
	
	/**
	 * Splits the player's hand into 2 hands and doubles their wager.
	 * 
	 * @param player The current player.
	 * @since 1.0.0
	 */
	public void split(int index)
	{
		Player player = null;
		try 
		{
			validatePersonIndex(index);
			player = (Player)players[index];
			player.setWager(player.getWager() * 2);
		} 
		catch (PlayerException | TableException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		//Adds a new hand and moves the player's second card into it.
		player.addHand();
		player.setHandSplit(0);
		player.addToHand(player.getHand(0).getCard(1), 1);
		player.removeFromHand(0, 1);
	}
	
	/**
	 * Returns half of the player's wager but they're unable to win participate
	 * in the rest of this round.
	 * 
	 * @param player The current player.
	 * @since 1.0.0
	 */
	public double surrender(int index)
	{
		double returnedAmount = 0;
		Player player = null;
		//Returns half the player's wager, and sets their wager to 0.
		try 
		{
			validatePersonIndex(index);
			player = (Player)players[index];
			returnedAmount = player.getWager() / 2;
			player.setTotalMoney(player.getTotalMoney() + returnedAmount);
			player.setWager(0);
		} 
		catch (PlayerException | TableException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		player.setSurrendered(true);
		return returnedAmount;
	}
	
	/**
	 * Flips the dealer's second card.
	 * 
	 * @since 1.0.0
	 */
	public void flipDealersCard()
	{
		players[players.length - 1].flipCardInHand(1, 0);
	}
	
	/**
	 * Adds cards to the dealer's hand until the value of the hand is greater 
	 * than or equal to 17.
	 * 
	 * @return An array of cards added.
	 */
	public Card[] addToDealersHand()
	{
		Person dealer = players[players.length - 1];
		ArrayList<Card> cardsAdded = new ArrayList<Card>();
		
		//While the dealer's score is less than 17
		while(dealer.getHand(0).getHandScore() < 17)
		{
			cardsAdded.add(dealer.addToHand(deck.getCard(), 0));
		}
		
		return cardsAdded.toArray(new Card[cardsAdded.size()]);
	}
	
	/**
	 * Determines the result of the player's actions this turn.
	 * 
	 * @return A String saying the result of the round.
	 * @since 1.0.0
	 */
	public String roundResult(int index) 
	{	
		//Dealer's score is greater than 21.
		Dealer dealer = (Dealer) players[players.length - 1];
		Player player = (Player) players[index];
		Hand[] hands = player.getHands();
			
		//If the player has surrendered.
		if(player.getSurrendered())
		{
			player.setSurrender(player.getSurrender() + 1);
			return player.getName() + " surrendered this hand";
		}
		//If the dealer's score is greater than blackjack.
		else if (dealer.getHand(0).getHandScore() > BLACKJACK)
		{
			//If the player hasn't used split
			if (hands.length < 2) 
			{
				//If the player's score is greater than 21
				if (hands[0].getHandScore() > BLACKJACK) 
				{
					double push = player.getWager();
					pushPayout(player);
					return player.getName() + " pushes and regains $" + push;
				}
				//If the player's score is 21
				else if (hands[0].getHandScore() == BLACKJACK) 
				{
					blackjackPayout(player);
					return player.getName() + " has Blackjack";
				}
				//If the player's score is less than 21
				else 
				{
					standardPayout(player);
					return player.getName() + " wins hand";
				}
			}
			//If player has used split
			else 
			{
				int bestHandIndex = 0;
				for (int j = 0; j < hands.length; j++) 
				{
					if ((hands[j].getHandScore() > hands[bestHandIndex].getHandScore()
						 && hands[j].getHandScore() <= BLACKJACK) || (hands[j].
						 getHandScore() < hands[bestHandIndex].getHandScore() 
						 && hands[bestHandIndex].getHandScore() > BLACKJACK))
					{
						bestHandIndex = j;
					}
				}
				
				//If the player's score is greater than 21
				if (hands[bestHandIndex].getHandScore() > BLACKJACK)
				{
					double push = player.getWager();
					pushPayout(player);
					return player.getName() + " pushes with hand " + (bestHandIndex + 1) 
							+ " and regains $" + push;
				}

				//If the player's score is equal to or less than 21
				else 
				{
					standardPayout(player);
					return player.getName() + " wins with hand " + (bestHandIndex + 1);
				}
			}
		}
		//Dealer's score is 21 (equivalent to blackjack)
		else if (dealer.getHand(0).getHandScore() == BLACKJACK)
		{
			//Player hasn't used split
			if (hands.length < 2)
			{
				//If the player's score is greater than 21
				if (hands[0].getHandScore() > BLACKJACK)
				{
					player.setBust(player.getBust() + 1);
					player.setLoss(player.getLoss() + 1);
					return player.getName() + " busts";
				}
				//If the player's score is 21
				else if (hands[0].getHandScore() == BLACKJACK)
				{
					double push = player.getWager();
					pushPayout(player);
					return player.getName() + " pushes and regains $" + push;
				}
				//If the player's score is less than 21
				else 
				{
					player.setLoss(player.getLoss() + 1);
					return player.getName() + " loses this hand";
				}
			}
			//Player has split
			else 
			{
				int bestHandIndex = 0;
				for (int j = 1; j < hands.length; j++) 
				{
					if ((hands[j].getHandScore() > hands[bestHandIndex].getHandScore()
						 && hands[j].getHandScore() <= BLACKJACK)
						 || (hands[j].getHandScore() < hands[bestHandIndex].getHandScore()
						 && hands[bestHandIndex].getHandScore() > BLACKJACK)) 
					{
						bestHandIndex = j;
					}
				}

				//If the player's score is greater than 21
				if (hands[bestHandIndex].getHandScore() > BLACKJACK) 
				{
					player.setBust(player.getBust() + 1);
					player.setLoss(player.getLoss() + 1);
					return player.getName() + " busts with all hands";
				}
				//If the player's score is 21
				else if (hands[bestHandIndex].getHandScore() == BLACKJACK)
				{
					double push = player.getWager();
					pushPayout(player);
					return player.getName() + " pushes with hand " + (bestHandIndex + 1) 
							+ " and regains $" + push;
				}
				//If the player's score is less than 21
				else 
				{
					player.setLoss(player.getLoss() + 1);
					return player.getName() + " loses with all hands";
				}
			}
		} 
		//Dealer's score less than 21
		else 
		{
			//If player hasn't split
			if (hands.length < 2) 
			{
				//If the player's score is greater than 21
				if (hands[0].getHandScore() > BLACKJACK) 
				{
					player.setBust(player.getBust() + 1);
					player.setLoss(player.getLoss() + 1);
					return player.getName() + " busts";
				}
				//If the player's score is 21
				else if (hands[0].getHandScore() == BLACKJACK) 
				{
					blackjackPayout(player);
					return player.getName() + " has Blackjack";
				}
				//If the player's score is greater than the dealer's score
				//and less than 21
				else if (hands[0].getHandScore() > dealer.getHand(0).getHandScore()
						&& hands[0].getHandScore() < BLACKJACK) 
				{
					standardPayout(player);
					return player.getName() + " wins hand";
				}
				//If the player's score equals the dealer's score
				else if (hands[0].getHandScore() == dealer.getHand(0).getHandScore()) 
				{
					double push = player.getWager();
					pushPayout(player);
					return player.getName() + " pushes and regains $" + push;
				}
				//If the player's score is less than the dealer's score
				else 
				{
					player.setLoss(player.getLoss() + 1);
					return player.getName() + " loses this hand";
				}
			}
			//If player has split
			else 
			{
				int bestHandIndex = 0;
				for (int j = 1; j < hands.length; j++) 
				{
					if ((hands[j].getHandScore() > hands[bestHandIndex].getHandScore()
						 && hands[j].getHandScore() <= BLACKJACK) || (hands[j].
						 getHandScore() < hands[bestHandIndex].getHandScore() && hands
						 [bestHandIndex].getHandScore() > BLACKJACK)) 
					{
						bestHandIndex = j;
					}
				}

				//If the player's score is greater than 21
				if (hands[bestHandIndex].getHandScore() > BLACKJACK) 
				{
					player.setBust(player.getBust() + 1);
					player.setLoss(player.getLoss() + 1);
					return player.getName() + " busts with all hands";
				}
				//If the player's score is 21
				else if (hands[bestHandIndex].getHandScore() == BLACKJACK)
				{
					blackjackPayout(player);
					return player.getName() + " has Blackjack with hand " 
							   + (bestHandIndex + 1);
					
				}
				//If the player's score is greater than the dealer's score
				//and less than 21
				else if (hands[bestHandIndex].getHandScore() > dealer.getHand(0).getHandScore()
						&& hands[bestHandIndex].getHandScore() < BLACKJACK) 
				{
					standardPayout(player);
					return player.getName() + " wins with hand " + (bestHandIndex + 1);
				}
				//If the player's score equals the dealer's score
				else if (hands[bestHandIndex].getHandScore() == dealer.getHand(0).getHandScore()) 
				{
					double push = player.getWager();
					pushPayout(player);
					return player.getName() + " pushes with hand " + (bestHandIndex + 1) 
							+ " and regains $" + push;
				}
				//If the player's score is less than the dealer's score
				else
				{
					player.setLoss(player.getLoss() + 1);
					return player.getName() + " loses with all hands";
				}
			}
		}
	}
	
	/**
	 * Pays out blackjack winnings, winnings are paid out at a 3:2.
	 * 
	 * @param payee The current player.
	 * @since 1.0.0
	 */
	private void blackjackPayout(Player payee)
	{
		final double BLACKJACKRETURN = 2.5;
		double blackjackWinnings = 0;
		try 
		{
			blackjackWinnings = payee.getWager() * BLACKJACKRETURN;
			payee.setTotalMoney(payee.getTotalMoney() + blackjackWinnings);
			payee.setTotalWinnnings(payee.getTotalWinnings() + blackjackWinnings);
		} 
		catch(PlayerException e) 
		{
			e.printStackTrace();
			System.exit(0);
		} 
		payee.setCurrentBlackjack(blackjackWinnings);
		payee.setHasBlackjack(true);
		payee.setBlackjack(payee.getBlackjack() + 1);
		payee.setWin(payee.getWin() + 1);
	}
	
	/**
	 * Pays out standard winnings, standard winnings are paid out at 2:1.
	 * 
	 * @param payee The current player.
	 * @since 1.0.0
	 */
	private void standardPayout(Player payee)
	{
		final double STANDARDRETURN = 2;
		double standardWinnings = 0;
		try
		{
			standardWinnings = payee.getWager() * STANDARDRETURN;
			payee.setTotalMoney(payee.getTotalMoney() + standardWinnings);
			payee.setTotalWinnnings(payee.getTotalWinnings() + standardWinnings);
		}
		catch(PlayerException ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		payee.setCurrentWin(standardWinnings);
		payee.setHasWin(true);
		payee.setWin(payee.getWin() + 1);
	}
	
	/**
	 * Pays out push winnings, push winnings are paid out at 1:1.
	 * 
	 * @param payee The current player.
	 * @since 1.4.0
	 */
	private void pushPayout(Player payee)
	{
		try
		{
			payee.setTotalMoney(payee.getTotalMoney() + payee.getWager());
		}
		catch(PlayerException ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		
		payee.setPush(payee.getPush() + 1);
	}
	
	/**
	 * Resets all of the player's hands, wagers, and if they've surrendered set 
	 * back to default state and checks if they have enough money to continue.
	 * 
	 * @since 1.0.0
	 */
	public void prepareForNewRound(int index)
	{
		players[index].startingHand();
		if(players[index] instanceof Player)
		{
			Player player = (Player)players[index];
			try 
			{
				player.setWager(0);
			} 
			catch (PlayerException e) 
			{
				e.printStackTrace();
			}	
			
			if(player.getSurrendered())
			{
				player.setSurrendered(false);
			}
			
			if(player.getHasWin())
			{
				player.setHasWin(false);
				player.setCurrentWin(0);
			}
			
			if(player.getHasBlackjack())
			{
				player.setHasBlackjack(false);
				player.setCurrentBlackjack(0);
			}
			
			if(player.getTotalMoney() < MINWAGER)
			{
				player.setBankrupt(true);
			}
		}
	}
}
