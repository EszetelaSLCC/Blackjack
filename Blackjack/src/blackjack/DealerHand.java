package blackjack;

import java.util.ArrayList;

/**
 * Extends Hand class for special functionality for a dealer's hand.
 * 
 * @author Jacob Slack
 *
 */
public class DealerHand extends Hand {
	private final int HIT_LIMIT = 18;

	/**
	 * Constructs dealer's hand. Calls super constructor.
	 */
	public DealerHand(int handScore, ArrayList<Card> cardsInHand) {
		super(handScore, cardsInHand);
	}

	/**
	 * Returns boolean indicating whether dealer should still hit based on
	 * dealer's current handScore compared to HIT_LIMIT.
	 * @return boolean
	 */
	public boolean dealerCanHit() {
		return (this.handScore < HIT_LIMIT) ? true : false;
	}

}
