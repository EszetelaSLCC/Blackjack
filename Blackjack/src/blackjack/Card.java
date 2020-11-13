package blackjack;


/**
 * Defines a playing card object with a face value and suit.
 * The card also has a point value in blackjack and an image filename to the card image.
 * @author Edward Szetela
 *
 */
public class Card {
	
	private int value;
	private String imagePath;
	private String face;
	private String suit;
	
	/**
	 * Initializes a playing card.
	 * @param value The point value of the playing card in blackjack.  
	 * @param imagePath The image filename of the playing card.
	 * @param face The pip value or face card value.  E.g. "ten" or "king".
	 * @param suit The suit of the playing card.
	 */
	public Card(int value, String imagePath, String face, String suit) {
		this.value = value;
		this.imagePath = imagePath;
		this.face = face;
		this.suit = suit;
	}

	/**
	 * Returns the blackjack point value of the card.  Pip value for two through ten.
	 * Ten for all face cards.  Eleven for aces.
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Returns the image filename of the playing card.
	 * @return the imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * Returns the face value of the card.  E.g. "ace" or "three".
	 * @return the face
	 */
	public String getFace() {
		return face;
	}

	/**
	 * Returns the suit of the card.
	 * @return the suit
	 */
	public String getSuit() {
		return suit;
	}
	
	

}
