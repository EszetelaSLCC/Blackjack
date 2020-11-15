/**
 * 
 */
package blackjack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 *
 */
public class Hand {
	private int handScore;
	private ArrayList<Card> cardsInHand;
	
	
	
	/**
	 * @param handScore
	 * @param cardsInHand
	 */
	public Hand(int handScore, ArrayList<Card> cardsInHand) {
		this.handScore = handScore;
		this.cardsInHand.addAll(cardsInHand);
	}



	/**
	 * @return 
	 * 
	 */
	public List<Card> getCardsInHand() {
		return null;
		// TODO Auto-generated method stub
		
	}

}
