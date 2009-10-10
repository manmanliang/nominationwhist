package org.blim.whist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Iterables;

public class GameService {

	private static final int MAX_CARDS = 52;
	
	private static final int[] ROUND_SEQUENCE_DFLT = {13,12,11,10,9,8,7,6,5,4,3,2,2,2,2,3,4,5,6,7,8,9,10,11,12,13};

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
		int roundIdx = currentRound(game);
		
		if (roundIdx < ROUND_SEQUENCE_DFLT.length) {
			List<Card> deck = new ArrayList<Card>(EnumSet.allOf(Card.class));
			Collections.shuffle(deck);

			round = new Round();
			
			for (int i = 0; i < game.getPlayers().size(); i++) {
				round.getHands().add(new Hand());
			}
		
			for (int j = 0; j < ROUND_SEQUENCE_DFLT[roundIdx]; j++) {
				for (Hand hand : round.getHands()) {
					hand.addCard(deck.remove(0));
				}
			}
			
			game.getRounds().add(round);
		}
		
		return round;
	}
	
	public int currentRound(Game game) {
		int round = getNextPlayableRound(game, 0);
		int roundsPlayed = game.getRounds().size();
		
		while (round < ROUND_SEQUENCE_DFLT.length && roundsPlayed > 0) {
			round = getNextPlayableRound(game, ++round);
			roundsPlayed--;
		}
		
		return round;
	}
	
	private int getNextPlayableRound(Game game, int idx) {
		int round = idx;
		
		for (; round < ROUND_SEQUENCE_DFLT.length; round++) {
			if (MAX_CARDS / ROUND_SEQUENCE_DFLT[round] >= game.getPlayers().size()) {
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

/*	public boolean roundFinished() { 
		if (trickHistory.size() == numberOfCards &&
		    trickHistory.get(numberOfCards - 1).getCards().size() == hands.size()) {
		    	return true;
		    } else 
		    	return false;
		}

	public void playCard(String name, int hand, Card card) {
		Trick currentTrick = trickHistory.get(trickHistory.size() - 1);
		hands.get(hand).getCards().remove(card);
		// TODO: need to add check to make sure we're not going over our budgeted size
		currentTrick.getCards().add(card);
		if (currentTrick.getCards().size() == hands.size()) {
			if (trickHistory.size() < numberOfCards) {
				//trickHistory.add(new Trick());
			}
		}
	}
*/
		
}
