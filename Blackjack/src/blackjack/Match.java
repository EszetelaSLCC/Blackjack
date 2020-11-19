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

	private GameState gameState;

	/**
	 * Initializes a match of blackjack.  Matches always start with gamestate PLAYING.
	 * @param gameState
	 */
	public Match() {
		this.gameState = GameState.PLAYING;
	}
	
	/**
	 * Updates the gamestate from the two passed in hands of cards.
	 * Evaluates according to standard blackjack rules where two cards totaling 21 (ace and 10/face card)
	 * is blackjack.  Point value over 21 is bust.
	 * @param dealerHand the dealer's current hand.
	 * @param playerHand the player's current hand.
	 * @return the updated gamestate.
	 * @throws IllegalStateException if the game state is not a valid game state.
	 */
	public GameState update(Hand dealerHand, Hand playerHand) {
		// Check for invalid low score
		if (dealerHand.getHandScore() < 2 || playerHand.getHandScore() < 2)
			throw new IllegalStateException("Invalid score.  Too low for hand of at least one card.");
		// Check for invalid double bust
		if (playerHand.getHandScore() > 21 && dealerHand.getHandScore() > 21)
			throw new IllegalStateException("Double bust.  Not a valid game state.");
		// Check for blackjacks
		if ((dealerHand.getCardsInHand().size() == 2 && dealerHand.getHandScore() == 21) ||
				(playerHand.getCardsInHand().size() == 2 && playerHand.getHandScore() == 21)) {
			// At least one blackjack exists.  Game is over.  Evaluate who won.
			if ((dealerHand.getCardsInHand().size() == 2 && dealerHand.getHandScore() == 21) &&
					(playerHand.getCardsInHand().size() == 2 && playerHand.getHandScore() == 21))
				// Double blackjack.  Very rare.
				gameState = GameState.PUSH_BLACKJACK;
			if ((dealerHand.getCardsInHand().size() == 2 && dealerHand.getHandScore() == 21) &&
					!(playerHand.getCardsInHand().size() == 2 && playerHand.getHandScore() == 21))
				// Dealer has blackjack, player does not.  Dealer wins.
				gameState = GameState.LOSE_DEALER_BLACKJACK;
			if (!(dealerHand.getCardsInHand().size() == 2 && dealerHand.getHandScore() == 21) &&
					(playerHand.getCardsInHand().size() == 2 && playerHand.getHandScore() == 21))
				// Dealer does not have blackjack, player does.  Player wins.
				gameState = GameState.WIN_PLAYER_BLACKJACK;
		}
		// Check for busts.
		else if (playerHand.getHandScore() > 21 || dealerHand.getHandScore() > 21) {
			// At least one bust exists.  Game is over.  Evaluate who won.
			if (playerHand.getHandScore() > 21)
			// Player busts.
			gameState = GameState.LOSE_PLAYER_BUSTS;
			if (dealerHand.getHandScore() > 21) 
			// Dealer busts.
			gameState = GameState.WIN_DEALER_BUSTS;
		}
		else
		// Checked all end states.  Game continues.
		gameState = GameState.PLAYING;
		
		return gameState;
	}

	/**
	 * Returns the current game state of the match.
	 * @return the gamestate
	 */
	public GameState getGamestate() {
		return gameState;
	}
}
