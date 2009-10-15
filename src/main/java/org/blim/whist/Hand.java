package org.blim.whist;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.CollectionOfElements;

import com.google.common.collect.Lists;

@Entity
public class Hand {

	private List<Card> cards = Lists.newArrayList();
	private Long id;

	@Id
	@GeneratedValue
	public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    	
	@CollectionOfElements
	public List<Card> getCards() {
		return cards;
	}
	
	public void addCard(Card card) {
		cards.add(card);
	}
	
	public void setCards(List<Card> cards) {
		this.cards.clear();
		this.cards.addAll(cards);
	}
	
}
