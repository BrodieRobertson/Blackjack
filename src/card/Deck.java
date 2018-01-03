package card;

import java.util.Arrays;
import java.util.Random;

/**
 * A deck is an object with a data representation of at least 1 deck of 52 
 * French playing cards but supports up to 8.
 * 
 * @author Brodie Robertson
 * @version 1.0.0
 * @since 1.0.0
 */
public class Deck implements Cloneable
{
	/**
	 * The cards in the deck.
	 */
	private Card[] cards;
	/**
	 * The number of standard decks in the the deck.
	 */
	private int numOfDecks;
	/**
	 * The size of a standard deck.
	 */
	private static final int DECKSIZE = 52;
	/**
	 * The minimum number of decks.
	 */
	private static final int MINNUMOFDECKS = 1;
	/**
	 * The maximum number of decks.
	 */
	private static final int MAXNUMOFDECKS = 8;
	
	/**
	 * Constructs a deck with a specified number of decks.
	 * 
	 * @param numOfDecks The number of standard decks in the deck.
	 * @since 1.0.0
	 */
	public Deck(int numOfDecks)
	{
		setNumOfDecks(numOfDecks);
		createDeck();
	}
	
	/**
	 * Constructs a deck with using the attributes of another deck, program 
	 * ends with a null argument.
	 * 
	 * @param other Deck being copied
	 * @since 1.0.0
	 */
	public Deck(Deck other)
	{
		try
		{
			if(other == null)
			{
				throw new NullPointerException("Null in Deck constructor");
			}
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		cards = new Card[this.cards.length];
		for(int i = 0; i < cards.length; i++)
		{
			cards[i] = new Card(other.cards[i]);
		}
	}
	
	/**
	 * String representation of the deck in the form.
	 * <br><pre>
	 * RED KING of DIAMONDS 10
	 * RED TEN of DIAMONDS 10
	 * RED ACE of HEARTS 11
	 * RED TWO of DIAMONDS 2
	 * BLACK FOUR of CLUBS 4
	 * RED JACK of HEARTS 10
	 * RED FIVE of DIAMONDS 5
	 * RED SIX of DIAMONDS 6
	 * RED QUEEN of DIAMONDS 10
	 * RED KING of HEARTS 10
	 * ...
	 * </pre>
	 * 
	 * @return String representation of the deck.
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * @since 1.0.0
	 */
	@Override
	public String toString() 
	{
		String cardList = "";
		for(int i = 0; i < cards.length; i++)
		{
			cardList += cards[i] + "\n";
		}
		
		return cardList;
	}
	
	/**
	 * Checks if the deck is equal to another object.
	 * 
	 * @param obj The object to compare this deck with.
	 * @return Whether the objects are equal.
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @since 1.0.0
	 */
	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		Deck other = (Deck) obj;
		if (!Arrays.equals(cards, other.cards))
		{
			return false;
		}
		if (numOfDecks != other.numOfDecks)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Clone method for the deck.
	 * 
	 * @return A copy of the deck.
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 * @since 1.0.0
	 */
	@Override
	public Deck clone()
	{
		Deck clone = null;
		try
		{
			clone = (Deck) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		
		for(int i = 0; i < cards.length; i++)
		{
			if(cards[i] != null)
			{
				clone.cards[i] = cards[i].clone();
			}
		}
		return clone;
	}

	/**
	 * Creates the deck with the standard set of French playing cards except 
	 * for the 2 jokers for the total number of decks specified in the 
	 * constructor and shuffles the deck.
	 * 
	 * @since 1.0.0
	 */
	private void createDeck()
	{
		cards = new Card[DECKSIZE * numOfDecks];
		int cardsAdded = 0;
		for(int i = 0; i < numOfDecks; i++)
		{
			int suitsCreated = 0;
			Colour colour = Colour.BLACK;
			for(Suit suit : Suit.values())
			{
				for(Face face : Face.values())
				{
					cards[cardsAdded] = new Card(colour, suit, face);
					cardsAdded++;
				}
				suitsCreated++;
				if(suitsCreated == 2)
				{
					colour = Colour.RED;
				}	
			}
		}
		shuffle();
	}
	
	/**
	 * Gets the number of decks.
	 * 
	 * @return The number of standard decks in the the deck.
	 * @since 1.0.0
	 */
	public int getNumOfDecks()
	{
		return numOfDecks;
	}
	
	/**
	 * Sets the number of decks, the problems ends if the number of decks is
	 * greater than the maximum number of decks or less than the minimum
	 * number of decks.
	 * 
	 * @param The number of standard decks in the the deck.
	 * @since 1.0.0
	 */
	private void setNumOfDecks(int numOfDecks)
	{
		try
		{
			if(numOfDecks < MINNUMOFDECKS || numOfDecks > MAXNUMOFDECKS)
			{
				throw new DeckException("Invalid number of decks: " + numOfDecks);
			}
		}
		catch(DeckException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		this.numOfDecks = numOfDecks;
	}
	
	/**
	 * Gets the last card in the deck and removes it from the deck.
	 * 
	 * @return A card from the deck.
	 * @since 1.0.0
	 */
	public Card getCard()
	{
		if(cards.length == 0)
		{
			try 
			{
				throw new DeckException("No cards remaining in the deck");
			} 
			catch (DeckException e) 
			{
				e.printStackTrace();
			}
		}
		
		Card card = cards[cards.length - 1];
		cards[cards.length - 1] = null;
		decreaseDeckSize();
		return card;
	}
	
	/**
	 * Decreases the size of the deck to the number of cards now in the deck,
	 * used when a card is removed by getCard.
	 * 
	 * @since 1.0.0
	 */
	private void decreaseDeckSize()
	{
		Card[] newCards = new Card[cards.length - 1];
		
		for(int i = 0; i < cards.length - 1; i++)
		{
			newCards[i] = cards[i];
		}
		
		cards = newCards;
	}
	
	/**
	 * Shuffles the deck, by swapping the positions of the first half of the
	 * deck with random positions in the second half.
	 * 
	 * @since 1.0.0
	 */
	public void shuffle()
	{
		int mid = cards.length / 2;
		Random rand = new Random();
		for(int i = 0; i < mid; i++)
		{
			int randIndex = mid + rand.nextInt(mid);
			Card temp = cards[i];
			cards[i] = cards[randIndex];
			cards[randIndex] = temp;
		}
	}
	
	/**
	 * Debug method to ensure that the correct number of cards of a certain 
	 * colour are within the deck.
	 * 
	 * @param colour The colour to search for.
	 * @return The number of cards with the correct colour.
	 * @since 1.0.0
	 */
	public String countColour(Colour colour)
	{
		int count = 0;
		for(int i = 0; i < cards.length; i++)
		{
			if(cards[i].getColour() == colour)
			{
				count++;
			}
		}
		
		return colour.toString() + " " + count;
	}
	
	/**
	 * Debug method to ensure that the correct number of cards of a certain
	 * suit are within the deck.
	 * 
	 * @param suit The suit to search for.
	 * @return The number of cards with the correct suit.
	 * @since 1.0.0
	 */
	public String countSuit(Suit suit)
	{
		int count = 0;
		for(int i = 0; i < cards.length; i++)
		{
			if(cards[i].getSuit() == suit)
			{
				count++;
			}
		}
		
		return suit.toString() + " " + count;
	}
	
	/**
	 * Debug method to ensure that the correct number of cards with a certain
	 * face are within the deck.
	 * 
	 * @param face The face to search for.
	 * @return The number of the cards with the correct face.
	 * @since 1.0.0
	 */
	public String countFace(Face face)
	{
		int count = 0;
		for(int i = 0; i < cards.length; i++)
		{
			if(cards[i].getFace() == face)
			{
				count++;
			}
		}
		
		return face.toString() + " " + count;
	}
}
