package org.blim.whist;

import com.google.common.collect.Lists;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Round {

	private List<Card> deck = Card.createDeck();

	private Long id;
	
	private Hand handOne = new Hand();
	private Hand handTwo = new Hand();
	private Hand handThree = new Hand();
	private Hand handFour = new Hand();

	@Id
	public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
	
	public Hand getHandOne() { return handOne; }
	public Hand getHandTwo() { return handTwo; }
	public Hand getHandThree() { return handThree; }
	public Hand getHandFour() { return handFour; }

	@Transient
	public List<Card> getDeck() {
		return deck;
	}

	public void resetData() {
		handOne.clearCards();
		handTwo.clearCards();
		handThree.clearCards();
		handFour.clearCards();
		deck = Card.createDeck();
	}
}
