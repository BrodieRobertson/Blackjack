package card;

import java.util.Arrays;

import logic.Table;

/**
 * The representation of the hand, this class defines a hand with attributes
 * representing the cards and whether the hand is a split hand. 
 * 
 * @author Brodie Robertson
 *
 */
public class Hand implements Cloneable
{
	/**
	 * The cards in the hand.
	 */
	private Card[] cards;
	/**
	 * Whether the hand has been split or not.
	 */
	private boolean split;
	
	/**
	 * Constructor for the hand.
	 */
	public Hand()
	{
		super();
		cards = new Card[0];
		split = false;
	}
	
	/**
	 * Copy constructor for the hand, if the argument is null the program ends.
	 * @param other
	 */
	public Hand(Hand other)
	{
		cards = new Card[other.cards.length];
		for(int i = 0; i < cards.length; i++)
		{
			cards[i] = new Card(other.cards[i]);
		}
		split = other.split;
	}
	
	/**
	 * The String representation of the hand in the form
	 * <br><pre>
	 * Hand Score: 20
     * BLACK TEN of SPADES 10
     * BLACK KING of SPADES 10
     * </pre>
     * 
     * @return The string representation of the hand.
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String cardList = "Hand Score: " + getHandScore() + "\n";
		for(int i = 0; i < cards.length; i++)
		{
			if(cards[i] != null && cards[i].getFaceUp())
			{
				cardList += cards[i] + "\n";
			}
			else
			{
				cardList += "Face Down Card\n";
			}
		}
		
		return cardList;
	}

	/**
	 * Checks if the hand is equal to another hand.
	 * 
	 * @param obj The object to compare this hand with.
	 * @return Whether the objects are equal.
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
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
		Hand other = (Hand) obj;
		if (!Arrays.equals(cards, other.cards))
		{
			return false;
		}
		if (split != other.split)
		{
			return false;
		}
		return true;
	}

	/**
	 * The clone method for the hand.
	 * 
	 * @return A copy of the hand.
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Hand clone()
	{
		Hand clone = null;
		try
		{
			clone = (Hand) super.clone();
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
	 * Gets the cards remaining in the hand.
	 * 
	 * @return The number of cards in the hand.
	 */
	public int getCardsRemaining()
	{
		return cards.length;
	}
	
	/**
	 * Adds a card to the hand, the program ends if the card is null.
	 * 
	 * @param card The card being added.
	 */
	public void addCard(Card card)
	{	
		try
		{	
			if(card == null)
			{
				throw new NullPointerException("Card added to the hand is null");
			}
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		increaseHandSize();
		cards[cards.length - 1] = new Card(card);
	}
	
	/**
	 * Gets the card at the specified index, the programs ends if the index is 
	 * invalid.
	 * 
	 * @param index The index of the card.
	 * @return The card at the specified index.
	 */
	public Card getCard(int index)
	{	
		try
		{
			if(index < 0 || index >= cards.length)
			{
				throw new HandException("Invalid index: " + index);
			}
		}
		catch(HandException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		Card temp = new Card(cards[index]);
		return temp;
	}
	
	public Card[] getCards()
	{
		Card[] temp = new Card[cards.length];
		for(int i = 0; i < temp.length; i++)
		{
			if(cards[i] != null)
			{
				temp[i] = cards[i].clone();
			}
		}
		
		return cards;
	}
	
	/**
	 * Removes the card at the specified index, the program ends if the index 
	 * is invalid.
	 * 
	 * @param index The index of the card.
	 */
	public void removeCard(int index)
	{
		try
		{	
			if(index < 0 || index >= cards.length)
			{
				throw new HandException("Invalid index: " + index);
			}
		}
		catch(HandException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		cards[index] = null;
		reorderHand();
		decreaseHandSize();
	}
	
	/**
	 * Reorders the hand so that if there is a null in the hand it's moved to
	 * the end, uses when a card is removed with removeCard.
	 */
	private void reorderHand()
	{
		for(int i = 0; i < cards.length; i++)
		{
			if(cards[i] == null && i != cards.length - 1)
			{
				cards[i] = cards[i + 1];
			}
		}
	}
	
	/**
	 * Decreases the size of the hand to the number of cards in the hand, used
	 * when a card is removed with removeCard.
	 */
	private void decreaseHandSize()
	{
		Card[] newCards = new Card[cards.length - 1];
		
		for(int i = 0; i < newCards.length; i++)
		{
			newCards[i] = cards[i];
		}
		
		cards = newCards;
	}
	
	/**
	 * Increases the size of the hand to the number of cards in the hand, used
	 * when a card is added with addCard.
	 */
	private void increaseHandSize()
	{
		Card[] newCards = new Card[cards.length + 1];
		
		for(int i = 0; i < cards.length; i++)
		{
			newCards[i] = cards[i];
		}
		
		cards = newCards;
	}
	
	/**
	 * Gets whether the hand has been split or not
	 * 
	 * @return Whether the hand has been split or not.
	 */
	public boolean getSplit()
	{
		return split;
	}
	
	/**
	 * Sets whether the hand has been split.
	 * 
	 * @param split Whether the hand has been split now.
	 */
	public void setSplit(boolean split)
	{
		this.split = split;
	}
	
	/**
	 * Gets the hand score, counts the card if it's face up.
	 * 
	 * @return The hand score.
	 */
	public int getHandScore()
	{
		int score = 0;
		boolean aceFound = false;
		for(int i = 0; i < cards.length; i++)
		{
			if(cards[i] != null && cards[i].getFaceUp())
			{
				score += cards[i].getValue();
				if(cards[i].getFace() == Face.ACE)
				{
					aceFound = true;
				}
			}
		}
		
		//If the hand has an ace and the score is greater than blackjack.
		if(score > Table.BLACKJACK && aceFound)
		{
			int i = 0;
			while(i < cards.length && score > Table.BLACKJACK)
			{
				if(cards[i] != null && cards[i].getFaceUp())
				{
					if(cards[i].getValue() == 11)
					{
						cards[i].setValue();
						score -= 10;
					}
				}
				i++;
			}
		}
		
		//If the hand has an ace and the score is less than blackjack.
		if(score < Table.BLACKJACK && aceFound)
		{
			int i = 0;
			while((i < cards.length && score < Table.BLACKJACK) && score + 10 <=
					Table.BLACKJACK) 
			{
				if(cards[i] != null && cards[i].getFaceUp())
				{
					if(cards[i].getValue() == 1 )
					{
						cards[i].setValue();
						score += 10;
					}
				}
				i++;
			}
		}
		
		return score;
	}
	
	/**
	 * Flips the card in the hand at the specified index, the program ends if
	 * the index is invalid.
	 * 
	 * @param index The index of the card.
	 */
	public void flipCardInHand(int index)
	{
		try
		{
			if(index < 0 | index >= cards.length)
			{
				throw new HandException("Invalid card index: " + index);
			}
		}
		catch(HandException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		cards[index].setFaceUp();
	}
}
