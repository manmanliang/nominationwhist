package org.blim.whist.player;

import javax.persistence.Entity;


@Entity
public class Player extends User {
	private String prettyName;
	private String shortName;

	public Player() {}
	
	protected Player(Player another) {
		super(another);
		prettyName = another.prettyName;
		shortName = another.shortName;
	}
	
	public void setPrettyName(String prettyName) {
		this.prettyName = prettyName;
	}

	public String getPrettyName() {
		return prettyName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getShortName() {
		return shortName;
	}
	
	@Override
	public Object clone() {
		return new Player(this);
	}
}
