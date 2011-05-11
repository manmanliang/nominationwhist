package org.blim.whist.player;

import javax.persistence.Entity;

@Entity
public class ComputerPlayer extends Player implements Cloneable {

	public ComputerPlayer() {}
	
	protected ComputerPlayer(ComputerPlayer another) {
		super(another);
	}
	
	@Override
	public Object clone() {
		return new ComputerPlayer(this);
	}

}
