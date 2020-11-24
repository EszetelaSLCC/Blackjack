package blackjack;

import java.util.ArrayList;

public class DealerHand extends Hand {
	private final int HIT_LIMIT = 18;

	public DealerHand(int handScore, ArrayList<Card> cardsInHand) {
		super(handScore, cardsInHand);
	}

	public boolean dealerCanHit() {
		return (this.handScore < HIT_LIMIT) ? true : false;
	}

}
