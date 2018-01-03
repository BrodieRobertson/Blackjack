package card;

/**
 * A card is an object with a data representation of a French playing card.
 * 
 * @author Brodie Robertson
 * @version 1.0.0
 * @since 1.0.0
 */
public class Card implements Cloneable
{
	/**
	 * The colour of the card.
	 */
	private Colour colour;
	/**
	 * The suit of the card.
	 */
	private Suit suit;
	/**
	 * The face of the card.
	 */
	private Face face;
	/**
	 * The value of the card, determined by the face.
	 */
	private int value;	
	/**
	 * Whether the card is face up or not.
	 */
	private boolean faceUp = true;
	
	/**
	 * Constructs a card with values representing the attributes of a card.
	 * 
	 * @param colour The colour of the card.
	 * @param suit The suit of the card.
	 * @param face The face of the card.
	 * @since 1.0.0
	 */
	public Card(Colour colour, Suit suit, Face face)
	{
		this.colour = colour;
		this.suit = suit;
		this.face = face;
		setValue();
	}
	
	/**
	 * Constructs a card using the attributes of another card, program ends
	 * with a null argument.
	 * 
	 * @param other Card to be copied.
	 * @since 1.0.0
	 */
	public Card(Card other)
	{
		//If the argument is null, throws exception and handles it.
		try
		{
			if(other == null)
			{
				throw new NullPointerException("Null in Card constructor");
			}
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		
		this.colour = other.colour;
		this.suit = other.suit;
		this.face = other.face;
		this.value = other.value;
		this.faceUp = other.faceUp;
	}
	
	/**
	 * String representation of the card in the form
	 * <br><pre>
	 * RED JACK of DIAMONDS 10
	 * </pre>
	 * 
	 * @return String representation of the card.
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		if(faceUp)
		{
			return colour.name().toString() + " " + face.toString() + " of " 
				+ suit.toString();
		}
		else
		{
			return "FACE DOWN CARD";
		}
	}
	
	/**
	 * Checks if the card is equal to another object.
	 * 
	 * @param obj The object to compare this card with.
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
		
		Card other = (Card) obj;
		if (colour != other.colour)
		{
			return false;
		}
		if (face != other.face)
		{
			return false;
		}
		if (suit != other.suit)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Clone method for the card.
	 * 
	 * @return A copy of the card.
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 * @since 1.0.0
	 */
	@Override
	public Card clone()
	{
		Card clone = null;
		try 
		{
			clone = (Card) super.clone();
		} 
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		
		return clone;
	}
	
	/**
	 * Gets the colour of the card.
	 * 
	 * @return The colour of the card.
	 * @since 1.0.0
	 */
	public Colour getColour()
	{
		return colour;
	}
	
	/**
	 * Gets the suit of the card.
	 * 
	 * @return The suit of the card.
	 * @since 1.0.0
	 */
	public Suit getSuit()
	{
		return suit;
	}
	
	/**
	 * Gets the face of the card.
	 * 
	 * @return The face of the card.
	 * @since 1.0.0
	 */
	public Face getFace()
	{
		return face;
	}
	
	/**
	 * Gets the value of the card.
	 * 
	 * @return The value of the card.
	 * @since 1.0.0
	 */
	public int getValue()
	{
		return value;
	}
	
	/**
	 * Sets the value of the card based on the face of the card.
	 * 
	 * @since 1.0.0
	 */
	public void setValue()
	{
		if(face == Face.ACE)
		{
			if(value == 11)
			{
				value = 1;
			}
			else
			{
				value = 11;
			}
		}
		else if(face == Face.TWO)
		{
			value = 2;
		}
		else if(face == Face.THREE)
		{
			value = 3;
		}
		else if(face == Face.FOUR) 
		{
			value = 4;
		}
		else if(face == Face.FIVE)
		{
			value = 5;
		}
		else if(face == Face.SIX)
		{
			value = 6;
		}
		else if(face == Face.SEVEN)
		{
			value = 7;
		}
		else if(face == Face.EIGHT)
		{
			value = 8;
		}
		else if(face == Face.NINE)
		{
			value = 9;
		}
		else
		{
			value = 10;
		}
	}
	
	/**
	 * Gets whether the card is face up or not.
	 * 
	 * @return boolean Whether the card is face up or not.
	 * @since 1.0.0
	 */
	public boolean getFaceUp()
	{
		return faceUp;
	}
	
	/**
	 * Flips whether the card is face up or not.
	 * 
	 * @since 1.0.0
	 */
	public void flipCard()
	{
		if(faceUp)
		{
			faceUp = false;
		}
		else
		{
			faceUp = true;
		}
	}
}
