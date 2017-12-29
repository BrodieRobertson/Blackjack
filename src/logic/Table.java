package logic;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.omg.CORBA.Current;

import card.*;
import player.*;

/**
 * The representation of the table, this class defines the table with
 * attributes representing the, players in the game, the deck, the player's
 * input, the current and total rounds, the minimum and maximum number of
 * player's the minimum and maximum wagers, and the value of blackjack. All
 * gameplay logic takes place here.
 * 
 * @author Brodie Robertson
 *
 */
public class Table 
{
	/**
	 * The player's in the game.
	 */
	private Person[] players;
	/**
	 * The deck.
	 */
	private Deck deck;
	/**
	 * The input device.
	 */
	private Scanner keyboard;
	/**
	 * The current round.
	 */
	private int currentRound = 1;
	/**
	 * The absolute total number of rounds.
	 */
	private int totalRounds;
	/**
	 * The total number of rounds actually played
	 */
	private int completedRounds = 1;
	private static final int INSURANCE_RETURN = 2;
	/**
	 * The minimum number of players.
	 */
	public static final int MINNUMPLAYERS = 1;
	/**
	 * The maximum number of player's
	 */
	public static final int MAXNUMPLAYERS = 6;
	/**
	 * The minimum allowable wager.
	 */
	public static final double MINWAGER = 25;
	/**
	 * The maximum allowable wager.
	 */
	public static final double MAXWAGER = 1000;
	/**
	 * The value of blackjack.
	 */
	public static final int BLACKJACK = 21;
	
	/*
	public static void main(String[] args)
	{
		Table game = new Table();
		boolean availablePlayers = true;
		//While there are still rounds remaining, continue the game.
		while(game.currentRound < game.totalRounds && availablePlayers)
		{
			System.out.println("Round: " + game.currentRound + 1);
			game.displayPlayers();
			game.initialBet();
			
			game.displayPlayers();
			game.deal();
			
			game.displayPlayers();
			game.insurance();
			
			game.displayPlayers();
			
			//If the dealer didn't reach 21 after the insurance round.
			if(game.players[game.players.length - 1].getHand(0).getHandScore() < 21)
			{
				game.round();
				
				game.displayPlayers();				
				game.payout();
			}
			game.prepareForNewRound();
			game.deck = new Deck(game.deck.getNumOfDecks());
			game.currentRound++;
			game.completeRound++;
			
			availablePlayers = false;
		}
		game.displayGameStats();
	}
	*/
	
	/**
	 * Constructor for the table, takes the number of human players, the number
	 * of AI players and the number of decks as arguments.
	 * 
	 * @param numOfHumanPlayers The number of human players.
	 * @param numOfAiPlayers The number of AI players.
	 * @param numOfDecks The number of decks.
	 */
	public Table()
	{
		deck = new Deck(1);
	}
	
	/**
	 * Creates the list of player's with the parameters specified, the program 
	 * ends if the number of human players, AI players or both added together
	 * are invalid.
	 * 
	 * @param numOfHumanPlayers The number of human players.
	 * @param numOfAiPlayers The number of AI players.
	 */
	public void createPlayers(int numOfHumanPlayers, int numOfAiPlayers) throws 
	PlayerException, HumanException, CpuException
	{
		if(numOfHumanPlayers + numOfAiPlayers > MAXNUMPLAYERS || numOfHumanPlayers + 
				numOfAiPlayers < MINNUMPLAYERS)
		{
			throw new PlayerException("Invalid number of players: " + 
					(numOfHumanPlayers + numOfAiPlayers));
		}
		if(numOfHumanPlayers < MINNUMPLAYERS || numOfHumanPlayers > 
			MAXNUMPLAYERS)
		{
			throw new HumanException("Invalid number of human players: " + 
				numOfHumanPlayers);
		}
		if(numOfAiPlayers < 0 || numOfAiPlayers > MAXNUMPLAYERS)
		{
			throw new CpuException("Invalid number of AI player: " + 
					numOfAiPlayers);
		}
		
		int playerIndex = 0;
		//Creates the human players.
		players = new Person[numOfHumanPlayers + numOfAiPlayers + 1];
		for(int i = 0; i < numOfHumanPlayers; i++)
		{
			players[playerIndex] = new Human();
			playerIndex++;
		}
		
		//Creates the AI players.
		for(int i = 0; i < numOfAiPlayers; i++)
		{
			players[playerIndex] = new CPU("CPU " + i);
			playerIndex++;
		}
		
		players[playerIndex] = new Dealer("Dealer");
	}
	
	/**
	 * Displays the complete statistics for the game and every player.
	 */
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
	
	/**
	 * @return
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
	 * @return
	 */
	public int getCurrentRound()
	{
		return currentRound;
	}
	
	/**
	 * @return
	 */
	public int getTotalRounds()
	{
		return totalRounds;
	}
	
	/**
	 * Sets the total number of rounds, prompts the user for an input until
	 * a valid value has been received.
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
	 */
	public Deck getDeck()
	{
		return new Deck(deck);
	}
	
	/**
	 * Gets the list of players.
	 * 
	 * @return The list of players.
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
	 * @param index
	 * @return
	 * @throws PersonException
	 */
	public Person getPersonAtIndex(int index)
	{
		try
		{
			validatePlayerIndex(index);
		}
		catch(TableException ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		
		return players[index].clone();
	}
	
	/**
	 * @param index
	 * @param name
	 * @throws PersonException
	 */
	public void setPersonNameAtIndex(int index, String name) throws PersonException
	{
		try
		{
			validatePlayerIndex(index);
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
					card.setFaceUp();
				}
						
				players[j].addToHand(card, 0);
			}
		}
	}
	
	/**
	 * @param index
	 * @param wager
	 * @throws PlayerException
	 * @throws TableException
	 */
	public void setHumanWager(int index, double wager) throws PlayerException, TableException
	{
		try
		{
			validatePlayerIndex(index);
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
	 * @param index
	 * @return
	 */
	public double setCpuWager(int index)
	{
		try
		{
			validatePlayerIndex(index);
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
			if(wager >= MINWAGER && wager <= MAXWAGER && wager <= player.getTotalMoney())
			{
				validWager = true;
			}
			else
			{
				wager--;
			}
		}
		
		try 
		{
			player.setWager(wager);
		} 
		catch (PlayerException ex) 
		{
			ex.printStackTrace();
			System.exit(0);
		}
		
		return wager;
	}
	
	/**
	 * @param index
	 * @param insurance
	 * @throws TableException
	 * @throws PlayerException
	 */
	public void setHumanInsurance(int index, double insurance) throws 
	TableException, PlayerException
	{
		Player player = null;
		try
		{
			validatePlayerIndex(index);
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
		if(player.getHand(0).getHandScore() < 21)
		{
			if(insurance <= player.getWager() / 2)
			{
				player.setInsurance(insurance);
			}
			else if(insurance > player.getTotalMoney())
			{
				throw new TableException("Invalid insurance: " + insurance);
			}
			else
			{
				throw new TableException("Invalid insurance: " + insurance);
			}
		}
	}
	
	/**
	 * @param index
	 * @return
	 */
	public double setCpuInsurance(int index)
	{
		
		Player player = null;
		try 
		{
			validatePlayerIndex(index);
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
		while(!player.getTookInsurance() || insurance <= 0)
		{
			if(insurance > player.getTotalMoney())
			{
				insurance--;
			}
		}
		
		if(insurance > 0)
		{
			player.setTookInsurance(true);
		}
		
		return insurance;
	}
	
	/**
	 * @param index
	 * @throws TableException
	 */
	private void validatePlayerIndex(int index) throws TableException
	{
		if(index < 0 || index > players.length)
		{
			throw new TableException("Index outside of valid player range");
		}
	}
	
	public boolean attemptDealerCardFlip()
	{
		Hand dealerHand = players[players.length - 1].getHand(0);
		//If the dealer's 2nd card's value is 10, flip it.
		if(dealerHand.getCard(1).getValue() == 10)
		{
			dealerHand.flipCardInHand(1);
			return true;
		}
		return false;
	}
	
	/**
	 * Pays outs insurance bets if the dealer has blackjack in his first 2 cards
	 * and sets any insurance bets back to 0 regardless of the result.
	 */
	public int insurancePayout(int index)
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
			} 
			catch (PlayerException e) 
			{
				e.printStackTrace();
				System.exit(0);
			}
			return 2;
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
				prepareForNewRound();
				return 1;
			}
			//If the player does not have Blackjack
			else
			{
				prepareForNewRound();
				return 0;
			}
		}
	}
	
	/**
	 * @param index
	 */
	public void resetInsurance(int index)
	{
		try
		{
			validatePlayerIndex(index);
			Player player = (Player)players[index];
			player.setInsurance(0);
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
	 */
	public Card hit(int newIndex, int handIndex)
	{
		try 
		{
			validatePlayerIndex(newIndex);
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
	 */
	public Card doubleDown(int index, int handIndex)
	{
		Player player = null;
		try
		{
			validatePlayerIndex(index);
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
	 */
	public void split(int index)
	{
		Player player = null;
		try 
		{
			validatePlayerIndex(index);
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
	 */
	public double surrender(int index)
	{
		double returnedAmount = 0;
		Player player = null;
		//Returns half the player's wager, and sets their wager to 0.
		try 
		{
			validatePlayerIndex(index);
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
	 * 
	 */
	public void flipDealersCard()
	{
		players[players.length - 1].flipCardInHand(1, 0);
	}
	
	/**
	 * @return
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
	 * Determines whether each player 
	 */
	private void payout() 
	{
		for (int i = 0; i < players.length - 1; i++) 
		{	
			//Dealer's score is greater than 21.
			Dealer dealer = (Dealer) players[players.length - 1];
			Player player = (Player) players[i];
			Hand[] hands = player.getHands();
			
			//If the player has surrendered.
			if(player.getSurrendered())
			{
				System.out.println(player.getName() + " surrendered this hand");
				player.setSurrender(player.getSurrender() + 1);
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
						System.out.println(player.getName() + " pushes");
						player.setPush(player.getPush() + 1);
					}
					//If the player's score is 21
					else if (hands[0].getHandScore() == BLACKJACK) 
					{
						System.out.println(player.getName() + " has Blackjack");
						blackjackPayout(player);
					}
					//If the player's score is less than 21
					else 
					{
						System.out.println(player.getName() + " wins hand");
						standardPayout(player);
					}
				}
				//If player has used split
				else 
				{
					int bestHandIndex = 0;
					for (int j = 1; i < hands.length; j++) 
					{
						if ((hands[j].getHandScore() > hands[bestHandIndex].getHandScore()
							 && hands[j].getHandScore() <= BLACKJACK) || (hands[j].
							 getHandScore()< hands[bestHandIndex].getHandScore() 
							 && hands[bestHandIndex].getHandScore() > BLACKJACK))
						{
							bestHandIndex = j;
						}
					}

					//If the player's score is greater than 21
					if (hands[bestHandIndex].getHandScore() > BLACKJACK)
					{
						System.out.println(player.getName() + " pushes with hand " + (bestHandIndex + 1));
						player.setPush(player.getPush() + 1);
					}

					//If the player's score is equal to or less than 21
					else 
					{
						System.out.println(player.getName() + " wins with hand " + (bestHandIndex + 1));
						standardPayout(player);
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
						System.out.println(player.getName() + " busts");
						player.setBust(player.getBust() + 1);
						player.setLoss(player.getLoss() + 1);
					}
					//If the player's score is 21
					else if (hands[0].getHandScore() == BLACKJACK)
					{
						System.out.println(player.getName() + " pushes");
						player.setPush(player.getPush() + 1);
					}
					//If the player's score is less than 21
					else 
					{
						System.out.println(player.getName() + " loses this hand");
						player.setLoss(player.getLoss() + 1);
					}
				}
				//Player has split
				else 
				{
					int bestHandIndex = 0;
					for (int j = 1; i < hands.length; j++) 
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
						System.out.println(player.getName() + " busts with all hands");
						player.setBust(player.getBust() + 1);
						player.setLoss(player.getLoss() + 1);
					}
					//If the player's score is 21
					else if (hands[bestHandIndex].getHandScore() == BLACKJACK)
					{
						System.out.println(player.getName() + " pushes with hand " + (bestHandIndex + 1));
						player.setPush(player.getPush() + 1);
					}
					//If the player's score is less than 21
					else 
					{
						System.out.println(player.getName() + " loses with all hands");
						player.setLoss(player.getLoss() + 1);
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
						System.out.println(player.getName() + " busts");
						player.setBust(player.getBust() + 1);
						player.setLoss(player.getLoss() + 1);
					}
					//If the player's score is 21
					else if (hands[0].getHandScore() == BLACKJACK) 
					{
						System.out.println(player.getName() + " has Blackjack");
						blackjackPayout(player);
					}
					//If the player's score is greater than the dealer's score
					//and less than 21
					else if (hands[0].getHandScore() > dealer.getHand(0).getHandScore()
							&& hands[0].getHandScore() < BLACKJACK) 
					{
						System.out.println(player.getName() + " wins hand");
						standardPayout(player);
					}
					//If the player's score equals the dealer's score
					else if (hands[0].getHandScore() == dealer.getHand(0).getHandScore()) 
					{
						System.out.println(player.getName() + " pushes");
						player.setPush(player.getPush() + 1);
					}
					//If the player's score is less than the dealer's score
					else 
					{
						System.out.println(player.getName() + " loses this hand");
						player.setLoss(player.getLoss() + 1);
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
						System.out.println(player.getName() + " busts with all hands");
						player.setBust(player.getBust() + 1);
						player.setLoss(player.getLoss() + 1);
					}
					//If the player's score is 21
					else if (hands[bestHandIndex].getHandScore() == BLACKJACK)
					{
						System.out.println(player.getName() + " has Blackjack with hand " 
										   + (bestHandIndex + 1));
						blackjackPayout(player);
					}
					//If the player's score is greater than the dealer's score
					//and less than 21
					else if (hands[bestHandIndex].getHandScore() > dealer.getHand(0).getHandScore()
							&& hands[bestHandIndex].getHandScore() < BLACKJACK) 
					{
						System.out.println(player.getName() + " wins with hand " + (bestHandIndex + 1));
						standardPayout(player);
					}
					//If the player's score equals the dealer's score
					else if (hands[bestHandIndex].getHandScore() == dealer.getHand(0).getHandScore()) 
					{
						System.out.println(player.getName() + " pushes with hand " + (bestHandIndex + 1));
						player.setPush(player.getPush() + 1);
					}
					//If the player's score is less than the dealer's score
					else
					{
						System.out.println(player.getName() + " loses with all hands");
						player.setLoss(player.getLoss() + 1);
					}
				}
			}
		}
		System.out.println();
	}
	
	
	/**
	 * Pays out blackjack winnings, winnings are paid out at a 3:2.
	 * 
	 * @param payee The current player.
	 */
	private void blackjackPayout(Player payee)
	{
		final double BLACKJACKRETURN = 2.5;
		try 
		{
			double blackjackWinnings = payee.getWager() * BLACKJACKRETURN;
			payee.setTotalMoney(payee.getTotalMoney() + blackjackWinnings);
			payee.setTotalWinnnings(payee.getTotalWinnings() + blackjackWinnings);
		} 
		catch(PlayerException e) 
		{
			e.printStackTrace();
			System.exit(0);
		} 
		payee.setBlackjack(payee.getBlackjack() + 1);
		payee.setWin(payee.getWin() + 1);
	}
	
	/**
	 * Pays out standard winnings, standard winnings are paid out at 2:1.
	 * 
	 * @param payee The current player.
	 */
	private void standardPayout(Player payee)
	{
		final double STANDARDRETURN = 2;
		try
		{
			double standardWinnings = payee.getWager() * STANDARDRETURN;
			payee.setTotalMoney(payee.getTotalMoney() + standardWinnings);
			payee.setTotalWinnnings(payee.getTotalWinnings() + standardWinnings);
		}
		catch(PlayerException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		payee.setWin(payee.getWin() + 1);
	}
	
	/**
	 * Resets all of the player's hands, wagers, and if they've surrendered set 
	 * back to default state and checks if they have enough money to continue.
	 */
	private void prepareForNewRound()
	{
		for(int i = 0; i < players.length; i++)
		{
			players[i].startingHand();
			if(players[i] instanceof Player)
			{
				Player player = (Player)players[i];
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
				
				if(player.getTotalMoney() < MINWAGER)
				{
					player.setBankrupt(true);
					System.out.println(player.getName() + " has gone bankrupt");
				}
			}
		}
		System.out.println();
	}
}
