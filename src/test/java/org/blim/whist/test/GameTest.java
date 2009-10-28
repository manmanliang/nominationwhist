package org.blim.whist.test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import junit.framework.TestCase;

import org.blim.whist.Card;
import org.blim.whist.Game;
import org.blim.whist.Hand;
import org.blim.whist.Round;
import org.blim.whist.Trick;
import org.blim.whist.WhistException;

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

	public void testBid() throws Exception {
		Game game = new Game();
		int exceptionThrown = 0;

		game.getPlayers().add("Wibble");
		game.getPlayers().add("Grelp");
		game.getPlayers().add("Flurp");
		game.setRoundSequence(new int[] {13, 13, 13, 13});

		Round round = game.addRound();

		try {
			game.bid(1, 2);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception not thrown when bidding out of turn", exceptionThrown == 1);
		exceptionThrown = 0;

		try {
			game.bid(0, 14);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception not thrown when bidding more than the number of cards", exceptionThrown == 1);
		exceptionThrown = 0;

		try {
			game.bid(0, 0);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception thrown when player 0 was validly bidding", exceptionThrown == 0);
		exceptionThrown = 0;

		try {
			game.bid(1, 5);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception thrown when player 1 was validly bidding", exceptionThrown == 0);
		exceptionThrown = 0;

		try {
			game.bid(2, 8);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception not thrown when last player bids such that total bids equals the number of cards", exceptionThrown == 1);
		exceptionThrown = 0;

		try {
			game.bid(2, 5);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception thrown when player 2 was validly bidding", exceptionThrown == 0);
		exceptionThrown = 0;

		round = game.addRound();
		game.bid(1, 2);
		game.bid(2, 2);
		game.bid(0, 0);

		round = game.addRound();
		game.bid(2, 2);
		game.bid(0, 0);
		game.bid(1, 2);

		round = game.addRound();
		try {
			game.bid(0, 0);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception not thrown when player tried to bid 0 four times in a row", exceptionThrown == 1);

	}

	public void testSelectTrumps() throws Exception {
		Game game = new Game();
		int exceptionThrown = 0;

		game.getPlayers().add("Wibble");
		game.getPlayers().add("Grelp");
		game.getPlayers().add("Flurp");
		game.setRoundSequence(new int[] {13});

		Round round = game.addRound();
		game.bid(0, 2);
		game.bid(1, 1);
		game.bid(2, 6);

		try {
			round.selectTrumps(1, Card.Suit.CLUBS);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception not thrown when choosing trumps when didn't bid highest", exceptionThrown == 1);
		exceptionThrown = 0;

		try {
			round.selectTrumps(2, Card.Suit.CLUBS);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception thrown when validly choosing trumps", exceptionThrown == 0);
		exceptionThrown = 0;

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
	
	public void testPlayerToPlay() throws Exception {
		Game game = new Game();
		int exceptionThrown = 0;

		game.getPlayers().add("Wibble");
		game.getPlayers().add("Grelp");
		game.getPlayers().add("Flurp");
		game.setRoundSequence(new int[] {13});

		Round round = game.addRound();
		game.bid(0, 2);
		game.bid(1, 1);
		game.bid(2, 6);
		round.selectTrumps(2, Card.Suit.CLUBS);

		List<Card> deck = new ArrayList<Card>(EnumSet.allOf(Card.class));
		round.getHands().get(0).getCards().clear();
		round.getHands().get(1).getCards().clear();
		round.getHands().get(2).getCards().clear();
		round.getHands().get(0).getCards().addAll(deck.subList(14, 27));
		round.getHands().get(1).getCards().addAll(deck.subList(27, 40));
		round.getHands().get(2).getCards().addAll(deck.subList(0, 13));

		game.addTrick();

		assertTrue("Player 0 is not playerTo, playerToPlay is " + game.activePlayer(), game.activePlayer() == 0);
		
		Card card = round.getHands().get(1).getCards().get(3);		
		try {
			game.playCard(1, card);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception not thrown when playing out of turn", exceptionThrown == 1);
		exceptionThrown = 0;

		try {
			game.playCard(0, card);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception not thrown when playing someone elses card", exceptionThrown == 1);
		exceptionThrown = 0;

		card = round.getHands().get(0).getCards().get(12);		
		try {
			game.playCard(0, card);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception thrown when player 0 played a valid card", exceptionThrown == 0);
		exceptionThrown = 0;

		assertTrue("Player 1 is not playerTo, playerToPlay is " + game.activePlayer(), game.activePlayer() == 1);

		card = round.getHands().get(1).getCards().get(12);
		try {
			game.playCard(1, card);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception not thrown when playing a card but not following suit", exceptionThrown == 1);
		exceptionThrown = 0;

		card = round.getHands().get(1).getCards().get(3);
		try {
			game.playCard(1, card);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception thrown when player 1 played a valid card", exceptionThrown == 0);

		assertTrue("Player 2 is not playerToPlay, playerToPlay is " + game.activePlayer(), game.activePlayer() == 2);

	}

	public void testGameFinished() throws Exception {
		Game game = new Game();
		game.getPlayers().add("Wibble");
		game.getPlayers().add("Grelp");
		game.setRoundSequence(new int[] {2, 2});
		Round round = game.addRound();
		
		game.bid(0, 2);
		game.bid(1, 1);
		round.selectTrumps(0, Card.Suit.CLUBS);

		List<Card> deck = new ArrayList<Card>(EnumSet.allOf(Card.class));
		round.getHands().get(0).getCards().clear();
		round.getHands().get(1).getCards().clear();
		round.getHands().get(0).getCards().addAll(deck.subList(14, 16));
		round.getHands().get(1).getCards().addAll(deck.subList(27, 29));

		game.addTrick();

		// First Round
		Hand hand = round.getHands().get(0);
		Card card = hand.getCards().get(1);
		game.playCard(0, card);
		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(1, card);

		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(1, card);
		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(0, card);

		assertFalse("Game should not be finished", game.isFinished());

		// Second Round
		
		round = game.getCurrentRound();
		game.bid(1, 2);
		game.bid(0, 1);
		round.selectTrumps(1, Card.Suit.CLUBS);
	
		deck = new ArrayList<Card>(EnumSet.allOf(Card.class));
		round.getHands().get(0).getCards().clear();
		round.getHands().get(1).getCards().clear();
		round.getHands().get(0).getCards().addAll(deck.subList(14, 16));
		round.getHands().get(1).getCards().addAll(deck.subList(27, 29));

		hand = round.getHands().get(1);
		card = hand.getCards().get(1);
		game.playCard(1, card);
		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(0, card);
		
		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(1, card);
		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(0, card);

		assertTrue("Game should be finished", game.isFinished());

	}
	
	public void testGameScores() throws Exception {
		Game game = new Game();
		game.getPlayers().add("Wibble");
		game.getPlayers().add("Grelp");
		game.setRoundSequence(new int[] {2, 2});
		Round round = game.addRound();
		
		game.bid(0, 2);
		game.bid(1, 1);
		round.selectTrumps(0, Card.Suit.CLUBS);

		List<Card> deck = new ArrayList<Card>(EnumSet.allOf(Card.class));
		round.getHands().get(0).getCards().clear();
		round.getHands().get(1).getCards().clear();
		round.getHands().get(0).getCards().addAll(deck.subList(27, 29));
		round.getHands().get(1).getCards().addAll(deck.subList(14, 16));

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
		game.bid(1, 1);
		game.bid(0, 2);
		round.selectTrumps(0, Card.Suit.CLUBS);
	
		deck = new ArrayList<Card>(EnumSet.allOf(Card.class));
		round.getHands().get(0).getCards().clear();
		round.getHands().get(1).getCards().clear();
		round.getHands().get(1).getCards().add(deck.get(27));
		round.getHands().get(0).getCards().addAll(deck.subList(18, 20));
		round.getHands().get(1).getCards().add(deck.get(15));

		hand = round.getHands().get(1);
		card = hand.getCards().get(1);
		game.playCard(1, card);
		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(0, card);
		
		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(0, card);
		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(1, card);

		assertTrue("Scores are wrong should be 13 and 11 but got " + game.scores().get(0) + " and " + game.scores().get(1),
				game.scores().get(0) == 13 && game.scores().get(1) == 11);

	}

}
