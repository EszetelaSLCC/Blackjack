/**
 *
 */
package blackjack;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Tests the classes of package blackjack for development.
 * Not intended for production use.
 * @author Edward Szetela & Jacob Slack
 *
 */
public class BlackjackTest {

	public static void main(String[] args) {
		
		Deck cardDeck = new Deck();
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		
		do {
			ArrayList<Card> playerInitialHand = new ArrayList<>();
			ArrayList<Card> dealerInitialHand = new ArrayList<>();
	
			Match gameMatch = new Match();
			GameState state;
			
			dealHand(cardDeck, playerInitialHand);
			dealHand(cardDeck, dealerInitialHand);
			
			Hand playerHand = new PlayerHand(0, playerInitialHand);
			Hand dealerHand = new DealerHand(0, dealerInitialHand);
			
			state = gameMatch.update(dealerHand, playerHand, false);
			System.out.println(state);
			
			if (state == GameState.LOSE_DEALER_BLACKJACK || state == GameState.PUSH_BLACKJACK 
					|| state == GameState.WIN_PLAYER_BLACKJACK) {
				System.out.println("playerHandScore: " + playerHand.getHandScore());
				playerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
		
				System.out.println("dealerHandScore: " + dealerHand.getHandScore());
				dealerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
			}
			else {
				System.out.println("playerHandScore: " + playerHand.getHandScore());
				playerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
		
				System.out.println("dealerHandScore hidden");
				System.out.println(dealerHand.getCardsInHand().get(0).getFace() + 
						dealerHand.getCardsInHand().get(0).getSuit());
			}

	
			System.out.println();
	
			//hit
			//boolean hit = true;
			//if (hit) {
				//Card tempCard = cardDeck.drawCard();
				//playerHand.addCard(tempCard);
			//}
	
		
			do {
				// ask player
				System.out.println("1 to hit, 2 to stand:  ");
				int playerChoice = input.nextInt();
				boolean playerStanding = false;
				
				if (playerChoice == 1) {
					// player hit
					Card playerCard = cardDeck.drawCard();
					playerHand.addCard(playerCard);
					playerStanding = false;
				}
				else if (playerChoice == 2) {
					// player stands
					playerStanding = true;
				}
				// dealer
				if (dealerHand.getHandScore() < 18 && playerStanding) {
					// dealer must hit with these conditions
					Card dealerCard = cardDeck.drawCard();
					dealerHand.addCard(dealerCard);
				}
				
				System.out.println("playerHandScore: " + playerHand.getHandScore());
				playerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
	
				if (!playerStanding) {
					// player not standing, show only one dealer card
					System.out.println("dealerHandScore hidden");
					System.out.println(dealerHand.getCardsInHand().get(0).getFace() + 
							dealerHand.getCardsInHand().get(0).getSuit());
				}
				
				if (playerStanding) {
					// player standing, show dealer cards and score
					System.out.println("dealerHandScore: " + dealerHand.getHandScore());
					dealerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
				}
			
				state = gameMatch.update(dealerHand, playerHand, playerStanding);
				System.out.println(state + "\n");
			} while (state == GameState.PLAYING);
	
			//cardDeck.printDeck();
			cardDeck.shuffle(playerHand, dealerHand);
			//cardDeck.printDeck();
			
			System.out.println("Another hand of blackjack? 1 for yes, 2 for no.");
		} while (input.nextInt() == 1);


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
