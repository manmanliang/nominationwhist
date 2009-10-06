package org.blim.whist;

import java.util.Comparator;

public class OrderComparator implements Comparator<Card> {
	public int compare(Card card1, Card card2) {
		if(card1.suit() == card2.suit()) {
			return card1.value().compareTo(card2.value());
		}
		else {
			return card1.suit().compareTo(card2.suit());
		}
	}
}
