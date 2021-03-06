package card;

import java.util.Arrays;

import logic.Table;

/**
 * A hand is an object with containing the a date representation of a player's 
 * hand.
 * 
 * @author Brodie Robertson
 * @version 1.7.0
 * @since 1.0.0
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
	 * Constructs a hand with default values.
	 * 
	 * @since 1.0.0
	 */
	public Hand()
	{
		super();
		cards = new Card[0];
		split = false;
	}
	
	/**
	 * Constructs a hand using the attributes of another hand, program ends
	 * with a null argument.
	 * 
	 * @param other
	 * @since 1.0.0
	 */
	public Hand(Hand other)
	{
		try
		{
			if(other == null)
			{
				throw new NullPointerException("Null in Hand constructor");
			}
		}
		catch(NullPointerException ex)
		{
			
		}
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
	 * @since 1.0.0
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
	 * @since 1.0.0
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
	 * @since 1.0.0
	 */
	public int getCardsRemaining()
	{
		return cards.length;
	}
	
	/**
	 * Adds a card to the hand, the program ends if the card is null.
	 * 
	 * @param card The card being added.
	 * @since 1.0.0
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
		
		//If the hand score is greater than Blackjack or less than Blackjack.
		if(getHandScore() > Table.BLACKJACK || getHandScore() < Table.BLACKJACK)
		{
			int aceCount = 0;
			int score = getHandScore();
			for(int i = 0; i < cards.length; i++)
			{
				if(cards[i].getFace() == Face.ACE && cards[i] != null)
				{
					aceCount++;
				}
			}
			
			int j = 0;
			while(j < cards.length && aceCount > 0)
			{
				if(score + 10 < Table.BLACKJACK && cards[j].getValue() == 1)
				{
					cards[j].setValue();
					score += 10;
					aceCount--;
				}
				else if(score > Table.BLACKJACK && cards[j].getValue() == 11)
				{
					cards[j].setValue();
					score -= 10;
					aceCount--;
				}
				j++;
			}
		}
	}
	
	/**
	 * Gets the card at the specified index, the programs ends if the index is 
	 * invalid.
	 * 
	 * @param index The index of the card.
	 * @return The card at the specified index.
	 * @see 1.0.0
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
	
	/**
	 * Gets all of the cards in the hand.
	 * 
	 * @return The cards in the hands.
	 * @since 1.0.0
	 */
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
	 * @since 1.0.0
	 */
	public void removeCard(int index)
	{
		try
		{	
			//Throws exception if index is less than 0 or greater than the 
			//number of cards in the hand.
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
	 * 
	 * @since 1.0.0
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
	 * 
	 * @since 1.0.0
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
	 * 
	 * @since 1.0.0
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
	 * @since 1.0.0
	 */
	public boolean getSplit()
	{
		return split;
	}
	
	/**
	 * Sets whether the hand has been split.
	 * 
	 * @param split Whether the hand has been split now.
	 * @since 1.0.0
	 */
	public void setSplit(boolean split)
	{
		this.split = split;
	}
	
	/**
	 * Gets the hand score, counts the card if it's face up.
	 * 
	 * @return The hand score.
	 * @since 1.0.0
	 */
	public int getHandScore()
	{
		int score = 0;
		for(int i = 0; i < cards.length; i++)
		{
			if(cards[i] != null && cards[i].getFaceUp())
			{
				score += cards[i].getValue();
			}
		}
		
		return score;
	}
	
	/**
	 * Flips the card in the hand at the specified index, the program ends if
	 * the index is invalid.
	 * 
	 * @param index The index of the card.
	 * @since 1.0.0
	 */
	public void flipCardInHand(int index, boolean faceUp)
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
		
		cards[index].setFaceUp(faceUp);
	}
}
