package org.blim.whist;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

public class Trick {
	private Map<String, Card> trickCards = Maps.newHashMap();
	
	public Map<String, Card> getCards() { return Collections.unmodifiableMap(trickCards); }
	
	public void addCard(String name, Card card) {
		trickCards.put(name, card);
	}
}
