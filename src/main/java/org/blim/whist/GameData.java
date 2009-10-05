package org.blim.whist;

import com.google.common.collect.Lists;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class GameData {

	private List<Card> deck = Card.createDeck();

	private Long id;
	
	private Player playerOne = new Player();
	private Player playerTwo = new Player();
	private Player playerThree = new Player();
	private Player playerFour = new Player();

	@Id
	public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
	
	public Player playerOne() { return playerOne; }
	public Player playerTwo() { return playerTwo; }
	public Player playerThree() { return playerThree; }
	public Player playerFour() { return playerFour; }

	@Transient
	public List<Card> getDeck() {
		return deck;
	}

	public void resetData() {
		playerOne.clearCards();
		playerTwo.clearCards();
		playerThree.clearCards();
		playerFour.clearCards();
		deck = Card.createDeck();
	}
}
