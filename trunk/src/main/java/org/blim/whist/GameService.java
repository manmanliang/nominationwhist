package org.blim.whist;

import java.util.Collections;
import java.util.List;

public class GameService {

	public void dealRound(List<Card> deck, int numOfCards,Hand one, Hand two, Hand three, Hand four) {
		Collections.shuffle(deck);
		
		for (int i = 0; i < numOfCards; i++) {
			one.addCard(deck.remove(0));
			two.addCard(deck.remove(0));
			three.addCard(deck.remove(0));
			four.addCard(deck.remove(0));
		}
	}
	
	public void playCard(List<Card> player, Card cardToPlay, Trick trick) {
		trick.addCard(cardToPlay);
		player.remove(cardToPlay);
	}
		
}
