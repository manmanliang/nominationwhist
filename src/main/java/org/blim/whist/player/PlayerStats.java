package org.blim.whist.player;

import org.blim.whist.game.Card;

public class PlayerStats {
	private Float win = null;
	private Float correctBid = null;
	private Integer favBid = null;
	private Card.Suit favTrumps = null;
	
	public Float getWin() {
		return win;
	}
	public void setWin(Float win) {
		this.win = win;
	}
	public Float getCorrectBid() {
		return correctBid;
	}
	public void setCorrectBid(Float correctBid) {
		this.correctBid = correctBid;
	}
	public Integer getFavBid() {
		return favBid;
	}
	public void setFavBid(Integer favBid) {
		this.favBid = favBid;
	}
	public Card.Suit getFavTrumps() {
		return favTrumps;
	}
	public void setFavTrumps(Card.Suit favTrumps) {
		this.favTrumps = favTrumps;
	}
}
