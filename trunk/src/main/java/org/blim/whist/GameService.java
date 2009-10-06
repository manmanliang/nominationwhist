package org.blim.whist;

import java.util.Collections;
import java.util.List;

public class GameService {

	public void dealRound(List<Card> deck, Round round) {
		Collections.shuffle(deck);
		
		List<Hand> hands = round.getHands();
		int numberOfCards = round.getNumberOfCards();
		
		for (int i = 0; i < numberOfCards; i++) {
			for (Hand hand : hands) {
				hand.addCard(deck.remove(0));
			}
		}
	}
	
	public void playCard(String name, List<Card> hand, Card cardToPlay, Trick trick) {
		trick.addCard(name, cardToPlay);
		hand.remove(cardToPlay);
	}
		
}
