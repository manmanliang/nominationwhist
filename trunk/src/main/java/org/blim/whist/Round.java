package org.blim.whist;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.blim.whist.Card.Suit;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Entity
public class Round {

	private Long id;
	private int numberOfCards;
	private List<Trick> tricks = Lists.newArrayList();
	private List<Hand> hands = Lists.newArrayList();
	private List<Integer> bids = Lists.newArrayList();
	private Suit trumps = null;
	
	@Id
	@GeneratedValue
	public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
	
	public int getNumberOfCards() {
		return numberOfCards;
	}

	public void setNumberOfCards(int numberOfCards) {
		this.numberOfCards = numberOfCards;
	}

	@OneToMany(cascade = CascadeType.ALL)
	public List<Trick> getTricks() {
		return tricks;
	}

	public void setTricks(List<Trick> tricks) {
		this.tricks = tricks;
	}

	@OneToMany(cascade = CascadeType.ALL)
	public List<Hand> getHands() {
		return hands;
	}

	public void setHands(List<Hand> hands) {
		this.hands = hands;
	}

	@IndexColumn(name = "sortkey")
	@CollectionOfElements
	public List<Integer> getBids() {
		return bids;
	}

	public void setBids(List<Integer> bids) {
		this.bids = bids;
	}

	public Suit getTrumps() {
		return this.trumps;
	}
	
	public void setTrumps(Suit trumps) {
		this.trumps = trumps;
	}

	@Transient
	public void bid(int player, int bid) {
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
	}
	
	@Transient
	public void playCard(int player, Card card) throws WhistException {
		Trick trick = getCurrentTrick();
		
		// Check to see if it was legal to play that card
		if (trick.getNumberOfCards() > 0) {
			Suit firstCardSuit = trick.getCards().get(trick.getFirstPlayer()).getSuit();
			
			if (firstCardSuit != card.getSuit()) {
				for (Card handCard : hands.get(player).getCards()) {
					if (firstCardSuit == handCard.getSuit()) {
						throw new WhistException("You are not allowed to play that card, when possible you must follow suit");
					}
				}
			}
		}
		
		trick.playCard(player, card);
		getHands().get(player).getCards().remove(card);
	}
	
	@Transient
	public List<Integer> scores() {
		if (!isFinished()) {
			return null;
		}
		
		List<Integer> scores = tricksWon();
		
		// Only add 10 for scores if we have all the bids in
		if (getNumberOfBids() == hands.size()) {
			for (int i = 0; i < hands.size(); i++) {
				if (scores.get(i).equals(bids.get(i))) {
					scores.set(i, new Integer(scores.get(i) + 10));
				}
			}
		}
		
		return scores;
	}

	@Transient
	public List<Integer> tricksWon() {
		List<Integer> trickScores = Lists.newArrayList();

		for (int i = 0; i < hands.size(); i++) {
			trickScores.add(new Integer(0));	
		}

		for (Trick trick : tricks) {
			if (trick.getNumberOfCards() == hands.size()) {
				int winningPlayer = trick.winner(trumps);
				trickScores.set(winningPlayer, new Integer(trickScores.get(winningPlayer) + 1));
			}
		}
		
		return trickScores;
	}
	
	@Transient
	public Trick getCurrentTrick() {
		return Iterables.getLast(tricks);
	}

	@Transient
	public boolean isFinished() { 
		if (tricks.size() == numberOfCards &&
				Iterables.getLast(tricks).getNumberOfCards() == hands.size()) {
			return true;
		} else { 
			return false;
		}
	}

	@Transient
	public int getNumberOfBids() {
		int count = 0;
		
		for (Integer bid : bids) {
			if (bid != null) {
				count++;
			}
		}
		
		return count;
	}
	
}