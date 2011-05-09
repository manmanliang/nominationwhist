package org.blim.whist.player;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class HumanPlayer extends Player implements Cloneable {

	private User user;
	private String prettyName;
	private String shortName;

	public HumanPlayer() {}
	
	protected HumanPlayer(HumanPlayer another) {
		super(another);

		prettyName = another.prettyName;
		shortName = another.shortName;
		this.user = (User) another.user.clone();
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
	@Transient
	public String getPlayerPrettyName() {
		return getPrettyName();
	}

	@Override
	@Transient
	public String getPlayerShortName() {
		return getShortName();
	}

	@Override
	public Object clone() {
		return new HumanPlayer(this);
	}

}
