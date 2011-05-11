package org.blim.whist.player;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

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
	public Object clone() {
		return new HumanPlayer(this);
	}

}
