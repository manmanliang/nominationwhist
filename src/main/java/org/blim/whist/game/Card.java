package org.blim.whist.game;

import org.json.simple.JSONAware;

public enum Card implements JSONAware {
	
	TWO_SPADES(Value.TWO, Suit.SPADES),
	THREE_SPADES(Value.THREE, Suit.SPADES),
	FOUR_SPADES(Value.FOUR, Suit.SPADES),
	FIVE_SPADES(Value.FIVE, Suit.SPADES),
	SIX_SPADES(Value.SIX, Suit.SPADES),
	SEVEN_SPADES(Value.SEVEN, Suit.SPADES),
	EIGHT_SPADES(Value.EIGHT, Suit.SPADES),
	NINE_SPADES(Value.NINE, Suit.SPADES),
	TEN_SPADES(Value.TEN, Suit.SPADES),
	JACK_SPADES(Value.JACK, Suit.SPADES),
	QUEEN_SPADES(Value.QUEEN, Suit.SPADES),
	KING_SPADES(Value.KING, Suit.SPADES),
	ACE_SPADES(Value.ACE, Suit.SPADES),
	TWO_HEARTS(Value.TWO, Suit.HEARTS),
	THREE_HEARTS(Value.THREE, Suit.HEARTS),
	FOUR_HEARTS(Value.FOUR, Suit.HEARTS),
	FIVE_HEARTS(Value.FIVE, Suit.HEARTS),
	SIX_HEARTS(Value.SIX, Suit.HEARTS),
	SEVEN_HEARTS(Value.SEVEN, Suit.HEARTS),
	EIGHT_HEARTS(Value.EIGHT, Suit.HEARTS),
	NINE_HEARTS(Value.NINE, Suit.HEARTS),
	TEN_HEARTS(Value.TEN, Suit.HEARTS),
	JACK_HEARTS(Value.JACK, Suit.HEARTS),
	QUEEN_HEARTS(Value.QUEEN, Suit.HEARTS),
	KING_HEARTS(Value.KING, Suit.HEARTS),
	ACE_HEARTS(Value.ACE, Suit.HEARTS),
	TWO_CLUBS(Value.TWO, Suit.CLUBS),
	THREE_CLUBS(Value.THREE, Suit.CLUBS),
	FOUR_CLUBS(Value.FOUR, Suit.CLUBS),
	FIVE_CLUBS(Value.FIVE, Suit.CLUBS),
	SIX_CLUBS(Value.SIX, Suit.CLUBS),
	SEVEN_CLUBS(Value.SEVEN, Suit.CLUBS),
	EIGHT_CLUBS(Value.EIGHT, Suit.CLUBS),
	NINE_CLUBS(Value.NINE, Suit.CLUBS),
	TEN_CLUBS(Value.TEN, Suit.CLUBS),
	JACK_CLUBS(Value.JACK, Suit.CLUBS),
	QUEEN_CLUBS(Value.QUEEN, Suit.CLUBS),
	KING_CLUBS(Value.KING, Suit.CLUBS),
	ACE_CLUBS(Value.ACE, Suit.CLUBS),
	TWO_DIAMONDS(Value.TWO, Suit.DIAMONDS),
	THREE_DIAMONDS(Value.THREE, Suit.DIAMONDS),
	FOUR_DIAMONDS(Value.FOUR, Suit.DIAMONDS),
	FIVE_DIAMONDS(Value.FIVE, Suit.DIAMONDS),
	SIX_DIAMONDS(Value.SIX, Suit.DIAMONDS),
	SEVEN_DIAMONDS(Value.SEVEN, Suit.DIAMONDS),
	EIGHT_DIAMONDS(Value.EIGHT, Suit.DIAMONDS),
	NINE_DIAMONDS(Value.NINE, Suit.DIAMONDS),
	TEN_DIAMONDS(Value.TEN, Suit.DIAMONDS),
	JACK_DIAMONDS(Value.JACK, Suit.DIAMONDS),
	QUEEN_DIAMONDS(Value.QUEEN, Suit.DIAMONDS),
	KING_DIAMONDS(Value.KING, Suit.DIAMONDS),
	ACE_DIAMONDS(Value.ACE, Suit.DIAMONDS);
	
    public enum Value implements JSONAware { 
    	TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;

		public String toJSONString() {
			return "\"" + toString() + "\"";
		}
    }
    
    public enum Suit implements JSONAware {
    	SPADES, HEARTS, CLUBS, DIAMONDS, NO_TRUMPS;
    
		public String toJSONString() {
			return "\"" + toString() + "\"";
		}
		
		public String toString() {
			return super.toString().replace("_", "-");
		}
    }

    private final Value value;
    private final Suit suit;

    private Card(Value value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    public Value getValue() { return value; }
    public Suit getSuit() { return suit; }
    
	public String toJSONString() {
		return "\"" + toString() + "\"";
	}

	public String toString() {
		return value.toString() + "-" + suit.toString();
	}

}
