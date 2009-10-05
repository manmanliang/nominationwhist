package org.blim.whist;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Round {

	private Long id;
	
	private Hand handOne = new Hand();
	private Hand handTwo = new Hand();
	private Hand handThree = new Hand();
	private Hand handFour = new Hand();

	@Id
	public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
	
	public Hand getHandOne() { return handOne; }
	public Hand getHandTwo() { return handTwo; }
	public Hand getHandThree() { return handThree; }
	public Hand getHandFour() { return handFour; }

}
