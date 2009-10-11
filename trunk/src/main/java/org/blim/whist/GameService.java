package org.blim.whist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class GameService {

	private static final int MAX_CARDS = 52;
	
	public static final int[] ROUND_SEQUENCE_DFLT = {13,12,11,10,9,8,7,6,5,4,3,2,2,2,2,3,4,5,6,7,8,9,10,11,12,13};

	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public Round createRound(Game game) {
		Round round = null;
		int roundIdx = nextRound(game);
		
		if (roundIdx < game.getRoundSequence().length) {
			List<Card> deck = new ArrayList<Card>(EnumSet.allOf(Card.class));
			Collections.shuffle(deck);

			round = new Round();
			
			for (int i = 0; i < game.getPlayers().size(); i++) {
				round.getHands().add(new Hand());
			}
		
			for (int j = 0; j < game.getRoundSequence()[roundIdx]; j++) {
				for (Hand hand : round.getHands()) {
					hand.addCard(deck.remove(0));
				}
			}
			
			game.getRounds().add(round);
		}
		
		return round;
	}
	
	public int nextRound(Game game) {
		int round = getNextPlayableRound(game, 0);
		int roundsPlayed = game.getRounds().size();
		
		while (round < game.getRoundSequence().length && roundsPlayed > 0) {
			round = getNextPlayableRound(game, ++round);
			roundsPlayed--;
		}
		
		return round;
	}
	
	private int getNextPlayableRound(Game game, int idx) {
		int round = idx;
		
		for (; round < game.getRoundSequence().length; round++) {
			if (MAX_CARDS / game.getRoundSequence()[round] >= game.getPlayers().size()) {
				return round;
			}
		}
		
		return round;
	}
	
	public void playCard(Round round, int player, Card card) {
		List<Card> cards = Iterables.getLast(round.getTrickHistory()).getCards();
		
		if (cards.size() - 1 < player) {
			// (player - (cards.size() - 1)) - 1 for number of nulls we need
			int missingPlayerCount = player - cards.size();
			for (int i = 0; i < missingPlayerCount; i++) {
				cards.add(null);
			}
			cards.add(card);
		}
		else {
			cards.set(player, card);
		}
		
		round.getHands().get(player).getCards().remove(card);
	}

	public List<Integer> gameScores(Game game) {
		List<Integer> gameScores = Lists.newArrayList();
		List<Integer> roundScores = Lists.newArrayList();
		
		for (int i = 0; i < game.getPlayers().size(); i++) {
			gameScores.set(i, new Integer(0));	
		}

		for (int i = 0; i < game.getRounds().size(); i++) {
			roundScores = roundScores(game.getRounds().get(i));
			for (int j = 0; j < game.getPlayers().size(); j++) {
				gameScores.set(j, new Integer(gameScores.get(j) + roundScores.get(i)));
			}
		}		
		
		return gameScores;
	}

	public List<Integer> roundScores(Round round) {
		
		List<Integer> scores = tricksWon(round);
		
		for (int i = 0; i < round.getHands().size(); i++) {
			if (scores.get(i).equals(round.getBids().get(i))) {
				scores.set(i, new Integer(scores.get(i) + 10));
			}
		}		
		
		return scores;
	}

	public List<Integer> tricksWon(Round round) {
		List<Integer> tricks = Lists.newArrayList();

		for (int i = 0; i < round.getHands().size(); i++) {
			tricks.add(new Integer(0));	
		}

		for (Trick trick : round.getTrickHistory()) {
			int winningPlayer = trickWinner(trick, round.getTrumps());
			tricks.set(winningPlayer, new Integer(tricks.get(winningPlayer) + 1));
		}
		
		return tricks;
	}
	
	public int trickWinner(Trick trick, Card.Suit trumps) {
		Card highestCard = null;
		int highestPlayer = -1;
		
		int numberOfCards = trick.getCards().size();
		for (int i = trick.getFirstPlayer(); i - trick.getFirstPlayer() < numberOfCards; i++) {
			if (candidateIsWinningCard(highestCard, trick.getCards().get(i % numberOfCards), trumps)) {
				highestPlayer = i % numberOfCards;
				highestCard = trick.getCards().get(i % numberOfCards);
			}
		}
		
		return highestPlayer;
	}
	
	public Round bid(Game game, String name, int bid) {
		Round round = Iterables.getLast(game.getRounds());
		int player = game.getPlayerIndex(name);
		List<Integer> bids = round.getBids();
		
		if (bids.size() - 1 < player) {
			// (player - (cards.size() - 1)) - 1 for number of nulls we need
			int missingPlayerCount = player - bids.size();
			for (int i = 0; i < missingPlayerCount; i++) {
				bids.add(null);
			}
			bids.add(bid);
		}
		else {
			bids.set(player, bid);
		}
		
		return round;
	}

	private boolean candidateIsWinningCard(Card current, Card candidate, Card.Suit trumps) {
		if (current == null) {
			return true;
		}
		
		if (current.getSuit().equals(candidate.getSuit())) {
			return current.getValue().compareTo(candidate.getValue()) < 0;
		}
		else {
			return candidate.getSuit().equals(trumps);
		}
	}

/*	public boolean roundFinished() { 
		if (trickHistory.size() == numberOfCards &&
		    trickHistory.get(numberOfCards - 1).getCards().size() == hands.size()) {
		    	return true;
		    } else 
		    	return false;
		}
*/
		
}
