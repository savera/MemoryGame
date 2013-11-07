package org.wintrisstech.ss.memory;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.wintrisstech.cards.Card;

public class CardButton extends JButton {
	private ImageIcon faceImage;
	private ImageIcon backImage;
	private Card card;
	private boolean faceUp;

	public CardButton(Card card) {
		this.card = card;
		this.faceImage = new ImageIcon(card.getFaceImage());
		this.backImage = new ImageIcon(card.getBackImage());
		this.setIcon(backImage);
		this.faceUp = false;

	}

	public void flip() {
		faceUp = !faceUp;
		if (faceUp) {
			setIcon(faceImage);
		} else {
			setIcon(backImage);
		}
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
		this.faceImage = new ImageIcon(card.getFaceImage());
	}

	public boolean isFaceUp() {
		return faceUp;
	}

	
}
