package org.blim.whist;

import com.google.common.collect.Lists;
import java.util.List;

public class Round {

	private Long id;
	private List<Trick> trickHistory = Lists.newArrayList();
	private int numberOfCards;
	private List<Hand> hands = Lists.newArrayList();

	public Round(int numHands, int numCards) {
		for(int i=0;i<numHands;i++) {
			hands.add(new Hand());
		}
		numberOfCards = numCards;
		trickHistory.add(new Trick());
	}
	
	public Round(int numCards) {
		this(4, numCards);
	}
	
	public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
	
	public int getNumberOfCards() {
		return numberOfCards;
	}

	public List<Trick> getTrickHistory() {
		return trickHistory;
	}

	public Hand getHand(int hand) { return hands.get(hand); }

	public List<Hand> getHands() { return hands; }
	
	public boolean roundFinished() { 
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
		currentTrick.addCard(name, card);
		if (currentTrick.getCards().size() == hands.size()) {
			if (trickHistory.size() < numberOfCards) {
				trickHistory.add(new Trick());
			}
		}
	}

}
