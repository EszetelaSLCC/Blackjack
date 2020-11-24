package blackjack;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class BlackjackGui extends JFrame {

	private JPanel contentPane;
	private JLabel lblIndicatorText;
	private JButton btnStand;
	private JButton btnHit;
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
	private JButton btnDeal;
	private static Deck cardDeck = new Deck();
	
	// Declare game variables, new for each hand played
	private static ArrayList<Card> playerInitialHand = new ArrayList<>();
	private static ArrayList<Card> dealerInitialHand = new ArrayList<>();
	private Hand playerHand;
	private DealerHand dealerHand;
	private Match gameMatch;

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
		
//		Match gameMatch = new Match();
//		GameState state;  //might be able to pull this out as we are initiating a match with each click of Deal and Match has a gameState field that is updated and there is a getGameState method.
		
		// Deal initial hands
		dealHand(cardDeck, playerInitialHand);
		dealHand(cardDeck, dealerInitialHand);
		
	}

	/**
	 * Create the frame.
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

		JPanel gameTablePanel = createGamePanelComponents();
		contentPane.add(gameTablePanel, BorderLayout.CENTER);
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
	
	private JButton createStandButton() {
		JButton btnStand = new JButton("STAND");
		btnStand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// show dealer cards
				// reveal dealer score
				// dealer hit loop based on score (see: BlackJackTest:95)
				// if dealer is hitting, update indicator text to "	Dealer is hitting..."
				// sleeps between dealer actions
				
					// player standing, show dealer cards and score.  Dealer hits according to rules.
					System.out.println("dealerHandScore: " + dealerHand.getHandScore());
					dealerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
					updateDealerHandGui();
					while (dealerHand.dealerCanHit()) { //dealerHand.getHandScore() < 18
						// Dealer needs to hit, start dealer hit loop until 18 or over
						// update indicator text here
						System.out.println("Dealer hitting...");
						Card dealerCard = cardDeck.drawCard();
						dealerHand.addCard(dealerCard);
						updateDealerHandGui();
						System.out.println("dealerHandScore: " + dealerHand.getHandScore());
						dealerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
					}
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
		btnStand.setFont(new Font("Tahoma", Font.PLAIN, 18));
		return btnStand;
	}

	private JButton createDealButton() {
		JButton btnDeal = new JButton("DEAL");
		btnDeal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblIndicatorText.setText("Game Begins!");
				btnDeal.setEnabled(false);
				btnDeal.setVisible(false);
				btnStand.setEnabled(true);
				btnStand.setVisible(true);
				btnHit.setEnabled(true);
				btnHit.setVisible(true);
				
				startMatch();
				
			}
		});
		btnDeal.setFont(new Font("Tahoma", Font.PLAIN, 18));
		return btnDeal;
	}

	protected void startMatch() {
//		Deck cardDeck = new Deck();
//		
//		// Declare game variables, new for each hand played
//		ArrayList<Card> playerInitialHand = new ArrayList<>();
//		ArrayList<Card> dealerInitialHand = new ArrayList<>();

		gameMatch = new Match();
		
		
//		// Deal initial hands
//		dealHand(cardDeck, playerInitialHand);
//		dealHand(cardDeck, dealerInitialHand);
		
		playerHand = new PlayerHand(0, playerInitialHand);
		dealerHand = new DealerHand(0, dealerInitialHand);
		
		System.out.println(dealerHand);
		System.out.println(playerHand);
		
		System.out.println();
		
		
		// Check game state on deal for blackjacks
		gameMatch.update(dealerHand, playerHand, false);
		
		lblDealerCardFirst.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + dealerHand.getCardFromHandByIndex(0).getImagePath())));
		lblDealerCardSecond.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/blue_back.png")));
		lblPlayerCardFirst.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + playerHand.getCardFromHandByIndex(0).getImagePath())));
		lblPlayerCardSecond.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + playerHand.getCardFromHandByIndex(1).getImagePath())));
		
		
		// If blackjack, show all cards as game is over
		if (gameMatch.getGamestate() == GameState.LOSE_DEALER_BLACKJACK || gameMatch.getGamestate() == GameState.PUSH_BLACKJACK 
				|| gameMatch.getGamestate() == GameState.WIN_PLAYER_BLACKJACK) {
			lblDealerCardSecond.setIcon(new ImageIcon(BlackjackGui.class.getResource("/blackjack/resources/" + dealerHand.getCardFromHandByIndex(1).getImagePath())));
			
			//System.out.println("playerHandScore: " + playerHand.getHandScore());
			playerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
	
			//System.out.println("dealerHandScore: " + dealerHand.getHandScore());
			dealerHand.getCardsInHand().forEach(c -> System.out.println(c.getFace() + c.getSuit()));
			
			// TODO make method to take state as param and based on state update indicator text for the blackjack(s)
			// TODO make a method to reset card labels and button states and update bank appropriately
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
		System.out.println(gameMatch.getGamestate());

		System.out.println();
	}
	
	private static void dealHand(Deck cardDeck, ArrayList<Card> hand) {
		for (int i = 0; i < 2; i++) {
			hand.add(cardDeck.drawCard());
		}
	}

	private JButton createHitButton() {
		JButton btnHit = new JButton("HIT");
		btnHit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Card cardToAdd = cardDeck.drawCard();
				playerHand.addCard(cardToAdd);
				gameMatch.update(dealerHand, playerHand, false);
				updateGuiGameState();
				updatePlayerHandGui();
			}
		});
		btnHit.setVisible(false);
		btnHit.setEnabled(false);
		btnHit.setFont(new Font("Tahoma", Font.PLAIN, 18));
		return btnHit;
	}

	protected void updatePlayerHandGui() {
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

	protected void updateGuiGameState() {
		switch (gameMatch.getGamestate()) {
			case LOSE_DEALER_BLACKJACK:
				lblIndicatorText.setText("Dealer has BLACKJACK! You lose!");
				btnDeal.setEnabled(true);
				btnDeal.setVisible(true);
				btnStand.setEnabled(false);
				btnStand.setVisible(false);
				btnHit.setEnabled(false);
				btnHit.setVisible(false);
				//TODO update bank
				//TODO reset hands and deck (cardDeck.shuffle() I believe)
				//TODO set card icons to null
				//TODO set indicator text to "Click DEAL to play"
				//TODO reset displayed hand scores
				break;
			case PUSH_BLACKJACK:
				lblIndicatorText.setText("You and Dealer have BLACKJACK! It's a push!");
				btnDeal.setEnabled(true);
				btnDeal.setVisible(true);
				btnStand.setEnabled(false);
				btnStand.setVisible(false);
				btnHit.setEnabled(false);
				btnHit.setVisible(false);
				break;
			case PUSH:
				lblIndicatorText.setText("It's a push");
				btnDeal.setEnabled(true);
				btnDeal.setVisible(true);
				btnStand.setEnabled(false);
				btnStand.setVisible(false);
				btnHit.setEnabled(false);
				btnHit.setVisible(false);
				break;
			case WIN_PLAYER_BLACKJACK:
				lblIndicatorText.setText("You have BLACKJACK! You win!");
				btnDeal.setEnabled(true);
				btnDeal.setVisible(true);
				btnStand.setEnabled(false);
				btnStand.setVisible(false);
				btnHit.setEnabled(false);
				btnHit.setVisible(false);
				break;
			case LOSE_PLAYER_BUSTS:
				lblIndicatorText.setText("You busted! You lose!");
				btnDeal.setEnabled(true);
				btnDeal.setVisible(true);
				btnStand.setEnabled(false);
				btnStand.setVisible(false);
				btnHit.setEnabled(false);
				btnHit.setVisible(false);
				break;
			case LOSE_PLAYER_LOWER_SCORE:
				lblIndicatorText.setText("Dealer wins! You lose!");
				btnDeal.setEnabled(true);
				btnDeal.setVisible(true);
				btnStand.setEnabled(false);
				btnStand.setVisible(false);
				btnHit.setEnabled(false);
				btnHit.setVisible(false);
				break;
			case WIN_DEALER_BUSTS:
				lblIndicatorText.setText("Dealer busts! You win!");
				btnDeal.setEnabled(true);
				btnDeal.setVisible(true);
				btnStand.setEnabled(false);
				btnStand.setVisible(false);
				btnHit.setEnabled(false);
				btnHit.setVisible(false);
				break;
			case WIN_DEALER_LOWER_SCORE:
				lblIndicatorText.setText("You win!");
				btnDeal.setEnabled(true);
				btnDeal.setVisible(true);
				btnStand.setEnabled(false);
				btnStand.setVisible(false);
				btnHit.setEnabled(false);
				btnHit.setVisible(false);
				break;
			case PLAYING:
				lblIndicatorText.setText("Hit or Stand?");
				break;
		}
		
	}

	private JPanel createGameButtonPanel() {
		JPanel gameButtonPanel = new JPanel();
		gameButtonPanel.setBackground(new Color(0, 128, 0));
		gameButtonPanel.setBorder(new EmptyBorder(60, 100, 60, 100));
		gameButtonPanel.setLayout(new GridLayout(1, 0, 150, 0));
		return gameButtonPanel;
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
		playerCardsPanel.setLayout(new GridLayout(1, 0, 0, 0));
		return playerCardsPanel;
	}

	private JLabel createLabelPlayerPanelTitle() {
		JLabel lblPlayerPanelTitle = new JLabel("Player Cards");
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
		dealerCardsPanel.setLayout(new GridLayout(1, 0, 0, 0));
		return dealerCardsPanel;
	}

	private JLabel createDealerPanelTitle() {
		JLabel lblDealerPanelTitle = new JLabel("Dealer Cards");
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
		
		JLabel lblCurrentBankCount = new JLabel("10");
		lblCurrentBankCount.setForeground(new Color(255, 255, 240));
		bankPanel.add(lblCurrentBankCount);
		lblCurrentBankCount.setBorder(new EmptyBorder(0, 0, 0, 0));
		lblCurrentBankCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrentBankCount.setFont(new Font("Tahoma", Font.PLAIN, 48));
	}

	private void createAvatarPanel(JPanel playerInfoPanel) {
		JPanel avatarPanel = new JPanel();
		avatarPanel.setBackground(new Color(119, 136, 153));
		avatarPanel.setBorder(new EmptyBorder(100, 0, 0, 0));
		playerInfoPanel.add(avatarPanel);
		avatarPanel.setLayout(new GridLayout(2, 0, 0, 2));
		
		JLabel lblPlayerAvatarImage = new JLabel("");
		lblPlayerAvatarImage.setBorder(new EmptyBorder(50, 0, 0, 0));
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
		JPanel playerInfoPanel = new JPanel();
		playerInfoPanel.setBackground(new Color(119, 136, 153));
		playerInfoPanel.setBorder(new EmptyBorder(100, 10, 0, 10));
		playerInfoPanel.setLayout(new GridLayout(2, 0, 0, 10));
		return playerInfoPanel;
	}

	private JLabel createGameTitleLabel() {
		JLabel lblGameTitle = new JLabel("Blackjack");
		lblGameTitle.setBorder(new EmptyBorder(5, 0, 25, 0));
		lblGameTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 48));
		return lblGameTitle;
	}

}
