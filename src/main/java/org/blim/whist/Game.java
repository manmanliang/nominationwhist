package org.blim.whist;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Game {

	private Long id;
	private Date creationDate;
	private String playerOne;
	private String playerTwo;
	private String playerThree;
	private String playerFour;
	
	@Id
	@GeneratedValue
	public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    
	public void setPlayerOne(String playerOne) {
		this.playerOne = playerOne;
	}
	
	public String getPlayerOne() {
		return playerOne;
	}

	public void setPlayerTwo(String playerTwo) {
		this.playerTwo = playerTwo;
	}

	public String getPlayerTwo() {
		return playerTwo;
	}

	public void setPlayerThree(String playerThree) {
		this.playerThree = playerThree;
	}

	public String getPlayerThree() {
		return playerThree;
	}

	public void setPlayerFour(String playerFour) {
		this.playerFour = playerFour;
	}

	public String getPlayerFour() {
		return playerFour;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
}
