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

	public Round createRound() {
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
			if (GameService.MAX_CARDS / roundSequence[round] >= players.size()) {
				return round;
			}
		}
		
		return round;
	}
	
	
}
