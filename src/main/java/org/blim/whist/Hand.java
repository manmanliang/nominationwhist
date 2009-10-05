package org.blim.whist;

import java.util.List;

import com.google.common.collect.Lists;

public class Hand {

	private List<Card> cards = Lists.newArrayList();
	
	public List<Card> getCards() {
		return cards;
	}
	
	public void addCard(Card card) {
		cards.add(card);
	}
	
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	
	public void clearCards() {
		cards.clear();
	}
	
}
