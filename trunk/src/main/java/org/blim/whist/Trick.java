package org.blim.whist;

import java.util.List;
import com.google.common.collect.Lists;

public class Trick {
	private List<Card> trickCards = Lists.newArrayList();
	
	public List<Card> getCards() { return trickCards; }
	
	public void addCard(Card card) {
		trickCards.add(card);
	}
}
