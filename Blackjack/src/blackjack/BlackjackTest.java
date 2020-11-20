/**
 *
 */
package blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tests the classes of package blackjack for development.
 * Not intended for production use.
 * @author Edward Szetela
 *
 */
public class BlackjackTest {

	public static void main(String[] args) {
		Deck cardDeck = new Deck();
		ArrayList<Card> playerInitialHand = new ArrayList<>();
		ArrayList<Card> dealerInitialHand = new ArrayList<>();

		dealHand(cardDeck, playerInitialHand);
		dealHand(cardDeck, dealerInitialHand);

		Hand playerHand = new PlayerHand(0, playerInitialHand);
		Hand dealerHand = new DealerHand(0, dealerInitialHand);

		System.out.println("playerHandScore: " + playerHand.getHandScore());
		playerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));

		System.out.println("dealerHandScore: " + dealerHand.getHandScore());
		dealerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));

		System.out.println();

		//hit
		boolean hit = true;
		if (hit) {
			Card tempCard = cardDeck.drawCard();
			playerHand.addCard(tempCard);
		}

		System.out.println("playerHandScore after player hit: " + playerHand.getHandScore());
		playerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));

		System.out.println("dealerHandScore still same after player hit?: " + dealerHand.getHandScore());
		dealerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));


		/* Think we need to make the update method and the gameState field static
		 *  on the Match class but we can discuss tomorrow
		 */
		// boolean playerStand = false;
		// Match.update(dealerHand, playerHand);

	}

	private static void dealHand(Deck cardDeck, ArrayList<Card> hand) {
		for (int i = 0; i < 2; i++) {
			hand.add(cardDeck.drawCard());
		}
	}


}
