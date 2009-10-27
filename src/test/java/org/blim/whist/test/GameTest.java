package org.blim.whist.test;

import java.util.List;

import junit.framework.TestCase;

import org.blim.whist.Card;
import org.blim.whist.Game;
import org.blim.whist.Hand;
import org.blim.whist.Round;
import org.blim.whist.Trick;

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
		Trick trick = game.addTrick();

		Hand hand = round.getHands().get(0);
		Card card = hand.getCards().get(3);
		
		game.playCard(0, card);

		assertFalse("Card was not removed from players hand", hand.getCards().contains(card));
		assertTrue("Card was not added to the trick", trick.getCards().contains(card));

	}
	/*
	public void testPlayerToPlay() throws Exception {
		Game game = new Game();
		game.getPlayers().add("Wibble");
		game.getPlayers().add("Grelp");
		game.getPlayers().add("Flurp");
		game.setRoundSequence(new int[] {13});

		Round round = game.addRound();
		round.bid(0, 2);
		round.bid(1, 1);
		round.bid(2, 6);
		round.setTrumps(Card.Suit.CLUBS);

		game.addTrick();

		assertTrue("Player 0 is not playerTo, playerToPlay is " + game.activePlayer(), game.activePlayer() == 0);
		
		Hand hand = round.getHands().get(0);
		Card card = hand.getCards().get(3);
		game.playCard(0, card);

		assertTrue("Player 1 is not playerTo, playerToPlay is " + game.activePlayer(), game.activePlayer() == 1);

		hand = round.getHands().get(1);
		card = hand.getCards().get(8);
		game.playCard(1, card);

		assertTrue("Player 2 is not playerToPlay, playerToPlay is " + game.activePlayer(), game.activePlayer() == 2);

	}

	public void testGameFinished() throws Exception {
		Game game = new Game();
		game.getPlayers().add("Wibble");
		game.getPlayers().add("Grelp");
		game.setRoundSequence(new int[] {2, 2});
		Round round = game.addRound();
		game.addTrick();

		// First Round
		Hand hand = round.getHands().get(0);
		Card card = hand.getCards().get(1);
		game.playCard(0, card);
		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(1, card);

		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(0, card);
		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(1, card);

		assertFalse("Game should not be finished", game.isFinished());

		// Second Round
		round = game.getCurrentRound();
		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(0, card);
		hand = round.getHands().get(1);
		card = hand.getCards().get(1);
		game.playCard(1, card);
		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(0, card);
		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(1, card);

		assertTrue("Game should be finished", game.isFinished());

	}
	
	public void testGameScores() throws Exception {
		Game game = new Game();
		game.getPlayers().add("Wibble");
		game.getPlayers().add("Grelp");
		game.setRoundSequence(new int[] {2, 2});
		
		Round round = game.addRound();
		round.setTrumps(Card.Suit.CLUBS);
		round.bid(0, 2);
		round.bid(1, 1);
		
		List<Card> deck = new ArrayList<Card>(EnumSet.allOf(Card.class));
		round.getHands().get(0).getCards().clear();
		round.getHands().get(1).getCards().clear();
		round.getHands().get(0).getCards().addAll(deck.subList(47, 49));
		round.getHands().get(1).getCards().addAll(deck.subList(45, 47));
		game.addTrick();

		// First Round
		Hand hand = round.getHands().get(0);
		Card card = hand.getCards().get(1);
		game.playCard(0, card);
		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(1, card);

		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(0, card);
		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(1, card);

		assertTrue("Scores are wrong should be 12 and 0 but got " + game.scores().get(0) + " and " + game.scores().get(1),
					game.scores().get(0) == 12 && game.scores().get(1) == 0);

		// Second Round
		round = game.getCurrentRound();
		round.setTrumps(Card.Suit.CLUBS);
		round.bid(0, 1);
		round.bid(1, 1);

		round.getHands().get(0).getCards().clear();
		round.getHands().get(1).getCards().clear();
		round.getHands().get(0).getCards().addAll(deck.subList(9, 11));
		round.getHands().get(1).getCards().add(deck.get(7));
		round.getHands().get(1).getCards().add(deck.get(11));
		
		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(0, card);
		hand = round.getHands().get(1);
		card = hand.getCards().get(1);
		game.playCard(1, card);
		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(0, card);
		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(1, card);

		assertTrue("Scores are wrong should be 23 and 11 but got " + game.scores().get(0) + " and " + game.scores().get(1),
				game.scores().get(0) == 23 && game.scores().get(1) == 11);


	}


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
