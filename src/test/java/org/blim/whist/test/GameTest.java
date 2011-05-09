package org.blim.whist.test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import junit.framework.TestCase;

import org.blim.whist.WhistException;
import org.blim.whist.game.Card;
import org.blim.whist.game.Game;
import org.blim.whist.game.Hand;
import org.blim.whist.game.Round;
import org.blim.whist.game.Trick;
import org.blim.whist.player.HumanPlayer;
import org.blim.whist.player.Player;

import com.google.common.collect.Iterables;

public class GameTest extends TestCase {

	public void testDealHandOfThirteenCards() throws Exception {
		Game game = new Game();
		Player playerOne = new HumanPlayer();
		playerOne.setId(1L);
		Player playerTwo = new HumanPlayer();
		playerTwo.setId(2L);
		Player playerThree = new HumanPlayer();
		playerThree.setId(3L);
		Player playerFour = new HumanPlayer();
		playerFour.setId(4L);
		
		game.getPlayers().add(playerOne);
		game.getPlayers().add(playerTwo);
		game.getPlayers().add(playerThree);
		game.getPlayers().add(playerFour);
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
		Player playerOne = new HumanPlayer();
		playerOne.setId(1L);
		Player playerTwo = new HumanPlayer();
		playerTwo.setId(2L);
		Player playerThree = new HumanPlayer();
		playerThree.setId(3L);

		game.getPlayers().add(playerOne);
		game.getPlayers().add(playerTwo);
		game.getPlayers().add(playerThree);
		game.setRoundSequence(new int[] {13, 13, 13, 13});

		game.addRound();

		try {
			game.bid(playerTwo, 2);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception not thrown when bidding out of turn", exceptionThrown == 1);
		exceptionThrown = 0;

		try {
			game.bid(playerOne, 14);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception not thrown when bidding more than the number of cards", exceptionThrown == 1);
		exceptionThrown = 0;

		try {
			game.bid(playerOne, 0);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception thrown when player 0 was validly bidding", exceptionThrown == 0);
		exceptionThrown = 0;

		try {
			game.bid(playerTwo, 5);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception thrown when player 1 was validly bidding", exceptionThrown == 0);
		exceptionThrown = 0;

		try {
			game.bid(playerThree, 8);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception not thrown when last player bids such that total bids equals the number of cards", exceptionThrown == 1);
		exceptionThrown = 0;

		try {
			game.bid(playerThree, 5);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception thrown when player 2 was validly bidding", exceptionThrown == 0);
		exceptionThrown = 0;

		game.addRound();
		game.bid(playerTwo, 2);
		game.bid(playerThree, 2);
		game.bid(playerOne, 0);

		game.addRound();
		game.bid(playerThree, 2);
		game.bid(playerOne, 0);
		game.bid(playerTwo, 2);

		game.addRound();
		try {
			game.bid(playerOne, 0);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception not thrown when player tried to bid 0 four times in a row", exceptionThrown == 1);

	}

	public void testSelectTrumps() throws Exception {
		Game game = new Game();
		int exceptionThrown = 0;
		Player playerOne = new HumanPlayer();
		playerOne.setId(1L);
		Player playerTwo = new HumanPlayer();
		playerTwo.setId(2L);
		Player playerThree = new HumanPlayer();
		playerThree.setId(3L);

		game.getPlayers().add(playerOne);
		game.getPlayers().add(playerTwo);
		game.getPlayers().add(playerThree);

		game.setRoundSequence(new int[] {13});

		Round round = game.addRound();
		game.bid(playerOne, 2);
		game.bid(playerTwo, 1);
		game.bid(playerThree, 6);

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
		Player playerOne = new HumanPlayer();
		playerOne.setId(1L);

		game.getPlayers().add(playerOne);
		game.setRoundSequence(new int[] {13});
		Round round = game.addRound();
		Trick trick = game.addTrick();

		Hand hand = round.getHands().get(0);
		Card card = hand.getCards().get(3);
		
		game.playCard(playerOne, card);

		assertFalse("Card was not removed from players hand", hand.getCards().contains(card));
		assertTrue("Card was not added to the trick", trick.getCards().contains(card));

	}
	
	public void testPlayerToPlay() throws Exception {
		Game game = new Game();
		int exceptionThrown = 0;
		Player playerOne = new HumanPlayer();
		playerOne.setId(1L);
		Player playerTwo = new HumanPlayer();
		playerTwo.setId(2L);
		Player playerThree = new HumanPlayer();
		playerThree.setId(3L);

		game.getPlayers().add(playerOne);
		game.getPlayers().add(playerTwo);
		game.getPlayers().add(playerThree);

		game.setRoundSequence(new int[] {13});

		Round round = game.addRound();
		game.bid(playerOne, 2);
		game.bid(playerTwo, 1);
		game.bid(playerThree, 6);
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
			game.playCard(playerTwo, card);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception not thrown when playing out of turn", exceptionThrown == 1);
		exceptionThrown = 0;

		try {
			game.playCard(playerOne, card);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception not thrown when playing someone elses card", exceptionThrown == 1);
		exceptionThrown = 0;

		card = round.getHands().get(0).getCards().get(12);		
		try {
			game.playCard(playerOne, card);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception thrown when player 0 played a valid card", exceptionThrown == 0);
		exceptionThrown = 0;

		assertTrue("Player 1 is not playerTo, playerToPlay is " + game.activePlayer(), game.activePlayer() == 1);

		card = round.getHands().get(1).getCards().get(12);
		try {
			game.playCard(playerTwo, card);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception not thrown when playing a card but not following suit", exceptionThrown == 1);
		exceptionThrown = 0;

		card = round.getHands().get(1).getCards().get(3);
		try {
			game.playCard(playerTwo, card);
		} catch (WhistException whistException) {
			exceptionThrown = 1;
		}
		assertTrue("Exception thrown when player 1 played a valid card", exceptionThrown == 0);

		assertTrue("Player 2 is not playerToPlay, playerToPlay is " + game.activePlayer(), game.activePlayer() == 2);

	}

	public void testGameFinished() throws Exception {
		Game game = new Game();
		Player playerOne = new HumanPlayer();
		playerOne.setId(1L);
		Player playerTwo = new HumanPlayer();
		playerTwo.setId(2L);

		game.getPlayers().add(playerOne);
		game.getPlayers().add(playerTwo);
		game.setRoundSequence(new int[] {2, 2});
		Round round = game.addRound();
		
		game.bid(playerOne, 2);
		game.bid(playerTwo, 1);
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
		game.playCard(playerOne, card);
		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(playerTwo, card);

		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(playerTwo, card);
		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(playerOne, card);

		assertFalse("Game should not be finished", game.isFinished());

		// Second Round
		
		round = game.getCurrentRound();
		game.bid(playerTwo, 2);
		game.bid(playerOne, 1);
		round.selectTrumps(1, Card.Suit.CLUBS);
	
		deck = new ArrayList<Card>(EnumSet.allOf(Card.class));
		round.getHands().get(0).getCards().clear();
		round.getHands().get(1).getCards().clear();
		round.getHands().get(0).getCards().addAll(deck.subList(14, 16));
		round.getHands().get(1).getCards().addAll(deck.subList(27, 29));

		hand = round.getHands().get(1);
		card = hand.getCards().get(1);
		game.playCard(playerTwo, card);
		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(playerOne, card);
		
		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(playerTwo, card);
		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(playerOne, card);

		assertTrue("Game should be finished", game.isFinished());

	}
	
	public void testGameScores() throws Exception {
		Game game = new Game();
		Player playerOne = new HumanPlayer();
		playerOne.setId(1L);
		Player playerTwo = new HumanPlayer();
		playerTwo.setId(2L);

		game.getPlayers().add(playerOne);
		game.getPlayers().add(playerTwo);
		game.setRoundSequence(new int[] {2, 2});
		Round round = game.addRound();
		
		game.bid(playerOne, 2);
		game.bid(playerTwo, 1);
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
		game.playCard(playerOne, card);
		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(playerTwo, card);

		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(playerOne, card);
		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(playerTwo, card);

		assertTrue("Scores are wrong should be 12 and 0 but got " + game.scores(0,0).get(0) + " and " + game.scores(0,0).get(1),
				game.scores(0,0).get(0) == 12 && game.scores(0,0).get(1) == 0);

		// Second Round
		
		round = game.getCurrentRound();
		game.bid(playerTwo, 1);
		game.bid(playerOne, 2);
		round.selectTrumps(0, Card.Suit.CLUBS);
	
		deck = new ArrayList<Card>(EnumSet.allOf(Card.class));
		round.getHands().get(0).getCards().clear();
		round.getHands().get(1).getCards().clear();
		round.getHands().get(1).getCards().add(deck.get(27));
		round.getHands().get(0).getCards().addAll(deck.subList(18, 20));
		round.getHands().get(1).getCards().add(deck.get(15));

		hand = round.getHands().get(1);
		card = hand.getCards().get(1);
		game.playCard(playerTwo, card);
		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(playerOne, card);
		
		hand = round.getHands().get(0);
		card = hand.getCards().get(0);
		game.playCard(playerOne, card);
		hand = round.getHands().get(1);
		card = hand.getCards().get(0);
		game.playCard(playerTwo, card);

		assertTrue("Scores are wrong should be 13 and 11 but got " + game.scores(0,1).get(0) + " and " + game.scores(0,1).get(1),
				game.scores(0,1).get(0) == 13 && game.scores(0,1).get(1) == 11);

	}

}
