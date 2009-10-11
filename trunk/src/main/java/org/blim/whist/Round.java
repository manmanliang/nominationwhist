package org.blim.whist;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.blim.whist.Card.Suit;
import org.hibernate.annotations.CollectionOfElements;

import com.google.common.collect.Lists;

@Entity
public class Round {

	private Long id;
	private int numberOfCards;
	private List<Trick> trickHistory = Lists.newArrayList();
	private List<Hand> hands = Lists.newArrayList();
	private List<Integer> bids = Lists.newArrayList();
	private Suit trumps;
	
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
	public List<Trick> getTrickHistory() {
		return trickHistory;
	}

	public void setTrickHistory(List<Trick> trickHistory) {
		this.trickHistory = trickHistory;
	}

	@OneToMany(cascade = CascadeType.ALL)
	public List<Hand> getHands() {
		return hands;
	}

	public void setHands(List<Hand> hands) {
		this.hands = hands;
	}

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
	
	public int highestBidder() {
		int maxBid = -1;
		int maxBidder = -1;
		
		for (int i = 0; i < bids.size(); i++) {
			if (bids.get(i) > maxBid) {
				maxBidder = i;
				maxBid = bids.get(i);
			}
		}
		
		return maxBidder;
	}
	
}
