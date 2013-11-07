package org.wintrisstech.ss.memory;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.wintrisstech.cards.Card;
import org.wintrisstech.cards.Deck;

@SuppressWarnings("serial")
/**
 * The Memory Game is a game where cards are laid out facing down in a grid. For
 * each card, there is a matching card with the same suit and number. The user
 * clicks on one card then on another card. After each click, the selected card
 * is flipped, thus showing the card's face. If the two cards match, they remain
 * facing up. If they do not match, then, upon the user's next click or after
 * one second, whichever occurs first, the two cards are flipped so they face
 * down again. The game continues until all cards are facing up.
 * 
 * @author Savera Soin &copy; 2013
 */
public class MemoryGame extends JPanel implements Runnable, ActionListener {
	/**
	 * Constants
	 */
	private static final int NUM_ROWS = 2;
	private static final int NUM_COLUMMS = 2;
	private static final int NUM_BUTTONS = NUM_ROWS * NUM_COLUMMS;
	private final static int MILLI_TIMER = 1000;

	/**
	 * Variables associated with the state of the game
	 */
	private Timer cardTimer;
	private int state = 0;
	private CardButton first = null;
	private CardButton second = null;
	private int misses = 0;
	private int matches = 0;
	/**
	 * Resources / helpers
	 */
	private Deck deck;
	private CardButton[] buttons;
	private static final Random RAND = new Random();
	/**
	 * Starts the game
	 * 
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new MemoryGame());

	}
	/**
	 * Initializes the MemoryGame. Generates a list of CardButtons and places
     * them on a grid inside a JFrame.
	 */
	@Override
	public void run() {
		cardTimer = new Timer(MILLI_TIMER, this);
		cardTimer.setRepeats(false);
		// TODO Auto-generated method stub

		JFrame frame = new JFrame("Memory Game");
		frame.add(this);
		// this.setPreferredSize(new Dimension(400, 300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		// Deck deck = new Deck(Color.BLUE);
		this.setLayout(new GridLayout(NUM_ROWS, NUM_COLUMMS));
		buttons = new CardButton[NUM_BUTTONS];
		deck = new Deck(Color.BLUE);
		for (int i = 0; i < NUM_BUTTONS; i++) {
			buttons[i] = new CardButton(deck.getCard());
			this.add(buttons[i]);
			buttons[i].addActionListener(this);
		}
		selectCards();
		frame.pack();
		frame.setVisible(true);

	}
	 /**
     * Produces an array containing a random selection of cards where each card
     * occurs exactly two times in the array. Cards occur in random order.
     * <p>
     * The "inside-out" version of the Fisher-Yates shuffle algorithm, which
     * simultaneously initializes and shuffles the array, is used.
     * 
     * @return the Card array
     */

	private void selectCards() {
		Card[] cards = new Card[NUM_BUTTONS];
		deck.shuffle();
		for (int i = 0; i < cards.length; i++) {
			if (i % 2 == 0) {
				cards[i] = deck.getCard();
			} else {
				cards[i] = cards[i - 1];
			}
		}
		shuffle(cards);
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setCard(cards[i]);
		}
	}
	
	/**
     * Handles action events. The state diagram is shown below.
     * <pre>
     *    +-+               +-+
     *c.u.| |           c.u.| |
     *    | v               | v
     *  +-----+           +-----+
     *  |  0  |   c.d.    |  1  |
     *  |     |---------->|     |<---+
     *  +-----+           +-----+    |
     *    ^ ^                |       |
     *    | |                |c.d.   |
     *    | |           Y    |       |
     *c.u.| +-------------[match?]   |c.d.
     *t.e.|                  | N     |
     *    |    +-----+       |       |           
     *    +----|  2  |<------+       |
     *         |     |---------------+
     *         +-----+               
     * </pre>
     * 
     * 
     * @param button the button that was clicked  on.
     */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == cardTimer) {
			if (state == 2) {
				first.flip();
				second.flip();
				state = 0;
			}
		} else {

			CardButton button = (CardButton) arg0.getSource();
			clickHandle(button);

		}

	}
	/**
	 * Handles a click on one of the card buttons.
	 * @param button the one that was clicked on.
	 */
	private void clickHandle(CardButton button) {
		// button.flip();
		// button.repaint();
		switch (state) {
		case 0:
			if (button.isFaceUp()) {

			} else {
				button.flip();
				first = button;
				state = 1;
			}
			break;
		case 1:
			if (button.isFaceUp()) {

			} else {
				second = button;
				// button is face down
				second.flip();
				if (first.getCard().equals(second.getCard())) {
					matches++;
					if (endOfGame()) {
						if (playAgain()) {
							resetGame();
						} else {
							System.exit(0);
						}
					}
					state = 0;

				} else {
					cardTimer.start();
					misses++;
					state = 2;
				}
			}
			break;
		case 2:
			cardTimer.stop();
			first.flip();
			second.flip();
			if (button.isFaceUp()) {
				state = 0;
			} else {
				first = button;
				first.flip();
				state = 1;
			}
			break;
		default:
		}
	}
	/**
     * Checks if the end of the game has been reached and, if so, 
     * asks the user if he/she wants to play again. If the user does
     * not want to play again, the program shuts down.
     * 
     * @return true if end of game
     */

	private void resetGame() {

		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].isFaceUp()) {
				buttons[i].flip();
			}
		}
		selectCards();
		misses = 0;
		matches = 0;

	}
	/**
     * Checks if the end of the game has been reached.
     * @return true if end of game
     */
	private boolean endOfGame() {

		return matches == NUM_BUTTONS / 2;

	}
	/**
     * Asks the user if he/she wants to play again. 
     * @return true if the user wants to play again.
     */
	private boolean playAgain() {

		int answer = JOptionPane.showConfirmDialog(this, "You had "
				+ (misses == 1 ? "miss" : "misses")
				+ ".\nDo you want to play again?", "End of Game",
				JOptionPane.YES_NO_OPTION);
		return answer == JOptionPane.YES_OPTION;

	}


	private void shuffle(Card[] cards) {
		for (int i = cards.length - 1; i > 0; i--) {
			int pos = RAND.nextInt(i + 1);
			Card store = cards[pos];
			cards[pos] = cards[i];
			cards[i] = store;
		}

	}
}
