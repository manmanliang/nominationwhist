package org.blim.whist;

import com.google.common.collect.Lists;
import java.util.List;

public class GameData {
	List<Card> deck = Card.createDeck();

	private Player playerOne = new Player();
	private Player playerTwo = new Player();
	private Player playerThree = new Player();
	private Player playerFour = new Player();
	
	public Player playerOne() { return playerOne; }
	public Player playerTwo() { return playerTwo; }
	public Player playerThree() { return playerThree; }
	public Player playerFour() { return playerFour; }
	
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
