package game;
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
	private int currentRound;
	/**
	 * The absolute total number of rounds.
	 */
	private int totalRounds;
	/**
	 * The total number of rounds actually played
	 */
	private int completedRounds;
	/**
	 * The minimum number of players.
	 */
	private static final int MINNUMPLAYERS = 1;
	/**
	 * The maximum number of player's
	 */
	private static final int MAXNUMPLAYERS = 6;
	/**
	 * The minimum allowable wager.
	 */
	private static final double MINWAGER = 25;
	/**
	 * The maximum allowable wager.
	 */
	private static final double MAXWAGER = 1000;
	/**
	 * The value of blackjack.
	 */
	public static final int BLACKJACK = 21;
	
	public static void main(String[] args)
	{
		Table game = new Table(2, 1, 1);
		boolean availablePlayers = true;
		//While there are still rounds remaining, continue the game.
		while(game.currentRound < game.totalRounds && availablePlayers)
		{
			System.out.println("Round: " + (game.currentRound + 1));
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
			
			availablePlayers = false;
			for(int i = 0; i < game.players.length - 1; i++)
			{
				Player player = (Player)game.players[i];
				if(!player.getBankrupt())
				{
					availablePlayers = true;
				}
			}
		}
		game.displayGameStats();
	}
	
	/**
	 * Constructor for the table, takes the number of human players, the number
	 * of AI players and the number of decks as arguments.
	 * 
	 * @param numOfHumanPlayers The number of human players.
	 * @param numOfAiPlayers The number of AI players.
	 * @param numOfDecks The number of decks.
	 */
	public Table(int numOfHumanPlayers, int numOfAiPlayers, int numOfDecks)
	{
		keyboard = new Scanner(System.in);
		createPlayers(numOfHumanPlayers, numOfAiPlayers);
		setTotalRounds();
		deck = new Deck(numOfDecks);
	}
	
	/**
	 * Creates the list of player's with the parameters specified, the program 
	 * ends if the number of human players, AI players or both added together
	 * are invalid.
	 * 
	 * @param numOfHumanPlayers The number of human players.
	 * @param numOfAiPlayers The number of AI players.
	 */
	private void createPlayers(int numOfHumanPlayers, int numOfAiPlayers)
	{
		try
		{
			if(numOfHumanPlayers + numOfAiPlayers > MAXNUMPLAYERS)
			{
				throw new PlayerException("Invalid number of players: " + 
						(numOfHumanPlayers + numOfAiPlayers));
			}
			if(numOfHumanPlayers < MINNUMPLAYERS || numOfHumanPlayers > 
				MAXNUMPLAYERS)
			{
				throw new PlayerException("Invalid number of human players: " + 
						numOfHumanPlayers);
			}
			if(numOfAiPlayers < 0 || numOfAiPlayers > MAXNUMPLAYERS)
			{
				throw new PlayerException("Invalid number of AI player: " + 
						numOfAiPlayers);
			}
		}
		catch(PlayerException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		
		int playerIndex = 0;
		//Creates the human players.
		players = new Person[numOfHumanPlayers + numOfAiPlayers + 1];
		for(int i = 0; i < numOfHumanPlayers; i++)
		{
			players[playerIndex] = new Human("Human " + i);
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
	 * Displays the hands and general statistics of each player.
	 */
	private void displayPlayers()
	{
		for(int i = 0; i < players.length; i++)
		{
			//If the player is of type player.
			if(players[i] instanceof Player)
			{
				Player player = (Player)players[i];
				if(!player.getBankrupt())
				{
					System.out.println(players[i]);	
				}
			}
			//If the player is a dealer.
			else
			{
				System.out.println(players[i]);	
			}
		}
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
	 * Sets the total number of rounds, prompts the user for an input until
	 * a valid value has been received.
	 */
	private void setTotalRounds()
	{
		int totalRounds = 0;
		boolean validInput = false;
		System.out.println("How many rounds would you like to play?");
		while(!validInput)
		{
			try
			{
				totalRounds = keyboard.nextInt();
				if(totalRounds > 0)
				{
					validInput = true;
				}
				else
				{
					System.out.println("Total rounds must be greater than 0: " + totalRounds);
				}
			}
			catch(InputMismatchException e)
			{
				System.out.println("Invalid Input");
				keyboard.next();
			}
		}
		
		System.out.println();
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
	 * Prompts each player for there initial bets, continues to prompt until
	 * a valid input is given.
	 */
	public void initialBet()
	{
		for(int i = 0; i < players.length - 1; i++)
		{
			Player player = (Player)players[i];
			if(!player.getBankrupt())
			{
				//If the player is human, prompt the user for an input.
				if(player instanceof Human)
				{
					System.out.println(player.getName() + " enter wager between " 
							+ MINWAGER + " and " + MAXWAGER);
					boolean validInput = false;
					double wager;
					//Prompts the user for an input until they give a valid input.
					while(!validInput)
					{	
						try 
						{
							wager = keyboard.nextDouble();
							if(wager >= MINWAGER & wager <= MAXWAGER)
							{
								player.setWager(wager);
								validInput = true;
							}
							else
							{
								System.out.println("Invalid wager: " + wager);
							}
						} 
						catch (PlayerException e) 
						{
							System.out.println(e.getMessage());
						}
						catch(InputMismatchException e)
						{
							System.out.println("Invalid input");
							keyboard.next();
						}
					}
					keyboard.nextLine();
				}
				//If the player is a CPU, set the wager to 50
				else
				{
					int wager = 50;
					boolean validWager = false;
					while(!validWager)
					{
						if(wager >= MINWAGER && wager <= player.getTotalMoney())
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
					catch (PlayerException e) 
					{

					}
				}
			}
		}
	}
	
	/**
	 * Prompts the user for an input for whether they wish to take insurance
	 * and how much they wish to take if the dealer's first card is an ace.
	 */
	public void insurance()
	{
		if(players[players.length - 1].getHand(0).getHandScore() == 11)
		{
			for(int i = 0; i < players.length - 1; i++)
			{
				Player player = (Player)players[i];
				//If the player is human and didn't achieve blackjack with there
				//first 2 cards.
				if(player instanceof Human && player.getHand(0).getHandScore() < BLACKJACK)
				{
					System.out.println(player.getName() + " would you like insurance?"
							+ " Y|N");
					String input = "";
					boolean validInput = false;
					//Prompts the user for an input until a valid input is given.
					while(!validInput)
					{
						input = keyboard.next();
						if(input.equalsIgnoreCase("y"))
						{
							if(player.getTotalMoney() > 0)
							{
								validInput = true;
							}
							else
							{
								System.out.println("You need to have money "
										+ "remaining to buy insurance");
							}
						}
						else if(input.equalsIgnoreCase("n"))
						{
							validInput = true;
						}
						else
						{
							System.out.println("Invalid Input");
						}
					}
					keyboard.nextLine();
					
					//If the player takes insurance.
					if(input.equalsIgnoreCase("y"))
					{
						System.out.println(player.getName() + " how much insurance "
								+ "would you like?");
						double insurance;
						validInput = false;
						//Prompts the user for an input until a valid input is given
						//insurance must be no greater than half the wager.
						while(!validInput)
						{
							try 
							{	
								insurance = keyboard.nextDouble();
								if(insurance <= player.getWager() / 2)
								{
									player.setInsurance(insurance);
									validInput = true;
								}
								else
								{
									System.out.println("Insurance can't be greater "
											+ "than half of the wager");
								}
							} 
							catch(PlayerException e)
							{
								System.out.println(e.getMessage());
							}
							catch(InputMismatchException e)
							{
								System.out.println("Invalid input");
								keyboard.nextLine();
							}
						}
						keyboard.nextLine();
					}
				}
				//If the player is a CPU and didn't achieve blackjack with there
				//first 2 cards.
				else if(player instanceof CPU && player.getHand(0).getHandScore() < 
						BLACKJACK)
				{
					try 
					{
						player.setInsurance(player.getWager()/2);
					} 
					catch (PlayerException e) 
					{
						e.printStackTrace();
					}
				}
			}
			
			Hand dealerHand = players[players.length - 1].getHand(0);
			//If the dealer's 2nd card's value is 10, flip it.
			if(dealerHand.getCard(1).getValue() == 10)
			{
				dealerHand.flipCardInHand(1);
			}
			
			insurancePayout();
		}
	}
	
	/**
	 * Pays outs insurance bets if the dealer has blackjack in his first 2 cards
	 * and sets any insurance bets back to 0 regardless of the result.
	 */
	private void insurancePayout()
	{	
		//If dealer has Blackjack
		if(players[players.length - 1].getHand(0).getHandScore() == BLACKJACK)
		{
			for(int i = 0; i < players.length - 1; i++)
			{
				Player temp = (Player)players[i];

				//If player has insurance bet
				if(temp.getInsurance() > 0)
				{
					try 
					{
						temp.setTotalMoney(temp.getTotalMoney() + temp.getInsurance());
						temp.setInsurance(0);
					} 
					catch (PlayerException e) 
					{
						e.printStackTrace();
						System.exit(0);
					}
				}
				//If player does not have insurance
				else
				{
					//If the player has Blackjack
					if(temp.getHand(0).getHandScore() == BLACKJACK)
					{
						try 
						{
							temp.setTotalMoney(temp.getTotalMoney() + temp.getWager());
						} 
						catch (PlayerException e) 
						{
							e.printStackTrace();
							System.exit(0);
						}
					}
					//If the player does not have Blackjack
					else
					{
						System.out.println(temp.getName() + " has lost this round");
					}
				}
				
				try 
				{
					temp.setWager(0);
				} 
				catch (PlayerException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
		//If dealer does not have Blackjack
		else
		{
			for(int i = 0; i < players.length - 1; i++)
			{
				Player temp = (Player)players[i];
				
				//If player has insurance bet
				if(temp.getInsurance() > 0)
				{
					try 
					{
						temp.setInsurance(0);
					} 
					catch (PlayerException e) 
					{
						e.printStackTrace();
						System.exit(0);
					}
				}
			}
		}
	}
	
	/**
	 * Cycles through each player as well as the dealer, if they're human they're
	 * prompted for there decision for the action that they wish to take during
	 * this round.
	 */
	public void round()
	{
		for(int i = 0; i < players.length - 1; i++)
		{
			Player player = (Player)players[i];
			//If the player did not reach Blackjack with in first 2 cards.
			if(!player.getBankrupt() && player.getHand(0).getHandScore() < BLACKJACK)
			{
				//If the player is human.
				if(player instanceof Human)
				{
					System.out.println(player.getName() + " what move do you "
							+ "wish to take? S|H|DD|SP|SU");
					boolean validInput = false;
					String input = "";
					//Prompts the user an input until they give a valid input.
					while(!validInput)
					{
						input = keyboard.next();
						//If the input is S, H or SU.
						if(input.equalsIgnoreCase("S") || input.
								equalsIgnoreCase("H") ||input.equalsIgnoreCase("SU"))
						{
							validInput = true;
						}
						//If the input is DD.
						else if(input.equalsIgnoreCase("DD"))
						{
							//Valid input if the player has money than the current
							//wager left.
							if(player.getTotalMoney() >= player.getWager())
							{
								validInput = true;
							}
							else
							{
								System.out.println(player.getName() + " you do "
										+ "not have enough money");
							}
						}
						//If the input is SP.
						else if(input.equalsIgnoreCase("SP"))
						{
							//If the player can afford to double wager and if the 
							//players 2 cards have matching values.
							if((player.getTotalMoney() >= player.getWager() && player.
							   getHand(0).getCard(0).getValue() == player.getHand(0).
							   getCard(1).getValue()) || (player.getHand(0).getCard(0).
							   getFace() == Face.ACE && player.getHand(0).getCard(1).
							   getFace() == Face.ACE))
							{
								validInput = true;
							}
							//Invalid input if the player has less money than the
							//current wager left.
							else if (player.getTotalMoney() < player.getWager())
							{
								System.out.println(player.getName() + " you do "
										+ "not have enough money");
							}
							//Invalid input if the value of both
							else if(player.getHand(0).getCard(0).getValue() !=
									player.getHand(0).getCard(1).getValue())
							{
								System.out.println("Cards in the hand do not match");
							}
						}
						//If the input is anything else.
						else
						{
							System.out.println("Invalid input");
						}
					}
					System.out.println();
					keyboard.nextLine();
					
					//If the input is S.
					if(input.equalsIgnoreCase("S"))
					{
						stand(player);
					}
					//If the input is H.
					else if(input.equalsIgnoreCase("H"))
					{
						hit(player, 0);
					}
					//If the input is DD.
					else if(input.equalsIgnoreCase("DD"))
					{
						doubleDown(player, 0);
					}
					//If the input is SP.
					else if(input.equalsIgnoreCase("SP"))
					{
						split(player);
					}
					//If the input SU.
					else
					{
						surrender(player);
					}	
				}
				//If the player is a CPU.
				else
				{
					stand(player);
				}
			}
		}
		dealersTurn();
	}
	
	/**
	 * Displays a message stating the player stand and their current score.
	 * 
	 * @param player The current player.
	 */
	private void stand(Player player)
	{
		System.out.println(player.getName() + " stands with a score of "
				+ player.getHand(0).getHandScore());
		System.out.println();
	}
	
	/**
	 * Displays a message stating that the player stands with a specific hand.
	 * 
	 * @param player The current player.
	 * @param index The specified hand.
	 */
	private void stand(Player player, int index)
	{
		System.out.println(player.getName() + " stands hand " + (index + 1) 
				+ " with a score of " + player.getHand(index).getHandScore());
		System.out.println();
	}
	
	/**
	 * Deals the player a new card and if the the player hasn't bust or reached 
	 * blackjack, they are prompted for a new choice of hit again or stand.
	 * 
	 * @param player The current player.
	 * @param index The index of the hand.
	 */
	private void hit(Player player, int index)
	{
		System.out.println(player.getName() + " hits\n");
		player.addToHand(deck.getCard(), index);
		System.out.println(player.getHand(index));
		
		Hand playerHand = player.getHand(index);
		//If hand's score less than Blackjack
		if(playerHand.getHandScore() < BLACKJACK)
		{
			/*
			 * If hand hasn't been split or if the hand has been split and first
			 * card is valued at 10 or 11 and less than 2 cards in hand or if
			 * hand has been split and first card is not valued at 10 or 11.
			 */
			if(!playerHand.getSplit() || (playerHand.getSplit() && ((playerHand.
			   getCard(0).getValue() == 11 || playerHand.getCard(0).getValue()
			   == 10) && playerHand.getCardsRemaining() < 2) || (playerHand.getCard(0)
			   .getValue() == 11 && playerHand.getCard(0).getValue() == 10)))
			{
				boolean validInput = false;
				String input = "";
				System.out.println("What do you want to do now? H|S");
				//Prompt the user for an input until they give a valid input.
				while(!validInput)
				{
					input = keyboard.next();
					if(input.equalsIgnoreCase("H") || input.equalsIgnoreCase("S"))
					{
						validInput = true;
					}
					else
					{
						System.out.println("Invalid input");
					}
				}
				
				//If the choice is H
				if(input.equalsIgnoreCase("H"))
				{
					hit(player,  index);
				}
				//If the choice is S
				else
				{
					stand(player, index);
				}
			}
		}
		//If hand's score greater than blackjack
		else if(playerHand.getHandScore() > BLACKJACK)
		{
			System.out.println(player.getName() + " busts\n");
			player.setBust(true);
		}
		//If the hand's score equals blackjack.
		else
		{
			if(playerHand.getSplit())
			{
				stand(player, index);
			}
			else
			{
				stand(player);
			}
		}
	}
	
	/**
	 * Doubles the player's wager and deals them a new to the specified hand, if 
	 * the player passes blackjack they are bust.
	 * 
	 * @param player The current player.
	 * @param index The index of the hand.
	 */
	private void doubleDown(Player player, int index)
	{
		System.out.println(player.getName() + " doubles down");
		try
		{
			player.setWager(player.getWager() * 2);
			System.out.println(player.getName() + "'s wager is now " + player.getWager());
		} 
		catch (PlayerException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
		player.addToHand(deck.getCard(), index);
		System.out.println(player.getName() + "'s score is now " + player.getHand(index).getHandScore());
		
		//If player surpasses blackjack
		if(player.getHand(index).getHandScore() > BLACKJACK)
		{
			System.out.println(player.getName() + " busts");
			player.setBust(true);
		}
		System.out.println();
	}
	
	/**
	 * Splits the player's hand into 2 hands and doubles their wager.
	 * 
	 * @param player The current player.
	 */
	private void split(Player player)
	{
		System.out.println(player.getName() + " splits hand");
		try 
		{
			player.setWager(player.getWager() * 2);
			System.out.println(player.getName() + "'s wager is now " + player.getWager());
		} 
		catch (PlayerException e) 
		{
			e.printStackTrace();
			System.exit(0);
		}
		//Adds a new hand and moves the player's second card into it.
		player.addHand();
		player.addToHand(player.getHand(0).getCard(1), 1);
		player.removeFromHand(0, 1);
		System.out.println();
		System.out.println(player);
		
		for(int i = 0; i < player.getHands().length; i++)
		{
			System.out.println("What do you want to do now with hand " + (i + 1) + " H|S|DD");
			boolean validInput = false;
			String input = "";
			//Prompts the user for an input until they give a valid input.
			while(!validInput)
			{
				input = keyboard.next();
				//If the input is S or H.
				if(input.equalsIgnoreCase("S") || input.equalsIgnoreCase("H"))
				{
					validInput = true;
				}
				//If the input is DD.
				else if(input.equalsIgnoreCase("DD"))
				{
					//Valid input if the player's total money is greater than or
					//equal to the player's current wager.
					if(player.getTotalMoney() >= player.getWager())
					{
						validInput = true;
					}
					else
					{
						System.out.println(player.getName() + " you do not have enough money");
					}
				}
				else
				{
					System.out.println("Invalid input");
				}
			}
			System.out.println();
			keyboard.nextLine();
			
			//If the input is H.
			if(input.equalsIgnoreCase("H"))
			{
				hit(player, i);
			}
			//If the input is S.
			else if(input.equalsIgnoreCase("S"))
			{
				stand(player, i);
			}
			//If the input is DD.
			else
			{
				doubleDown(player, i);
			}
		}
	}
	
	/**
	 * Returns half of the player's wager but they're unable to win participate
	 * in the rest of this round.
	 * 
	 * @param player The current player.
	 */
	private void surrender(Player player)
	{
		System.out.println(player.getName() + " surrenders");
		System.out.println(player.getName() + " half of his wager is returned\n");
		//Returns half the player's wager, and sets their wager to 0.
		try 
		{
			player.setTotalMoney(player.getTotalMoney() + (player.getWager() / 2));
			player.setWager(0);
		} 
		catch (PlayerException e) 
		{
			e.printStackTrace();
		}
		
		player.setSurrendered(true);
	}
	
	/**
	 * Plays out the dealer's turn, the dealer's turn follows a basic set of 
	 * rules that allow for no choice.
	 */
	private void dealersTurn()
	{
		Dealer dealer = (Dealer) players[players.length - 1];
		System.out.println(dealer.getName() + "'s turn");
		dealer.flipCardInHand(1, 0);
		System.out.println(dealer.getName() + "'s face down card was " + dealer.getHand(0).getCard(1));
		System.out.println(dealer.getName() + "'s score is " + dealer.getHand(0).getHandScore());
		
		//While the dealer's score is less than 17
		while(dealer.getHand(0).getHandScore() < 17)
		{
			System.out.println(dealer.getName() + " adds a card to his hand");
			dealer.addToHand(deck.getCard(), 0);
			System.out.println(dealer.getName() + "'s total is now " + dealer.getHand(0).getHandScore());
		}
		
		if(dealer.getHand(0).getHandScore() > BLACKJACK)
		{
			System.out.println("Dealer busts all non busted players win");
		}
			
		System.out.println();
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
