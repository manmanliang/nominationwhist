package org.blim.whist.test;

import java.util.List;

import junit.framework.TestCase;

import org.blim.whist.Card;
import org.blim.whist.Game;
import org.blim.whist.GameService;
import org.blim.whist.Hand;
import org.blim.whist.Round;
import org.blim.whist.Trick;

import com.google.common.collect.Iterables;

public class GameServiceTest extends TestCase {

	private GameService gameService = new GameService();

	public void testDealHandOfThirteenCards() throws Exception {
		Game game = new Game();
		game.getPlayers().add("Beer");
		game.getPlayers().add("Tea");
		game.getPlayers().add("Coffee");
		game.getPlayers().add("Wine");
		gameService.createRound(game);

		List<Hand> hands = Iterables.getLast(game.getRounds()).getHands();
		
		assertTrue("Players do not have 13 cards each [" + hands.get(0).getCards().size() + ", " 
														 + hands.get(1).getCards().size() + ", " 
														 + hands.get(2).getCards().size() + ", " 
														 + hands.get(3).getCards().size() + "].", 
				   hands.get(0).getCards().size() == 13 && 
				   hands.get(1).getCards().size() == 13 && 
				   hands.get(2).getCards().size() == 13 && 
				   hands.get(3).getCards().size() == 13);

	}

	public void testPlayCard() throws Exception {
		Game game = new Game();
		game.getPlayers().add("Wibble");
		Round round = gameService.createRound(game);
		round.getTrickHistory().add(new Trick(0));
		Hand hand = round.getHands().get(0);

		Card card = hand.getCards().get(3);
		
		gameService.playCard(round, 0, card);

		assertFalse("Card was not removed from players hand", hand.getCards().contains(card));
		assertTrue("Card was not added to the trick", Iterables.getLast(round.getTrickHistory()).getCards().contains(card));

	}

}
