package org.blim.whist.test;

import java.util.Collections;
import java.util.List;

import org.blim.whist.Game;
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
		int roundsNum[] = {13};
		Game game = new Game(roundsNum, 4);
		List<Card> deck = Card.createDeck();

		svcGame.dealRound(deck, game.getCurrentRound());

		assertTrue("Players do not have 13 cards each [" + game.getCurrentRound().getHand(0).getCards().size() + ", " 
														 + game.getCurrentRound().getHand(1).getCards().size() + ", " 
														 + game.getCurrentRound().getHand(2).getCards().size() + ", " 
														 + game.getCurrentRound().getHand(3).getCards().size() + "].", 
				   game.getCurrentRound().getHand(0).getCards().size() == 13 && 
				   game.getCurrentRound().getHand(1).getCards().size() == 13 && 
				   game.getCurrentRound().getHand(2).getCards().size() == 13 && 
				   game.getCurrentRound().getHand(3).getCards().size() == 13);
	}

	public void testPlayCard() throws Exception {
		List<Card> deck = Card.createDeck();
		String player = new String("rob");
		Hand hand = new Hand();
		Trick trick = new Trick();

		Collections.shuffle(deck);

		hand.getCards().addAll(deck.subList(3, 11));
		deck.removeAll(hand.getCards());
		Card cardToPlay = hand.getCards().get(3);
		
		svcGame.playCard(player, hand.getCards(), cardToPlay, trick);

		assertFalse("Card was not removed from players hand", hand.getCards().contains(cardToPlay));
		assertTrue("Card was not added to the trick", trick.getCards().containsKey(player));
	}

}
