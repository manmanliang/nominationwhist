package org.blim.whist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Entity
public class Game {

	private Long id;
	private Date creationDate;	
	private List<Round> rounds = Lists.newArrayList();	
	private List<String> players = Lists.newArrayList();
	private int[] roundSequence;
	public static final int MAX_CARDS = 52;
	public static final int[] ROUND_SEQUENCE_DFLT = {13,12,11,10,9,8,7,6,5,4,3,2,2,2,2,3,4,5,6,7,8,9,10,11,12,13};
	
	@Id
	@GeneratedValue
	public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	@CollectionOfElements
	@IndexColumn(name = "sortkey")
	public List<String> getPlayers() {
		return players;
	}

	public void setPlayers(List<String> players) {
		this.players = players;
	}

	@OneToMany(cascade = CascadeType.ALL)
	public List<Round> getRounds() {
		return rounds;
	}

	public void setRounds(List<Round> rounds) {
		this.rounds = rounds;
	}
	
	public void setRoundSequence(int[] roundSequence) {
		this.roundSequence = roundSequence;
	}
	
	@CollectionOfElements
	@IndexColumn(name = "sortkey")
	public int[] getRoundSequence() {
		return roundSequence;
	}
	public int getPlayerIndex(String name) {
		return players.indexOf(name);
	}
	
	@Transient
	public Round getCurrentRound() {
		return Iterables.getLast(rounds);
	}

	public Round addRound() {
		Round round = null;
		int roundIdx = nextRound();
		
		if (roundIdx < roundSequence.length) {
			List<Card> deck = new ArrayList<Card>(EnumSet.allOf(Card.class));
			Collections.shuffle(deck);

			round = new Round();
			
			for (int i = 0; i < players.size(); i++) {
				round.getHands().add(new Hand());
			}
		
			for (int j = 0; j < roundSequence[roundIdx]; j++) {
				for (Hand hand : round.getHands()) {
					hand.addCard(deck.remove(0));
				}
			}
			round.setNumberOfCards(roundSequence[roundIdx]);
			
			rounds.add(round);
		}
		
		return round;
	}
	
	public int nextRound() {
		int round = getNextPlayableRound(0);
		int roundsPlayed = rounds.size();
		
		while (round < roundSequence.length && roundsPlayed > 0) {
			round = getNextPlayableRound(++round);
			roundsPlayed--;
		}
		
		return round;
	}
	
	private int getNextPlayableRound(int idx) {
		int round = idx;
		
		for (; round < roundSequence.length; round++) {
			if (Game.MAX_CARDS / roundSequence[round] >= players.size()) {
				return round;
			}
		}
		
		return round;
	}
	
	public List<Integer> scores() {
		List<Integer> gameScores = Lists.newArrayList();
		List<Integer> roundScores = Lists.newArrayList();
		
		for (int i = 0; i < players.size(); i++) {
			gameScores.add(new Integer(0));	
		}

		for (int i = 0; i < rounds.size(); i++) {
			if (rounds.get(i).isFinished()) {
				roundScores = rounds.get(i).scores();
				for (int j = 0; j < players.size(); j++) {
					gameScores.set(j, new Integer(gameScores.get(j) + roundScores.get(j)));
				}
			}
		}		
		
		return gameScores;
	}

	public Trick addTrick() {
		Round currentRound = getCurrentRound();
		Trick trick = null;
		
		if (currentRound.getTricks().size() < currentRound.getNumberOfCards()) {
			trick = new Trick();
			currentRound.getTricks().add(trick);
			if (currentRound.getTricks().size() == 1) {
				trick.setFirstPlayer(rounds.size() % players.size());
			} else {
				int previousWinner = currentRound.getTricks().get(currentRound.getTricks().size() - 2).winner(currentRound.getTrumps());
				trick.setFirstPlayer(previousWinner);			
			}
		}
		
		return trick;
	}
	
	public boolean isFinished() {
		if (rounds.size() == roundSequence.length && Iterables.getLast(rounds).isFinished()) {
			return true;
		} else { 
			return false;
		}
	}

	public int playerToPlayCard() {
		Round currentRound = getCurrentRound();
		Trick currentTrick = Iterables.getLast(currentRound.getTricks());
		
		if (currentTrick.getNumberOfCards() == 0) {
			return currentTrick.getFirstPlayer();
		} else {
			if (currentTrick.getNumberOfCards() == players.size()) {
				return currentTrick.getCards().indexOf(null);
			} else {
				// return one more than the current highest index
				return currentTrick.getNumberOfCards();
			}
		}
	}

	public void playCard(int player, Card card) {
		Round currentRound = getCurrentRound();

		currentRound.playCard(player, card);
		
		if (Iterables.getLast(currentRound.getTricks()).getNumberOfCards() == players.size()) {
			if (addTrick() == null) {
				if (addRound() != null) {
					addTrick();
				}
			}
		}
	}
	
}
