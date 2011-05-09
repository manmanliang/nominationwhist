package org.blim.whist.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.blim.whist.WhistException;
import org.blim.whist.game.Card.Suit;
import org.blim.whist.player.Player;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Entity
public class Game {

	private Long id;
	private Date creationDate;	
	private List<Round> rounds = Lists.newArrayList();	
	private List<Player> players = Lists.newArrayList();
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
	
	@ManyToMany(cascade = CascadeType.ALL)
	@IndexColumn(name = "sortkey")
	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	@OneToMany(cascade = CascadeType.ALL)
	public List<Round> getRounds() {
		return rounds;
	}

	public void setRounds(List<Round> rounds) {
		this.rounds = rounds;
	}
	
	@CollectionOfElements
	@IndexColumn(name = "sortkey")
	public int[] getRoundSequence() {
		return roundSequence;
	}
	
	public void setRoundSequence(int[] roundSequence) {
		this.roundSequence = roundSequence;
	}
	
	@Transient
	public Integer getPlayerIndex(Player player) {
		return players.indexOf(player);
	}
	
	@Transient
	public Round getCurrentRound() {
		if (rounds.size() == 0) {
			return null;
		}
		
		return Iterables.getLast(rounds);
	}

	@Transient
	public void start() {
		if (getCurrentRound() == null) {
			addRound();
			addTrick();			
		}
	}

	@Transient
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
			round.setFirstPlayer(rounds.size() % players.size());
			
			rounds.add(round);
		}
		
		return round;
	}
	
	@Transient
	public int nextRound() {
		int round = getNextPlayableRound(0);
		int roundsPlayed = rounds.size();
		
		while (round < roundSequence.length && roundsPlayed > 0) {
			round = getNextPlayableRound(++round);
			roundsPlayed--;
		}
		
		return round;
	}
	
	@Transient
	public List<Integer> scores(int startRound, int endRound) {
		List<Integer> gameScores = Lists.newArrayList();
		List<Integer> roundScores = Lists.newArrayList();
		
		for (int i = 0; i < players.size(); i++) {
			gameScores.add(new Integer(0));	
		}

		for (int i = startRound; i <= endRound; i++) {
			if (rounds.get(i).isFinished()) {
				roundScores = rounds.get(i).scores();
				for (int j = 0; j < players.size(); j++) {
					gameScores.set(j, new Integer(gameScores.get(j) + roundScores.get(j)));
				}
			}
		}		
		
		return gameScores;
	}

	@Transient
	public Trick addTrick() {
		Round currentRound = getCurrentRound();
		Trick trick = null;
		
		if (currentRound.getTricks().size() < currentRound.getNumberOfCards()) {
			trick = new Trick();
			currentRound.getTricks().add(trick);
			if (currentRound.getTricks().size() == 1) {
				trick.setFirstPlayer((rounds.size() - 1) % players.size());
			} else {
				int previousWinner = currentRound.getTricks().get(currentRound.getTricks().size() - 2).winner(currentRound.getTrumps());
				trick.setFirstPlayer(previousWinner);			
			}
		}
		
		return trick;
	}

	@Transient
	public boolean isFinished() {
		if (rounds.size() == roundSequence.length && Iterables.getLast(rounds).isFinished()) {
			return true;
		} else { 
			return false;
		}
	}

	@Transient
	public Integer activePlayer() {
		Round currentRound = getCurrentRound();

		if (currentRound == null) {
			return null;
		}
		
		int numberOfBids = currentRound.getNumberOfBids();
		
		if (numberOfBids == 0) {
			return rounds.indexOf(currentRound) % players.size();
		}
		
		if (numberOfBids < players.size()) {
			List<Integer> currentBids = currentRound.getBids();
			int i = rounds.indexOf(currentRound) % players.size();
			
			do {
				i = (i + 1) % players.size();
			} while (i < currentBids.size() &&
					 currentBids.get(i) != null);
			
			return i;
		}
		
		if (currentRound.getTrumps() == null) {
			return currentRound.highestBidder();
		}
		
		Trick currentTrick = Iterables.getLast(currentRound.getTricks());
		
		if (currentTrick.getNumberOfCards() == 0) {
			return currentTrick.getFirstPlayer();
		} else {
			if (currentTrick.getNumberOfCards() == players.size()) {
				return -1;
			} else {
				// return one more than the current highest index
				return (currentTrick.getNumberOfCards() + currentTrick.getFirstPlayer()) % players.size();
			}
		}
	}

	@Transient
	public void bid(Player player, Integer bid) throws WhistException {
		int playerIndex = getPlayerIndex(player);
		if (playerIndex != activePlayer()) {
			throw new WhistException("Sorry, it isn't your turn");
		}

		Round currentRound = getCurrentRound();
		int maxZeroCount = 3;
		
		// Make sure this doesn't exceed max num of 0 bids
		if (bid == 0) {
			if (rounds.size() > maxZeroCount) {
				int currentRoundIdx = rounds.indexOf(currentRound);
				int i = currentRoundIdx - maxZeroCount;
				int zeroCount = 0;
				
				while (i != currentRoundIdx &&
					   rounds.get(i).getBids().get(playerIndex) == 0) {
					zeroCount++;
					i++;
				}
				
				if (zeroCount == maxZeroCount) {
					throw new WhistException("Sorry, you can't bid 0 more than " + maxZeroCount + " times");
				}
			}
		}
		
		currentRound.bid(playerIndex, bid);
	}
	
	@Transient
	public void selectTrumps (Player player, Suit trumps) throws WhistException {
		Integer playerIndex = getPlayerIndex(player);
		if (playerIndex != activePlayer()) {
			throw new WhistException("Sorry, it isn't your turn");
		}

		Round currentRound = getCurrentRound();

		if (currentRound.highestBidder() != playerIndex) {
			throw new WhistException("Sorry, you didn't bid highest");
		}
		
		currentRound.selectTrumps(playerIndex, trumps);
		
		if (Iterables.getLast(currentRound.getTricks()).getNumberOfCards() == players.size()) {
			if (addTrick() == null) {
				if (addRound() != null) {
					addTrick();
				}
			}
		}
	}
	
	@Transient
	public void playCard (Player player, Card card) throws WhistException {
		Integer playerIndex = getPlayerIndex(player);
		if (playerIndex != activePlayer()) {
			throw new WhistException("Sorry, it isn't your turn");
		}

		Round currentRound = getCurrentRound();

		currentRound.playCard(playerIndex, card);
		
		if (Iterables.getLast(currentRound.getTricks()).getNumberOfCards() == players.size()) {
			if (addTrick() == null) {
				if (addRound() != null) {
					addTrick();
				}
			}
		}
	}
	
	@Transient
	public String gamePhase() {
		Round currentRound = getCurrentRound();
		
		if (currentRound.getHands().get(0).getCards().size() == 0 || 
				Iterables.getLast(currentRound.getTricks()).getNumberOfCards() == 0) {
			return "loading";
		}
		else if (rounds.size() == roundSequence.length &&
					Iterables.getLast(rounds).isFinished()) {
			return "finished";
		}
		else if (currentRound.getNumberOfBids() < players.size()) {
			return "bid";
		}
		else if (currentRound.getTrumps() == null) {
			return "trumps";
		} else {
			return "trick";
		}
	    
	}
	
	@Transient
	private int getNextPlayableRound(int idx) {
		int round = idx;
		
		for (; round < roundSequence.length; round++) {
			if (Game.MAX_CARDS / roundSequence[round] >= players.size()) {
				return round;
			}
		}
		
		return round;
	}
	
}
