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
	private int firstPlayer;
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

	public int getFirstPlayer() {
		return firstPlayer;
	}
	public void setFirstPlayer(int firstPlayer) {
		this.firstPlayer = firstPlayer;
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
	public void bid(int player, int bid) throws WhistException {
		
		if ((getNumberOfBids() + firstPlayer) % hands.size() != player) {
			throw new WhistException("Sorry, it isn't your turn");
		}
		
		if (bid < 0 || bid > numberOfCards) {
			throw new WhistException("Sorry, this round doesn't have " + bid + " cards");
		}
		
		if (bids.size() > player && bids.get(player) != null) {
			throw new WhistException("Sorry, you have already bid");			
		}
		
		if (getNumberOfBids() == hands.size() - 1) {
			int bidCount = 0;
			
			for (int i = firstPlayer; i - firstPlayer < getNumberOfBids(); i++) {
				bidCount += bids.get(i % hands.size());
			}

			if (bidCount + bid == numberOfCards) {
				throw new WhistException("Sorry, the bids can't add up to " + numberOfCards);							
			}
		}
		
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
	public void selectTrumps(int player, Suit trumps) throws WhistException {

		if (highestBidder() != player) {
			throw new WhistException("Sorry, you didn't bid highest");
		}
		
		setTrumps(trumps);
	}
	
	@Transient
	public void playCard(int player, Card card) throws WhistException {
		Trick trick = getCurrentTrick();
		
		// Check to see if it was legal to play that card
		if (!hands.get(player).getCards().contains(card)) {
			throw new WhistException("Sorry, it is not in your hand");			
		}
		
		if ((trick.getNumberOfCards() + trick.getFirstPlayer()) % hands.size() != player) {
			throw new WhistException("Sorry, it isn't your turn");
		}
		
		if (trick.getNumberOfCards() > 0) {
			Suit firstCardSuit = trick.getCards().get(trick.getFirstPlayer()).getSuit();
			
			if (firstCardSuit != card.getSuit()) {
				for (Card handCard : hands.get(player).getCards()) {
					if (firstCardSuit == handCard.getSuit()) {
						throw new WhistException("Sorry, when possible you must follow suit");
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
	
	@Transient
	public int highestBidder() {
		int maxBid = -1;
		int maxBidder = -1;
		int numberOfPlayers = hands.size();

		for (int i = firstPlayer; i - firstPlayer < getNumberOfBids(); i++) {
			if (bids.get(i % numberOfPlayers) > maxBid) {
				maxBidder = i % numberOfPlayers;
				maxBid = bids.get(i % numberOfPlayers);
			}
		}
		
		return maxBidder;
	}
	
}