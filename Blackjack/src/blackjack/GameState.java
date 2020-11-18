/**
 * 
 */
package blackjack;

/**
 * Enumerates the various game states of the game blackjack.
 * @author Edward Szetela
 *
 */
public enum GameState {
	
	LOSE_DEALER_BLACKJACK,
	PUSH_BLACKJACK,
	PUSH,
	WIN_PLAYER_BLACKJACK,
	LOSE_PLAYER_BUSTS,
	// LOSE_PLAYER_LOWER_SCORE,
	WIN_DEALER_BUSTS,
	// WIN_DEALER_LOWER_SCORE,
	PLAYING

}
