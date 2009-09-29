package org.blim.whist;

import java.util.Collections;
import java.util.List;

public class GameService {

	public void dealRound(int numOfCards, List<Card> one, List<Card> two, List<Card> three, List<Card> four) {
		List<Card> deck = Card.createDeck();
		Collections.shuffle(deck);
		
		for (int i = 0; i < numOfCards; i++) {
			one.add(deck.remove(0));
			two.add(deck.remove(0));
			three.add(deck.remove(0));
			four.add(deck.remove(0));
		}
	}
		
}
