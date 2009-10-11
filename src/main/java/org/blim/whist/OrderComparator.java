package org.blim.whist;

import java.util.Comparator;

public class OrderComparator implements Comparator<Card> {
	public int compare(Card card1, Card card2) {
		if(card1.getSuit() == card2.getSuit()) {
			return card1.getValue().compareTo(card2.getValue());
		}
		else {
			return card1.getSuit().compareTo(card2.getSuit());
		}
	}
}
