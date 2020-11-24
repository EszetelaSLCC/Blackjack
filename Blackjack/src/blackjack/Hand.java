package blackjack;

import java.util.ArrayList;
import java.util.List;

/**
 * Constructs Hand instance to allow for calculating and storing score
 * of <code>cardsInHand</code>
 * 
 * @author Jacob Slack
 */
public class Hand {
	protected int handScore;
	private ArrayList<Card> cardsInHand;

	/**
	 * Constructs Hand instance and initializes <code>handScore</code> and
	 * <code>cardsInHand</code>. Calls to <code>calculateHandScore()</code>
	 * to calculate initial <code>handScore</code> from list of 2 cards passed
	 * in during call to constructor.
	 * 
	 * @param handScore - int 
	 * @param cardsInHand - ArrayList<Card>
	 */
	public Hand(int handScore, ArrayList<Card> cardsInHand) {
		this.handScore = handScore;
		ArrayList<Card> passedCards = new ArrayList<>(cardsInHand);
		this.cardsInHand = passedCards;

		calculateHandScore();
	}

	/**
	 * Returns the list of cards in the hand for which the method is called.
	 * @return cardsInHand - List<Card>
	 */
	public List<Card> getCardsInHand() {
		return cardsInHand;
	}
	
	public Card getCardFromHandByIndex(int index) {
		return cardsInHand.get(index);
	}

	/**
	 * Adds the card argument to the current Hand instance's <code>cardsInHand</code>
	 * and then calls the calculateHandScore() method to update handScore for
	 * the current hand.
	 */
	public void addCard(Card card) {
		cardsInHand.add(card);
		calculateHandScore();
	}

	/**
	 * Returns current score of the cards in <code>cardsInHand</code>
	 * for the Hand instance for which it was called.
	 * @return int handScore
	 */
	public int getHandScore() {
		return handScore;
	}

	protected void calculateHandScore() {
		handScore = 0;
		int acesInHand = 0;

		for (int i = 0; i < cardsInHand.size(); i++) {
			if (cardsInHand.get(i).getFace() == "A") {
				acesInHand++;
			}
			else {
				handScore += cardsInHand.get(i).getValue();
			}
		}

		for (int i = 0; i < acesInHand; i++) {
			if ((handScore + 11) > 21) {
				handScore++;
			}
			else {
				handScore += 11;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		cardsInHand.forEach(c -> sb.append(c.getFace() + c.getSuit() + " "));
		sb.append(" score: " + handScore);
		return sb.toString();
	}
	
	


}
