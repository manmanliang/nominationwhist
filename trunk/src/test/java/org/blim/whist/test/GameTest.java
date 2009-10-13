package org.blim.whist.test;

import java.util.List;

import junit.framework.TestCase;

import org.blim.whist.Card;
import org.blim.whist.Game;
import org.blim.whist.Hand;
import org.blim.whist.Round;

import com.google.common.collect.Iterables;

public class GameTest extends TestCase {

	public void testDealHandOfThirteenCards() throws Exception {
		Game game = new Game();
		game.getPlayers().add("Beer");
		game.getPlayers().add("Tea");
		game.getPlayers().add("Coffee");
		game.getPlayers().add("Wine");
		game.setRoundSequence(new int[] {13});
		game.addRound();

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
		game.setRoundSequence(new int[] {13});
		Round round = game.addRound();
		game.addTrick();
		Iterables.getLast(round.getTricks()).setFirstPlayer(0);
		Hand hand = round.getHands().get(0);

		Card card = hand.getCards().get(3);
		
		round.playCard(0, card);

		assertFalse("Card was not removed from players hand", hand.getCards().contains(card));
		assertTrue("Card was not added to the trick", Iterables.getLast(round.getTricks()).getCards().contains(card));

	}

	/*
 		// Set up data manually for now
		game.getPlayers().add("Rob");
		game.getPlayers().add("Lee");
		game.getPlayers().add("Mum");
		game.getPlayers().add("Dad");
		
		game.setRoundSequence(new int[] {13});
	    
		gameService.addRound(game);
	 */
	
	/*
	    game.getPlayers().add("Rob");
		game.getPlayers().add("Lee");
		game.getPlayers().add("Mum");
		game.getPlayers().add("Dad");

		game.setRoundSequence(new int[] {13, 12});

		Round round = gameService.addRound(game);
		round.addTrick();
		
		// Set up data manually for now
	    gameService.playCard(round, 0, round.getHands().get(0).getCards().get(4));
	    gameService.playCard(round, 1, round.getHands().get(1).getCards().get(2));
	    gameService.playCard(round, 2, round.getHands().get(2).getCards().get(7));

	 */
}
