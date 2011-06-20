package org.blim.whist.player;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class BaseComputerPlayer extends Player {

	public BaseComputerPlayer() {}
	
	protected BaseComputerPlayer(BaseComputerPlayer another) {
		super(another);
	}
	
	@Transient
	public final PlayerType getType() {
		return PlayerType.COMPUTER;
	}

}
