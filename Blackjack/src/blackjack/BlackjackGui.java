package blackjack;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.awt.event.ActionEvent;

/**
 * Class for Blackjack game interface and methods.
 * 
 * @author Jacob Slack
 *
 */
@SuppressWarnings("serial")
public class BlackjackGui extends JFrame {

	private JPanel contentPane;
	private JLabel lblIndicatorText;
	private JLabel lblCurrentBankCount;
	private JLabel lblDealerPanelTitle;
	private JLabel lblPlayerPanelTitle;
	private JButton btnStand;
	private JButton btnHit;
	private JButton btnDeal;
	private JButton btnQuit;
	private JLabel lblPlayerCardFirst;
	private JLabel lblPlayerCardSecond;
	private JLabel lblPlayerCardThird;
	private JLabel lblPlayerCardFourth;
	private JLabel lblPlayerCardFifth;
	private JLabel lblPlayerCardSixth;
	private JLabel lblDealerCardFirst;
	private JLabel lblDealerCardSecond;
	private JLabel lblDealerCardThird;
	private JLabel lblDealerCardFourth;
	private JLabel lblDealerCardFifth;
	private JLabel lblDealerCardSixth;
	private ArrayList<JLabel> dealerCardLabels = new ArrayList<>();
	private ArrayList<JLabel> playerCardLabels = new ArrayList<>();
	private static Deck cardDeck = new Deck();
	private int playerBank = 10;
	private static ArrayList<Card> playerInitialHand = new ArrayList<>();
	private static ArrayList<Card> dealerInitialHand = new ArrayList<>();
	private static ArrayList<Integer> top3Scores = new ArrayList<>();
	private Hand playerHand;
	private DealerHand dealerHand;
	private Match gameMatch;
	private JPanel gameButtonPanel_1;
	private static String file;
	private JPanel playerInfoPanel_1;
	private static JLabel lblScore1Num = new JLabel();
	private static JLabel lblScore2Num = new JLabel();
	private static JLabel lblScore3Num = new JLabel();


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BlackjackGui frame = new BlackjackGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		file = "src/blackjack/resources/high_scores.txt";
		
		try(Scanner reader = new Scanner(new File(file))) {
			while(reader.hasNextLine()) {
				String line = reader.nextLine();
				
				if (line != null) {
					int readInScore = Integer.parseInt(line);
					top3Scores.add(readInScore);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		lblScore1Num.setText(top3Scores.get(0).toString());
		lblScore2Num.setText(top3Scores.get(1).toString());
		lblScore3Num.setText(top3Scores.get(2).toString());
	}

	/**
	 * Constructs the Blackjack game interface for initial game state.
	 */
	public BlackjackGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1150, 1150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLabel lblGameTitle = createGameTitleLabel();
		contentPane.add(lblGameTitle, BorderLayout.NORTH);

		JPanel playerInfoPanel = createPlayerInfoPanel();
		contentPane.add(playerInfoPanel, BorderLayout.WEST);
		
		createAvatarPanel(playerInfoPanel);
		createBankPanel(playerInfoPanel);
		createHighScorePanel(playerInfoPanel);

		JPanel gameTablePanel = createGamePanelComponents();
		contentPane.add(gameTablePanel, BorderLayout.CENTER);
		
		Collections.addAll(dealerCardLabels, lblDealerCardFirst, lblDealerCardSecond,
				lblDealerCardThird, lblDealerCardFourth, lblDealerCardFifth, lblDealerCardSixth
				);
		Collections.addAll(playerCardLabels, lblPlayerCardFirst, lblPlayerCardSecond,
				lblPlayerCardThird, lblPlayerCardFourth, lblPlayerCardFifth, lblPlayerCardSixth
				);
	}

	private JButton createDealButton() {
		JButton btnDeal = new JButton("DEAL");
		btnDeal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// clears the initial array lists that are used to construct a hand so that we can populate them with fresh cards each time new game starts.
				playerInitialHand.clear();
				dealerInitialHand.clear();
				
				// populates initial array lists with new cards so array lists can be passed as args to Hand constructors.
				dealHand(cardDeck, playerInitialHand);
				dealHand(cardDeck, dealerInitialHand);
				
				playerHand = new PlayerHand(0, playerInitialHand);
				dealerHand = new DealerHand(0, dealerInitialHand);
				
				// resets images for the cards in dealer and player hands in gui to match table and appear empty again.
				resetHandsGui();
				
				lblIndicatorText.setText("Game Begins!");
				btnDeal.setEnabled(false);
				btnDeal.setVisible(false);
				toggleHitAndStandVisibility(true);
				btnQuit.setEnabled(false);
				btnQuit.setVisible(false);
				
				startMatch();
				
			}
		});
		btnDeal.setFont(new Font("Tahoma", Font.PLAIN, 16));
		return btnDeal;
	}

	private JButton createHitButton() {
		JButton btnHit = new JButton("HIT");
		btnHit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Card cardToAdd = cardDeck.drawCard();
				playerHand.addCard(cardToAdd);
				gameMatch.update(dealerHand, playerHand, false);
				updatePlayerHandGui();
				updateGuiGameState();
			}
		});
		btnHit.setVisible(false);
		btnHit.setEnabled(false);
		btnHit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		return btnHit;
	}
	
	private JButton createStandButton() {
		JButton btnStand = new JButton("STAND");
		btnStand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDealerHandGui();

				while (dealerHand.dealerCanHit()) { 
					Card dealerCard = cardDeck.drawCard();
					dealerHand.addCard(dealerCard);
					
					// sets ImageIcon for newly drawn card in dealer's hand
					updateDealerHandGui();
				}
				
				gameMatch.update(dealerHand, playerHand, true);
				updateGuiGameState();
					
			}

			private void updateDealerHandGui() {
				int cardGuiToUpdate = dealerHand.getCardsInHand().size();
				
				switch (cardGuiToUpdate) {
					case 2:
						lblDealerCardSecond.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + dealerHand.getCardFromHandByIndex(1).getImagePath())));
						break;
					case 3:
						lblDealerCardThird.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + dealerHand.getCardFromHandByIndex(2).getImagePath())));
						break;
					case 4:
						lblDealerCardFourth.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + dealerHand.getCardFromHandByIndex(3).getImagePath())));
						break;
					case 5:
						lblDealerCardFifth.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + dealerHand.getCardFromHandByIndex(4).getImagePath())));
						break;
					case 6:
						lblDealerCardSixth.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + dealerHand.getCardFromHandByIndex(5).getImagePath())));
						break;
					default:
						break;
				}
				
			}
		});
		btnStand.setEnabled(false);
		btnStand.setVisible(false);
		btnStand.setFont(new Font("Tahoma", Font.PLAIN, 16));
		return btnStand;
	}
	
	private JButton createQuitButton() {
		btnQuit = new JButton("QUIT");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean added = false;
				int count = 0;
				
				while (!added && count < 3) {
					if (playerBank >= top3Scores.get(count)) {
						top3Scores.add(playerBank);
						added = true;
					}
					count++;
				}
				
				Collections.sort(top3Scores, Collections.reverseOrder());
				
				if (top3Scores.size() > 3) {
					top3Scores.remove(top3Scores.size() - 1);
				}
				
				try (PrintWriter writer = new PrintWriter(file); BufferedWriter bw = new BufferedWriter(writer)) {
					for (int i = 0; i < 3; i++) {
						bw.write("" + top3Scores.get(i));
						bw.newLine();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			
			contentPane.setVisible(false);
			dispose();
			}
		});
		btnQuit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		return btnQuit;
	}
	
	private static void dealHand(Deck cardDeck, ArrayList<Card> hand) {
		for (int i = 0; i < 2; i++) {
			hand.add(cardDeck.drawCard());
		}
	}

	private void startMatch() {
		gameMatch = new Match();
		
		// Check game state on deal for blackjacks
		gameMatch.update(dealerHand, playerHand, false);
		
		lblDealerCardFirst.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + dealerHand.getCardFromHandByIndex(0).getImagePath())));
		lblDealerCardSecond.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/blue_back.png")));
		lblPlayerCardFirst.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + playerHand.getCardFromHandByIndex(0).getImagePath())));
		lblPlayerCardSecond.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + playerHand.getCardFromHandByIndex(1).getImagePath())));
		
		updateGuiGameState();
		
	}
	
	private void updatePlayerHandGui() {
		int cardGuiToUpdate = playerHand.getCardsInHand().size();
		
		switch (cardGuiToUpdate) {
			case 3:
				lblPlayerCardThird.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + playerHand.getCardFromHandByIndex(2).getImagePath())));
				break;
			case 4:
				lblPlayerCardFourth.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + playerHand.getCardFromHandByIndex(3).getImagePath())));
				break;
			case 5:
				lblPlayerCardFifth.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + playerHand.getCardFromHandByIndex(4).getImagePath())));
				break;
			case 6:
				lblPlayerCardSixth.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + playerHand.getCardFromHandByIndex(5).getImagePath())));
				break;
			default:
				break;
		}
	}
	
	private void updateGuiGameState() {
		switch (gameMatch.getGamestate()) {
			case LOSE_DEALER_BLACKJACK:
				lblIndicatorText.setText("Dealer has BLACKJACK! You lose!");
				playerBank -= 1;
				updateGuiForEndGameStates();
				evaluatePlayAgainOptions();
				cardDeck.shuffle(playerHand, dealerHand);
				break;
			case PUSH_BLACKJACK:
				lblIndicatorText.setText("You and Dealer have BLACKJACK! It's a push!");
				updateGuiForEndGameStates();
				evaluatePlayAgainOptions();
				cardDeck.shuffle(playerHand, dealerHand);
				break;
			case PUSH:
				lblIndicatorText.setText("It's a push");
				updateGuiForEndGameStates();
				evaluatePlayAgainOptions();
				cardDeck.shuffle(playerHand, dealerHand);
				break;
			case WIN_PLAYER_BLACKJACK:
				lblIndicatorText.setText("You have BLACKJACK! You win!");
				playerBank += 2;
				updateGuiForEndGameStates();
				evaluatePlayAgainOptions();
				cardDeck.shuffle(playerHand, dealerHand);
				break;
			case LOSE_PLAYER_BUSTS:
				lblIndicatorText.setText("You busted! You lose!");
				playerBank -= 1;
				updateGuiForEndGameStates();
				evaluatePlayAgainOptions();
				cardDeck.shuffle(playerHand, dealerHand);
				break;
			case LOSE_PLAYER_LOWER_SCORE:
				lblIndicatorText.setText("Dealer wins! You lose!");
				playerBank -= 1;
				updateGuiForEndGameStates();
				evaluatePlayAgainOptions();
				cardDeck.shuffle(playerHand, dealerHand);
				break;
			case WIN_DEALER_BUSTS:
				lblIndicatorText.setText("Dealer busts! You win!");
				playerBank += 1;
				updateGuiForEndGameStates();
				evaluatePlayAgainOptions();
				cardDeck.shuffle(playerHand, dealerHand);
				break;
			case WIN_DEALER_LOWER_SCORE:
				lblIndicatorText.setText("You win!");
				playerBank += 1;
				updateGuiForEndGameStates();
				evaluatePlayAgainOptions();
				cardDeck.shuffle(playerHand, dealerHand);
				break;
			case PLAYING:
				lblIndicatorText.setText("Hit or Stand?");
				lblDealerPanelTitle.setText("Dealer Cards - Total: ??");
				lblPlayerPanelTitle.setText("Player Cards - Total: " + playerHand.getHandScore());
				break;
		}
		
	}

	private void updateGuiForEndGameStates() {
		toggleHitAndStandVisibility(false);
		revealDealerCards();
		lblCurrentBankCount.setText(String.valueOf(playerBank));
		lblDealerPanelTitle.setText("Dealer Cards - Total: " + dealerHand.getHandScore());
		lblPlayerPanelTitle.setText("Player Cards - Total: " + playerHand.getHandScore());
	}

	private void toggleHitAndStandVisibility(boolean visible) {
		btnStand.setEnabled(visible);
		btnStand.setVisible(visible);
		btnHit.setEnabled(visible);
		btnHit.setVisible(visible);
	}

	private void evaluatePlayAgainOptions() {
		if (playerBank > 0) {
			btnDeal.setEnabled(true);
			btnDeal.setVisible(true);
			btnQuit.setEnabled(true);
			btnQuit.setVisible(true);
			btnDeal.setText("PLAY AGAIN?");
		}
		else {
			btnQuit.setEnabled(true);
			btnQuit.setVisible(true);
			lblIndicatorText.setText("Bankrupt!!! Thanks for playing!");
		}
	}

	private void revealDealerCards() {
		for (int i = 0; i < dealerHand.getCardsInHand().size(); i++) {
			dealerCardLabels.get(i).setIcon(new ImageIcon(BlackjackGui.class.getResource(
					"/blackjack/resources/" + dealerHand.getCardFromHandByIndex(i).getImagePath()))
					);
		}
	}

	private void resetHandsGui() {
		for (int i = 0; i < dealerCardLabels.size(); i++) {
			dealerCardLabels.get(i).setIcon(null);
		}
		
		for (int i = 0; i < playerCardLabels.size(); i++) {
			playerCardLabels.get(i).setIcon(null);
		}
	}
	
	private JPanel createGamePanelComponents() {
		JPanel gameTablePanel = createGameTablePanel();
		
		JPanel dealerPanel = createDealerPanelComponents();
		gameTablePanel.add(dealerPanel);
		
		JPanel playerPanel = createPlayerPanelComponents();
		gameTablePanel.add(playerPanel);
		
		JPanel gameControlPanel = createGameControlPanelComponents();	
		gameTablePanel.add(gameControlPanel);
		
		return gameTablePanel;
	}

	private JPanel createGameControlPanelComponents() {
		JPanel gameControlPanel = createGameControlPanel();
			
		JPanel gameButtonPanel = createGameButtonPanel();
		gameControlPanel.add(gameButtonPanel);
		
		btnHit = createHitButton();
		gameButtonPanel.add(btnHit);
		
		btnDeal = createDealButton();
		gameButtonPanel.add(btnDeal);
		
		JButton btnQuit = createQuitButton();
		gameButtonPanel_1.add(btnQuit);
		
		btnStand = createStandButton();
		gameButtonPanel.add(btnStand);
		
		lblIndicatorText = createLabelIndicatorText();
		gameControlPanel.add(lblIndicatorText);
		return gameControlPanel;
	}

	private JPanel createPlayerPanelComponents() {
		JPanel playerPanel = createPlayerPanel();
		
		JLabel lblPlayerPanelTitle = createLabelPlayerPanelTitle();
		playerPanel.add(lblPlayerPanelTitle, BorderLayout.NORTH);
		
		JPanel playerCardsPanel = createPlayerCardsPanelComponents();
		playerPanel.add(playerCardsPanel, BorderLayout.CENTER);
		
		return playerPanel;
	}

	private JPanel createPlayerCardsPanelComponents() {
		JPanel playerCardsPanel = createPlayerCardsPanel();
		
		lblPlayerCardFirst = createLabelPlayerCardFirst();
		playerCardsPanel.add(lblPlayerCardFirst);
		
		lblPlayerCardSecond = createLabelPlayerCardSecond();
		playerCardsPanel.add(lblPlayerCardSecond);
		
		lblPlayerCardThird = createLabelPlayerCardThird();
		playerCardsPanel.add(lblPlayerCardThird);
		
		lblPlayerCardFourth = createLabelPlayerCardFourth();
		playerCardsPanel.add(lblPlayerCardFourth);
		
		lblPlayerCardFifth = createLabelPlayerCardFifth();
		playerCardsPanel.add(lblPlayerCardFifth);
		
		lblPlayerCardSixth = createLabelPlayerCardSixth();
		playerCardsPanel.add(lblPlayerCardSixth);
		
		return playerCardsPanel;
	}

	private JPanel createDealerPanelComponents() {
		JPanel dealerPanel = createDealerPanel();
		
		JLabel lblDealerPanelTitle = createDealerPanelTitle();
		dealerPanel.add(lblDealerPanelTitle, BorderLayout.NORTH);
		
		JPanel dealerCardsPanel = createDealerCardsPanelComponents();	
		dealerPanel.add(dealerCardsPanel, BorderLayout.CENTER);
		
		return dealerPanel;
	}

	private JPanel createDealerCardsPanelComponents() {
		JPanel dealerCardsPanel = createDealerCardsPanel();
		
		lblDealerCardFirst = createLabelDealerCardFirst();
		dealerCardsPanel.add(lblDealerCardFirst);
		
		lblDealerCardSecond = createdLabelDealerCardSecond();
		dealerCardsPanel.add(lblDealerCardSecond);
		
		lblDealerCardThird = createLabelDealerCardThird();
		dealerCardsPanel.add(lblDealerCardThird);
		
		lblDealerCardFourth = createLabelDealerCardFourth();
		dealerCardsPanel.add(lblDealerCardFourth);
		
		lblDealerCardFifth = createLabelDealerCardFifth();
		dealerCardsPanel.add(lblDealerCardFifth);
		
		lblDealerCardSixth = createLabelDealerCardSixth();
		dealerCardsPanel.add(lblDealerCardSixth);
		
		return dealerCardsPanel;
	}

	private JLabel createLabelIndicatorText() {
		JLabel lblIndicatorText = new JLabel("Click \"DEAL\" to play");
		lblIndicatorText.setForeground(new Color(255, 255, 240));
		lblIndicatorText.setOpaque(true);
		lblIndicatorText.setFont(new Font("Tahoma", Font.BOLD, 36));
		lblIndicatorText.setHorizontalAlignment(SwingConstants.CENTER);
		lblIndicatorText.setBackground(new Color(0, 128, 0));
		return lblIndicatorText;
	}

	private JPanel createGameButtonPanel() {
		gameButtonPanel_1 = new JPanel();
		gameButtonPanel_1.setBackground(new Color(0, 128, 0));
		gameButtonPanel_1.setBorder(new EmptyBorder(50, 80, 50, 80));
		gameButtonPanel_1.setLayout(new GridLayout(1, 0, 90, 0));
		return gameButtonPanel_1;
	}

	private JPanel createGameControlPanel() {
		JPanel gameControlPanel = new JPanel();
		gameControlPanel.setBackground(new Color(0, 128, 0));
		gameControlPanel.setLayout(new GridLayout(2, 0, 0, 0));
		return gameControlPanel;
	}

	private JLabel createLabelPlayerCardSixth() {
		JLabel lblPlayerCardSixth = new JLabel("");
		lblPlayerCardSixth.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerCardSixth.setOpaque(true);
		lblPlayerCardSixth.setBackground(new Color(0, 128, 0));
		return lblPlayerCardSixth;
	}

	private JLabel createLabelPlayerCardFifth() {
		JLabel lblPlayerCardFifth = new JLabel("");
		lblPlayerCardFifth.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerCardFifth.setOpaque(true);
		lblPlayerCardFifth.setBackground(new Color(0, 128, 0));
		return lblPlayerCardFifth;
	}

	private JLabel createLabelPlayerCardFourth() {
		JLabel lblPlayerCardFourth = new JLabel("");
		lblPlayerCardFourth.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerCardFourth.setOpaque(true);
		lblPlayerCardFourth.setBackground(new Color(0, 128, 0));
		return lblPlayerCardFourth;
	}

	private JLabel createLabelPlayerCardThird() {
		JLabel lblPlayerCardThird = new JLabel("");
		lblPlayerCardThird.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerCardThird.setOpaque(true);
		lblPlayerCardThird.setBackground(new Color(0, 128, 0));
		return lblPlayerCardThird;
	}

	private JLabel createLabelPlayerCardSecond() {
		JLabel lblPlayerCardSecond = new JLabel("");
		lblPlayerCardSecond.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerCardSecond.setOpaque(true);
		lblPlayerCardSecond.setBackground(new Color(0, 128, 0));
		return lblPlayerCardSecond;
	}

	private JLabel createLabelPlayerCardFirst() {
		JLabel lblPlayerCardFirst = new JLabel("");
		lblPlayerCardFirst.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerCardFirst.setOpaque(true);
		lblPlayerCardFirst.setBackground(new Color(0, 128, 0));
		return lblPlayerCardFirst;
	}

	private JPanel createPlayerCardsPanel() {
		JPanel playerCardsPanel = new JPanel();
		playerCardsPanel.setBackground(new Color(0, 128, 0));
		playerCardsPanel.setLayout(new GridLayout(1, 0, 0, 0));
		return playerCardsPanel;
	}

	private JLabel createLabelPlayerPanelTitle() {
		lblPlayerPanelTitle = new JLabel("Player Cards");
		lblPlayerPanelTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
		lblPlayerPanelTitle.setForeground(new Color(255, 255, 255));
		lblPlayerPanelTitle.setOpaque(true);
		lblPlayerPanelTitle.setBackground(new Color(0, 128, 0));
		lblPlayerPanelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerPanelTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
		return lblPlayerPanelTitle;
	}

	private JPanel createPlayerPanel() {
		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new BorderLayout(0, 0));
		return playerPanel;
	}

	private JLabel createLabelDealerCardSixth() {
		JLabel lblDealerCardSixth = new JLabel("");
		lblDealerCardSixth.setHorizontalAlignment(SwingConstants.CENTER);
		lblDealerCardSixth.setBackground(new Color(0, 128, 0));
		lblDealerCardSixth.setOpaque(true);
		return lblDealerCardSixth;
	}

	private JLabel createLabelDealerCardFifth() {
		JLabel lblDealerCardFifth = new JLabel("");
		lblDealerCardFifth.setHorizontalAlignment(SwingConstants.CENTER);
		lblDealerCardFifth.setOpaque(true);
		lblDealerCardFifth.setBackground(new Color(0, 128, 0));
		return lblDealerCardFifth;
	}

	private JLabel createLabelDealerCardFourth() {
		JLabel lblDealerCardFourth = new JLabel("");
		lblDealerCardFourth.setHorizontalAlignment(SwingConstants.CENTER);
		lblDealerCardFourth.setOpaque(true);
		lblDealerCardFourth.setBackground(new Color(0, 128, 0));
		return lblDealerCardFourth;
	}

	private JLabel createLabelDealerCardThird() {
		JLabel lblDealerCardThird = new JLabel("");
		lblDealerCardThird.setHorizontalAlignment(SwingConstants.CENTER);
		lblDealerCardThird.setOpaque(true);
		lblDealerCardThird.setBackground(new Color(0, 128, 0));
		return lblDealerCardThird;
	}

	private JLabel createdLabelDealerCardSecond() {
		JLabel lblDealerCardSecond = new JLabel("");
		lblDealerCardSecond.setHorizontalAlignment(SwingConstants.CENTER);
		lblDealerCardSecond.setOpaque(true);
		lblDealerCardSecond.setBackground(new Color(0, 128, 0));
		return lblDealerCardSecond;
	}

	private JLabel createLabelDealerCardFirst() {
		JLabel lblDealerCardFirst = new JLabel("");
		lblDealerCardFirst.setHorizontalAlignment(SwingConstants.CENTER);
		lblDealerCardFirst.setOpaque(true);
		lblDealerCardFirst.setBackground(new Color(0, 128, 0));
		return lblDealerCardFirst;
	}

	private JPanel createDealerCardsPanel() {
		JPanel dealerCardsPanel = new JPanel();
		dealerCardsPanel.setBackground(new Color(0, 128, 0));
		dealerCardsPanel.setLayout(new GridLayout(1, 0, 0, 0));
		return dealerCardsPanel;
	}

	private JLabel createDealerPanelTitle() {
		lblDealerPanelTitle = new JLabel("Dealer Cards");
		lblDealerPanelTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
		lblDealerPanelTitle.setOpaque(true);
		lblDealerPanelTitle.setBackground(new Color(0, 128, 0));
		lblDealerPanelTitle.setForeground(new Color(255, 255, 255));
		lblDealerPanelTitle.setFont(new Font("Tahoma", Font.BOLD, 24));
		lblDealerPanelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		return lblDealerPanelTitle;
	}

	private JPanel createDealerPanel() {
		JPanel dealerPanel = new JPanel();
		dealerPanel.setLayout(new BorderLayout(0, 0));
		return dealerPanel;
	}

	private JPanel createGameTablePanel() {
		JPanel gameTablePanel = new JPanel();
		gameTablePanel.setBackground(new Color(0, 128, 0));
		gameTablePanel.setLayout(new GridLayout(3, 0, 0, 35));
		return gameTablePanel;
	}

	private void createBankPanel(JPanel playerInfoPanel) {
		JPanel bankPanel = new JPanel();
		bankPanel.setForeground(new Color(0, 0, 0));
		bankPanel.setBackground(new Color(119, 136, 153));
		bankPanel.setBorder(new EmptyBorder(0, 0, 100, 0));
		playerInfoPanel.add(bankPanel);
		bankPanel.setLayout(new GridLayout(4, 0, 0, 0));
						
		JLabel lblCurrentBank = new JLabel("Current Bank:");
		lblCurrentBank.setForeground(new Color(255, 255, 240));
		bankPanel.add(lblCurrentBank);
		lblCurrentBank.setBorder(new EmptyBorder(0, 0, 0, 0));
		lblCurrentBank.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblCurrentBank.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblCurrentBankCount = new JLabel(String.valueOf(playerBank));
		lblCurrentBankCount.setForeground(new Color(255, 255, 240));
		bankPanel.add(lblCurrentBankCount);
		lblCurrentBankCount.setBorder(new EmptyBorder(0, 0, 0, 0));
		lblCurrentBankCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrentBankCount.setFont(new Font("Tahoma", Font.PLAIN, 48));
	}

	private void createHighScorePanel(JPanel playerInfoPanel) {
		JPanel highScorePanel = new JPanel();
		highScorePanel.setForeground(new Color(0, 0, 0));
		highScorePanel.setBackground(new Color(119, 136, 153));
		highScorePanel.setLayout(new GridLayout(7, 0, 0, 0));
		playerInfoPanel.add(highScorePanel);
		
		JLabel lblHighScoreText = new JLabel("High Scores:");
		setHighScoreText(lblHighScoreText);
		highScorePanel.add(lblHighScoreText);
		
		JLabel lblScore1Text = new JLabel("First Place");
		setHighScoreText(lblScore1Text);
		highScorePanel.add(lblScore1Text);
		
		setHighScoreText(lblScore1Num);
		highScorePanel.add(lblScore1Num);
		
		JLabel lblScore2Text = new JLabel("Second Place");
		setHighScoreText(lblScore2Text);
		highScorePanel.add(lblScore2Text);
		
		setHighScoreText(lblScore2Num);
		highScorePanel.add(lblScore2Num);
		
		JLabel lblScore3Text = new JLabel("Third Place");
		setHighScoreText(lblScore3Text);
		highScorePanel.add(lblScore3Text);
		
		setHighScoreText(lblScore3Num);
		highScorePanel.add(lblScore3Num);
	}

	private void setHighScoreText(JLabel lblHighScoreText) {
		lblHighScoreText.setHorizontalAlignment(SwingConstants.CENTER);
		lblHighScoreText.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblHighScoreText.setForeground(new Color(255, 255, 240));
	}

	private void createAvatarPanel(JPanel playerInfoPanel) {
		JPanel avatarPanel = new JPanel();
		avatarPanel.setBackground(new Color(119, 136, 153));
		avatarPanel.setBorder(new EmptyBorder(100, 0, 0, 0));
		playerInfoPanel.add(avatarPanel);
		avatarPanel.setLayout(new GridLayout(2, 0, 0, 2));
		
		JLabel lblPlayerAvatarImage = new JLabel("");
		lblPlayerAvatarImage.setBorder(new EmptyBorder(25, 0, 0, 0));
		lblPlayerAvatarImage.setHorizontalAlignment(SwingConstants.CENTER);
		avatarPanel.add(lblPlayerAvatarImage);
		lblPlayerAvatarImage.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/default_avatar_126x126.png")));
		
		JLabel lblPlayerAvatar = new JLabel("Player Avatar");
		lblPlayerAvatar.setForeground(new Color(255, 255, 240));
		avatarPanel.add(lblPlayerAvatar);
		lblPlayerAvatar.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblPlayerAvatar.setBorder(new EmptyBorder(0, 0, 50, 0));
		lblPlayerAvatar.setHorizontalAlignment(SwingConstants.CENTER);
	}

	private JPanel createPlayerInfoPanel() {
		playerInfoPanel_1 = new JPanel();
		playerInfoPanel_1.setBackground(new Color(119, 136, 153));
		playerInfoPanel_1.setBorder(new EmptyBorder(100, 10, 0, 10));
		playerInfoPanel_1.setLayout(new GridLayout(3, 0, 0, 10));
		return playerInfoPanel_1;
	}

	private JLabel createGameTitleLabel() {
		JLabel lblGameTitle = new JLabel("Blackjack");
		lblGameTitle.setBorder(new EmptyBorder(5, 0, 25, 0));
		lblGameTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 48));
		return lblGameTitle;
	}

}
