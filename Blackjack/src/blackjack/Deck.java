/**
 * 
 */
package blackjack;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Defines a deck of playing cards.  Each deck begins with 52 cards.
 * The deck consists of standard suits (Club, Diamond, Heart, Spades) and
 * the usual values (2 - 10, Jack, Queen, King, Ace) for each suit.
 * There are no jokers in the deck.
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
		Collections.shuffle(cardDeck);
	}
	
	/**
	 * Adds the cards in the player's hand and dealer's hand back in to 
	 * the deck and shuffles the deck.
	 */
	public void shuffle(Hand playerHand, Hand dealerHand) {
		cardDeck.addAll(dealerHand.getCardsInHand());
		cardDeck.addAll(playerHand.getCardsInHand());
		Collections.shuffle(cardDeck);
	}
	
	/**
	 * Draws the top card in the deck, which is the card at index 0.
	 * Removes the card from the deck as it is in play.
	 * @return the drawn Card.
	 * @throws NullPointerException if there are no cards left in the deck.
	 */
	public Card drawCard() {
		if (cardDeck.size() == 0)
			throw new NullPointerException("The deck is out of cards.");
		
		Card tempCard = cardDeck.get(0);
		cardDeck.remove(0);
		return tempCard;
	}

	/**
	 * Prints a test output of the card deck.  Each card will be printed on a separate
	 * line then the number of cards will be printed.
	 */
	public void printDeck() {
		cardDeck.forEach(c -> {
			System.out.print(c + "\n");
		}); 
		System.out.println("Number of cards: " + cardDeck.size());
	}
}
