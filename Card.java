// This class represents one playing card.
public class Card {
	public static final int SPADES   = 0;
	public static final int HEARTS   = 1;
	public static final int CLUBS    = 2;
	public static final int DIAMONDS = 3;

	public static final int ACE      = 1;
	public static final int TWO      = 2;
	public static final int THREE    = 3;
	public static final int FOUR     = 4;
	public static final int FIVE     = 5;
	public static final int SIX      = 6;
	public static final int SEVEN    = 7;
	public static final int EIGHT    = 8;
	public static final int NINE     = 9;
	public static final int TEN      = 10;
	public static final int JACK     = 11;
	public static final int QUEEN    = 12;
	public static final int KING     = 13;

	int suit;
	int face;
	String[] suits = {"Spades", "Hearts", "Clubs", "Diamonds"}; 
	String[] faces = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
	
	public Card(int cardSuit, int cardFace) {
		suit=cardSuit; 
		face=cardFace;
	}

	public int getSuit() {
		return suit; 
	}

	public int getFace() {
		return face; 
	}
	
	public int getValue() {
		if (face>=10) 
			return 10; 
		return face; 
	}
	
	public String toString() {
		return faces[face-1]+" of "+suits[suit]; 
	}	
}