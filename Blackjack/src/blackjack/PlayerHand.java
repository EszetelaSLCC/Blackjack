package blackjack;

import java.util.ArrayList;

/**
 * Extends Hand class. 
 * 
 * @author Jacob Slack
 *
 */
public class PlayerHand extends Hand {

	/**
	 * Calls to super class to construct hand for player.
	 * @param handScore
	 * @param cardsInHand
	 */
	public PlayerHand(int handScore, ArrayList<Card> cardsInHand) {
		super(handScore, cardsInHand);
	}

}
