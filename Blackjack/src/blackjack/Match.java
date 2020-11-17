/**
 * 
 */
package blackjack;

/**
 * Defines a match of the game blackjack.  This class
 * includes the logic determining the game state and who is the winner.
 * @author Edward Szetela
 *
 */
public class Match {

	private GameState gamestate;

	/**
	 * Initializes a match of blackjack.  Matches always start with gamestate 
	 * DEAL.  
	 * Creates two hands, one for the player and one for the dealer.
	 * @param gamestate
	 */
	public Match() {
		this.gamestate = GameState.DEAL;
		Hand dealerHand = new Hand();
		Hand playerHand = new Hand();
		
		for (int i = 1; i < 3; i++) {
			dealerHand.addCard();
			playerHand.addCard();
		}
		
	}
	
	/**
	 * Updates the gamestate from the two passed in hands of cards.
	 * Returns the gamestate.
	 * @param dealerHand the dealer's current hand.
	 * @param playerHand the player's current hand.
	 * @throws IllegalStateException if the game state is not a valid game state.
	 */
	public GameState update(Hand dealerHand, Hand playerHand) {
		// Check for invalid low score
		if (dealerHand.getHandScore() < 2 || playerHand.getHandScore() < 2)
			throw new IllegalStateException("Invalid score.  Too low for hand of at least two cards.");
		// Check for blackjacks
		if (dealerHand.getCardsInHand().size() == 2 && dealerHand.getHandScore() == 21) 
			// Dealer has blackjack.  Check for player blackjack.
			if (playerHand.getCardsInHand().size() == 2 && playerHand.getHandScore() == 21) {
				// Double blackjack on the deal.  Very rare.
				gamestate = GameState.PUSH_BLACKJACK;
				return GameState.PUSH_BLACKJACK;
			}
			else {
				// Dealer blackjack with no player blackjack.  Dealer wins.
				gamestate = GameState.LOSE_DEALER_BLACKJACK;
				return GameState.LOSE_DEALER_BLACKJACK;
			}
		if (playerHand.getCardsInHand().size() == 2 && playerHand.getHandScore() == 21) {
			// Player has blackjack.  Already checked for dealer blackjack so player wins.
			gamestate = GameState.WIN_PLAYER_BLACKJACK;
			return GameState.WIN_PLAYER_BLACKJACK;
		}
		// Check for busts
		if (playerHand.getHandScore() > 21 && dealerHand.getHandScore() > 21)
			// Double bust.  Not a valid game state.  Throw error.
			throw new IllegalStateException("Double bust.  Not a valid game state.");
		if (playerHand.getHandScore() > 21) {
			// Player busts.
			gamestate = GameState.LOSE_PLAYER_BUSTS;
			return GameState.LOSE_PLAYER_BUSTS;
		}
		if (dealerHand.getHandScore() > 21) {
			// Dealer busts.
			gamestate = GameState.WIN_DEALER_BUSTS;
			return GameState.WIN_DEALER_BUSTS;
		}
		// Checked all end states.  Game continues.
		gamestate = GameState.PLAYING;
		return GameState.PLAYING;
	}

	/**
	 * Returns the current game state of the match.
	 * @return the gamestate
	 */
	public GameState getGamestate() {
		return gamestate;
	}
}
