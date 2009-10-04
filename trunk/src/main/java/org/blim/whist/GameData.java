package org.blim.whist;

import com.google.common.collect.Lists;
import java.util.List;

public class GameData {
	private List<Card> playerOne = Lists.newArrayList();
	private List<Card> playerTwo = Lists.newArrayList();
	private List<Card> playerThree = Lists.newArrayList();
	private List<Card> playerFour = Lists.newArrayList();
	
	public List<Card> playerOne() { return playerOne; }
	public List<Card> playerTwo() { return playerTwo; }
	public List<Card> playerThree() { return playerThree; }
	public List<Card> playerFour() { return playerFour; }
	
	public void addCardToPlayer(List<Card> player, Card card) {
		player.add(card);
	}
	
	public void resetData() {
		playerOne.clear();
		playerTwo.clear();
		playerThree.clear();
		playerFour.clear();
	}
}
