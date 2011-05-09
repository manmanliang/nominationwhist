package org.blim.whist.player;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.blim.whist.BaseEntity;

@Entity
public abstract class Player extends BaseEntity {
		
	public Player () {}
	
	protected Player(Player another) {
		super(another);
	}
	
    @Transient
	public abstract String getPlayerPrettyName();

    @Transient
	public abstract String getPlayerShortName();

}
