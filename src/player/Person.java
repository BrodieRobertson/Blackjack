package player;

import java.util.Arrays;

import card.Card;
import card.Hand;
import card.HandException;

/**
 * The abstract representation of a person, this class defines a person with
 * attributes representing the hands of cards of the person, and the name of
 * the person.
 * 
 * @author Brodie Robertson
 *
 */
public abstract class Person implements Cloneable
{
	/**
	 * The hands of the person.
	 */
	private Hand[] hands;
	/**
	 * The name of the person.
	 */
	private String name;
	
	/**
	 * The constructor of the person, takes the name of the person as an
	 * argument.
	 * 
	 * @param name The name of the person.
	 */
	public Person()
	{
		super();
		startingHand();
	}
	
	/**
	 * String representation of the person in the form
	 * <br><pre>
	 * Human 0
	 * Hand:
     * Hand Score: 20
     * BLACK TEN of SPADES 10
     * BLACK KING of SPADES 10
     * </pre>
     * 
     * @return The string representation of the person.
     * 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String string = name + "\n";
		for(int i = 0; i < hands.length; i++)
		{
			if(hands.length > 1)
			{
				string += "Hand: " + (i + 1) + "\n" + hands[i].toString();
			}
			else
			{
				string += "Hand:\n" + hands[i].toString();
			}
		}
		
		return string;
	}

	/**
	 * Checks if the person is equal to another object.
	 * 
	 * @param obj The object to compare the person with.
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
		Person other = (Person) obj;
		if (!Arrays.equals(hands, other.hands))
		{
			return false;
		}
		if (name == null) 
		{
			if (other.name != null)
			{
				return false;
			}
		} 
		else if (!name.equals(other.name))
		{
			return false;
		}
		return true;
	}

	/**
	 * The clone method for the person.
	 * 
	 * @return A copy of the person.
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Person clone()
	{
		Person clone = null;
		try 
		{
			clone = (Person) super.clone();
		} 
		catch (CloneNotSupportedException e) 
		{
			e.printStackTrace();
		}
		
		clone.hands = new Hand[hands.length];
		for(int i = 0; i < hands.length; i++)
		{
			clone.hands[i] = hands[i].clone();
		}
		
		return clone;
	}
	
	/**
	 * Gets the hands of the person.
	 * 
	 * @return The hands of the person.
	 */
	public Hand[] getHands()
	{
		Hand[] handsCopy = new Hand[hands.length];
		
		for(int i = 0; i < hands.length; i++)
		{
			if(hands[i] != null)
			{
				handsCopy[i] = new Hand(hands[i]);
			}
		}
		
		return handsCopy;
	}
	
	/**
	 * Sets the hands of the person, if the argument is null the program ends.
	 * 
	 * @param hand The new set of hands.
	 */
	public void setHands(Hand[] hand)
	{
		try
		{
			if(hand.equals(null))
			{
				throw new Exception("Hand can't be null");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		Hand[] copy = new Hand[hand.length];;
		for(int i = 0; i < hand.length; i++)
		{
			if(hand[i] != null)
			{
				copy[i] = new Hand(hand[i]);
			}
		}
		
		hands = copy;
	}
	
	/**
	 * Sets the starting hand of the person.
	 */
	public void startingHand()
	{
		hands = new Hand[1];
		hands[0] = new Hand();
	}
	
	/**
	 * Gets a hand of the person at a specific index, if the index is invalid
	 * the program ends.
	 * 
	 * @return The hand at the specified index.
	 */
	public Hand getHand(int index)
	{
		validateHandIndex(index);
		return new Hand(hands[index]);
	}
	
	/**
	 * Increases the number of hands and adds a new hand.
	 */
	public void addHand()
	{
		increaseHands();
		hands[hands.length - 1] = new Hand();
	}
	
	/**
	 * Increases the total number of hands the person has.
	 */
	private void increaseHands()
	{
		Hand[] newHands = new Hand[hands.length + 1];
		for(int i = 0; i < hands.length; i++)
		{
			newHands[i] = hands[i];
		}
		
		hands = newHands;
	}
	
	/**
	 * Gets the name of the person.
	 * 
	 * @return The name of the person.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Sets the name of the person.
	 * 
	 * @param name The name of the person.
	 */
	public void setName(String name) throws PersonException
	{
		if(name.equals("") || name.equals(null))
		{
			throw new PersonException("Invalid name: " + name);
		}
		
		this.name = name;
	}
	
	/**
	 * Adds a card to specified hand, the program ends if the card is null or 
	 * the index is invalid.
	 * 
	 * @param card The card being added.
	 * @param index The index of the specified hand.
	 */
	public Card addToHand(Card card, int index)
	{
		validateHandIndex(index);
		hands[index].addCard(card);
		return new Card(card);
	}
	
	/**
	 * Removes a specified card from a specified hand, the program ends if the
	 * the hand or card index is invalid.
	 * 
	 * @param handIndex The index of the specified hand.
	 * @param cardIndex The index of the specified card.
	 */
	public void removeFromHand(int handIndex, int cardIndex)
	{
		validateHandIndex(handIndex);
		hands[handIndex].removeCard(cardIndex);
	}
	
	/**
	 * Flips a specified card in a specified hand, the program ends if the hand
	 * or card index is invalid.
	 * 
	 * @param cardIndex The index of the specified hand.
	 * @param handIndex The index of the specified card.
	 */
	public void flipCardInHand(int cardIndex, int handIndex)
	{
		validateHandIndex(handIndex);
		hands[handIndex].flipCardInHand(cardIndex);
	}
	
	public void setHandSplit(int handIndex)
	{
		validateHandIndex(handIndex);
		hands[handIndex].setSplit(true);
	}
	
	/**
	 * Validates the hand index, the program ends if the index is invalid.
	 * 
	 * @param index The hand index.
	 */
	private void validateHandIndex(int index)
	{
		try
		{
			if(index < 0 || index >= hands.length)
			{
				throw new PersonException("Invalid Hand index: " + index);
			}
		}
		catch(PersonException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
}
