package blackjack;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jacob Slack
 *
 */
public class Hand {
	private int handScore;
	private ArrayList<Card> cardsInHand;




	public Hand(int handScore, List<Card> playerInitialHand) {
		this.handScore = handScore;
		ArrayList<Card> passedCards = new ArrayList<>(playerInitialHand);
		this.cardsInHand = passedCards;

		calculateHandScore();
	}

	/**
	 *
	 * @return
	 *
	 */
	public List<Card> getCardsInHand() {
		return cardsInHand;
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
	 *
	 * @return int handScore
	 */
	public int getHandScore() {
		return handScore;
	}

	private void calculateHandScore() {
		int fixedCardsScore = 0;
		int acesInHand = 0;

		for (int i = 0; i < cardsInHand.size(); i++) {
			if (cardsInHand.get(i).getFace() == "A") {
				acesInHand++;
			}
			else {
				fixedCardsScore += cardsInHand.get(i).getValue();
			}

		}

		handScore = fixedCardsScore;

		for (int i = 0; i < acesInHand; i++) {
			if ((handScore + 11) > 21) {
				handScore++;
			}
			else {
				handScore += 11;
			}
		}
	}


}
