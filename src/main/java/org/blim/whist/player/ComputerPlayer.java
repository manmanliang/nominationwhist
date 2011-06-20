package org.blim.whist.player;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.blim.whist.game.Card;
import org.blim.whist.game.Game;
import org.blim.whist.game.Card.Suit;

@Entity
public abstract class ComputerPlayer extends BaseComputerPlayer implements Cloneable {
	
	public ComputerPlayer() {}
	
	protected ComputerPlayer(ComputerPlayer another) {
		super(another);
	}
	
	@Transient
	public abstract Integer bid(Game game);
	
	@Transient
	public abstract Suit chooseTrumps(Game game);
	
	@Transient
	public abstract Card playCard(Game game);

	@Transient
	public abstract String getComputerType();

}
