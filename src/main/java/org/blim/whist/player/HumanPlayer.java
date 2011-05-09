package org.blim.whist.player;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class HumanPlayer extends Player implements Cloneable {

	private User user;
	
	public HumanPlayer() {}
	
	protected HumanPlayer(HumanPlayer another) {
		super(another);

		this.user = (User) another.user.clone();
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	@Transient
	public String getPrettyName() {
		return user.getPrettyName();
	}

	@Override
	@Transient
	public String getShortName() {
		return user.getShortName();
	}

	@Override
	public Object clone() {
		return new HumanPlayer(this);
	}

}
