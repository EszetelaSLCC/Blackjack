package blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Tests the classes of package blackjack for development.
 * Not intended for production use.
 * @author Edward Szetela & Jacob Slack
 *
 */
public class BlackjackTest {

	public static void main(String[] args) throws InterruptedException {
		
		// Declare program variables, only one per runtime
		Deck cardDeck = new Deck();
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		boolean continueGame = false;
		// Player's bank, starting at 10 points.
		double playerBank = 10.0;
		
		// Loop while player wants to keep playing new hands
		do {
			// Declare game variables, new for each hand played
			List<Card> playerInitialHand = new ArrayList<>();
			List<Card> dealerInitialHand = new ArrayList<>();
	
			Match gameMatch = new Match();
			GameState state;
			
			// Deal initial hands
			dealHand(cardDeck, playerInitialHand);
			dealHand(cardDeck, dealerInitialHand);
			
			Hand playerHand = new PlayerHand(0, playerInitialHand);
			Hand dealerHand = new DealerHand(0, dealerInitialHand);
			
			// Check game state on deal for blackjacks
			state = gameMatch.update(dealerHand, playerHand, false);
			
			// If blackjack, show all cards as game is over
			if (state == GameState.LOSE_DEALER_BLACKJACK || state == GameState.PUSH_BLACKJACK 
					|| state == GameState.WIN_PLAYER_BLACKJACK) {
				printPlayerHand(playerHand);
		
				printFullDealerHand(dealerHand);
			}
			// No blackjack, show player cards and dealer's first card only until player stands
			else {
				printPlayerHand(playerHand);
		
				printHiddenDealerHand(dealerHand);
			}
			// Print game state for testing
			System.out.println(state);
	
			System.out.println();

			// If no blackjack, continue normal play
			if (!(state == GameState.LOSE_DEALER_BLACKJACK || state == GameState.PUSH_BLACKJACK 
					|| state == GameState.WIN_PLAYER_BLACKJACK)) {
				// Loop while game state is playing
				do {
					// ask player to hit or stand
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
					
					printPlayerHand(playerHand);
		
					if (!playerStanding) {
						printHiddenDealerHand(dealerHand);
					}
					
					if (playerStanding) {
						printFullDealerHand(dealerHand);
						if (dealerHand.getHandScore() < 18) {
							// Dealer needs to hit, start dealer hit loop until 18 or over
							do {
								System.out.println("Dealer hitting...");
								Thread.sleep(2000);
								Card dealerCard = cardDeck.drawCard();
								dealerHand.addCard(dealerCard);
								printFullDealerHand(dealerHand);
							} while (dealerHand.getHandScore() < 18);
						}
					}
				
					// Print current game state
					state = gameMatch.update(dealerHand, playerHand, playerStanding);
					System.out.println(state + "\n");
				} while (state == GameState.PLAYING);
			}
			// Change points in player's bank from game outcome.  Blackjack pays 1.5 times bet,
			// other wins pay same as bet of 1 point, losses all lose bet of 1 point.
			// Dealer blackjack is just a normal loss.
			switch (state) {
			case WIN_PLAYER_BLACKJACK:
				playerBank += 1.5;
				break;
			case WIN_DEALER_BUSTS:
			case WIN_DEALER_LOWER_SCORE:
				playerBank += 1.0;
				break;
			case LOSE_DEALER_BLACKJACK:
			case LOSE_PLAYER_BUSTS:
			case LOSE_PLAYER_LOWER_SCORE:
				playerBank -= 1.0;
				break;
			default:
				// Push, push blackjack, or God forbid, playing, all leave points alone.
				break;
			}
			System.out.printf("Your current bank: %.1f\n",playerBank);
			
			// Shuffle cards back into deck for next hand.
			cardDeck.shuffle(playerHand, dealerHand);
			
			if (playerBank <= 0) {
				System.out.println("You're bankrupt.  Better luck next time.");
				continueGame = false;
			}
			else {
				// Ask player if they want to break out of outer loop and end program.
				System.out.println("Another hand of blackjack? 1 for yes, 2 for no.");
				if (input.nextInt() == 1)
					continueGame = true;
				else
					continueGame = false;
			}
		} while (continueGame);
	}

	private static void printHiddenDealerHand(Hand dealerHand) {
		System.out.println("dealerHandScore hidden");
		System.out.println(dealerHand.getCardsInHand().get(0).getFace() + 
				dealerHand.getCardsInHand().get(0).getSuit());
	}

	private static void printFullDealerHand(Hand dealerHand) {
		System.out.println("dealerHandScore: " + dealerHand.getHandScore());
		dealerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
	}

	private static void printPlayerHand(Hand playerHand) {
		System.out.println("playerHandScore: " + playerHand.getHandScore());
		playerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
	}

	private static void dealHand(Deck cardDeck, List<Card> playerInitialHand) {
		for (int i = 0; i < 2; i++) {
			playerInitialHand.add(cardDeck.drawCard());
		}
	}
}
