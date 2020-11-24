package blackjack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
		double playerBank = 10.0;
		
		// Loop while player wants to keep playing new hands
		do {
			// Declare game variables, new for each hand played
			ArrayList<Card> playerInitialHand = new ArrayList<>();
			ArrayList<Card> dealerInitialHand = new ArrayList<>();
	
			Match gameMatch = new Match();
			GameState state;
			
			// Deal initial hands
			dealHand(cardDeck, playerInitialHand);
			dealHand(cardDeck, dealerInitialHand);
			
			Hand playerHand = new PlayerHand(0, playerInitialHand);
			DealerHand dealerHand = new DealerHand(0, dealerInitialHand);
			
			// Check game state on deal for blackjacks
			state = gameMatch.update(dealerHand, playerHand, false);
			
			// If blackjack, show all cards as game is over
			if (state == GameState.LOSE_DEALER_BLACKJACK || state == GameState.PUSH_BLACKJACK 
					|| state == GameState.WIN_PLAYER_BLACKJACK) {
				System.out.println("playerHandScore: " + playerHand.getHandScore());
				playerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
		
				System.out.println("dealerHandScore: " + dealerHand.getHandScore());
				dealerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
			}
			// No blackjack, show player cards and dealer's first card only until player stands
			else {
				System.out.println("playerHandScore: " + playerHand.getHandScore());
				playerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
		
				System.out.println("dealerHandScore hidden");
				System.out.println(dealerHand.getCardsInHand().get(0).getFace() + 
						dealerHand.getCardsInHand().get(0).getSuit());
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
					
					// Show player cards and score
					System.out.println("playerHandScore: " + playerHand.getHandScore());
					playerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
		
					if (!playerStanding) {
						// player not standing, show only one dealer card.  Dealer takes no action yet.
						System.out.println("dealerHandScore hidden");
						System.out.println(dealerHand.getCardsInHand().get(0).getFace() + 
								dealerHand.getCardsInHand().get(0).getSuit());
					}
					
					if (playerStanding) {
						// player standing, show dealer cards and score.  Dealer hits according to rules.
						System.out.println("dealerHandScore: " + dealerHand.getHandScore());
						dealerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
						while (dealerHand.dealerCanHit()) { //dealerHand.getHandScore() < 18
							// Dealer needs to hit, start dealer hit loop until 18 or over
							System.out.println("Dealer hitting...");
							Thread.sleep(2000);
							Card dealerCard = cardDeck.drawCard();
							dealerHand.addCard(dealerCard);
							System.out.println("dealerHandScore: " + dealerHand.getHandScore());
							dealerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
						}
					}
				
					// Print current game state
					state = gameMatch.update(dealerHand, playerHand, playerStanding);
					System.out.println(state + "\n");
				} while (state == GameState.PLAYING);
			}
	
			// Shuffle cards back into deck for next hand.
			cardDeck.shuffle(playerHand, dealerHand);
			
			// Update playerBank depending on results of game
			switch (state) {
			case WIN_PLAYER_BLACKJACK:
				playerBank += 1.5;
				break;
			case LOSE_DEALER_BLACKJACK:
			case LOSE_PLAYER_BUSTS:
			case LOSE_PLAYER_LOWER_SCORE:
				playerBank -= 1;
				break;
			case WIN_DEALER_BUSTS:
			case WIN_DEALER_LOWER_SCORE:
				playerBank += 1;
				break;
			default:
				break;
			}
			
			// Ask player if they want to break out of outer loop and end program.
			System.out.println("Another hand of blackjack? 1 for yes, 2 for no.");
		} while (input.nextInt() == 1);
	
		// Write highscore file
		File highScore = new File("src/blackjack/resources/highscore.txt");
		if (highScore.exists()) {
			// File exists.  Append new score.
			try (FileWriter writer = new FileWriter(highScore, true); BufferedWriter bw = new BufferedWriter(writer)) {
				bw.write("Player 1 score : " + playerBank);
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			// File does not exist.  Create.
			try (PrintWriter writer = new PrintWriter(highScore); BufferedWriter bw = new BufferedWriter(writer)) {
				bw.write("High Scores");
				bw.newLine();
				bw.write("-------------");
				bw.newLine();
				bw.write("Player 1 score : " + playerBank);
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void dealHand(Deck cardDeck, ArrayList<Card> hand) {
		for (int i = 0; i < 2; i++) {
			hand.add(cardDeck.drawCard());
		}
	}
}
