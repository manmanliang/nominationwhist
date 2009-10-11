package org.blim.whist;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.CollectionOfElements;

import com.google.common.collect.Lists;

@Entity
public class Trick {
	private List<Card> cards = Lists.newArrayList();
	private int firstPlayer;
	private Long id;
	
	public Trick(int firstPlayer) {
		this.setFirstPlayer(firstPlayer);
	}

	@Id
	@GeneratedValue
	public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

	public void setFirstPlayer(int firstPlayer) {
		this.firstPlayer = firstPlayer;
	}

	public int getFirstPlayer() {
		return firstPlayer;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	@CollectionOfElements
    public List<Card> getCards() {
		return cards;
	}

	public void playCard(int player, Card card) {
		if (cards.size() - 1 < player) {
			// (player - (cards.size() - 1)) - 1 for number of nulls we need
			int missingPlayerCount = player - cards.size();
			for (int i = 0; i < missingPlayerCount; i++) {
				cards.add(null);
			}
			cards.add(card);
		}
		else {
			cards.set(player, card);
		}
	}

}
