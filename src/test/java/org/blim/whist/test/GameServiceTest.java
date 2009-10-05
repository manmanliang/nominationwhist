package org.blim.whist.test;

import java.util.Collections;
import java.util.List;

import org.blim.whist.GameService;
import org.blim.whist.Card;
import org.blim.whist.Trick;
import org.blim.whist.Hand;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import junit.framework.TestCase;

public class GameServiceTest extends TestCase {

	private GameService svcGame = new GameService();

	public void testDealHandOfThirteenCards() throws Exception {
		Hand one = new Hand();
		Hand two = new Hand();
		Hand three = new Hand();
		Hand four = new Hand();
		List<Card> deck = Card.createDeck();

		svcGame.dealRound(deck, 13, one, two, three, four);

		assertTrue("Players do not have 13 cards each [" + one.getCards().size() + ", " + two.getCards().size() + ", " + three.getCards().size() + ", " + four.getCards().size() + "].", one.getCards().size() == 13 && two.getCards().size() == 13 && three.getCards().size() == 13 && four.getCards().size() == 13);
	}

	public void testPlayCard() throws Exception {
		List<Card> deck = Card.createDeck();
		Hand player = new Hand();
		Trick trick = new Trick();

		Collections.shuffle(deck);

		player.getCards().addAll(deck.subList(3, 11));
		deck.removeAll(player.getCards());
		Card cardToPlay = player.getCards().get(3);
		
		svcGame.playCard(player.getCards(), cardToPlay, trick);

		assertFalse("Card was not removed from players hand", player.getCards().contains(cardToPlay));
		assertTrue("Card was not added to the trick", trick.getCards().contains(cardToPlay));
	}

}
