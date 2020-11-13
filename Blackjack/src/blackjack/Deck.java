/**
 * 
 */
package blackjack;

import java.util.ArrayList;

/**
 * Defines a deck of playing cards.  Each deck begins with 52 cards.
 * @author Edward Szetela
 *
 */
public class Deck {
	
	private ArrayList<Card> cardDeck = new ArrayList<>();
	private final String[] SUITS = new String[] {"C", "D", "H", "S"};
	private final String[] CARDFACES = new String[] 
			{"2", "3", "4", "5", "6", "7", "8", "9", "10", "A", "J", "Q", "K"};
	
	
	/**
	 * Initializes a deck of cards with the 52 Card objects from a standard
	 * playing card set.
	 */
	public Deck() {
		for (int s = 0; s < SUITS.length; s++) {
			for (int f = 0; f < CARDFACES.length; f++) {
				String imagePath = (CARDFACES[f] + SUITS[s] + ".png");
				int value;
				switch (CARDFACES[f]) {
				case "A":
					value = 11;
					break;
				case "J":
				case "Q":
				case "K":
					value = 10;
					break;
				default:
					value = Integer.parseInt(CARDFACES[f]);
					break;
				}
				cardDeck.add(new Card(value, imagePath, CARDFACES[f], SUITS[s]));
			}
		}
		/*cardDeck.forEach(c -> {
			System.out.print(c.getValue() + " " + c.getFace() + c.getSuit() + " " + c.getImagePath() + "\n");
		}); // Test Line - Prints deck to console */
	}
}
