package org.blim.whist;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CollectionOfElements;

import com.google.common.collect.Lists;

@Entity
public class Round {

	private Long id;
	private int numberOfCards;
	private List<Trick> trickHistory = Lists.newArrayList();
	private List<Hand> hands = Lists.newArrayList();
	private List<Integer> bids = Lists.newArrayList();
	
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

	public int highestBidder() {
		int maxBid = -1;
		int maxBidder = -1;
		
		for (int i = 0; i < bids.size(); i++) {
			if (bids.get(i) > maxBid) {
				maxBidder = i;
			}
		}
		
		return maxBidder;
	}
}
