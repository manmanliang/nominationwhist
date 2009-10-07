package org.blim.whist;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.common.collect.Lists;

@Entity
public class Game {

	private Long id;
	private Date creationDate;
	private String playerOne;
	private String playerTwo;
	private String playerThree;
	private String playerFour;
	private List<Round> rounds = Lists.newArrayList();
	private static final int[] defaultRoundsNum = {13,12,11,10,9,8,7,6,5,4,3,2,2,2,2,3,4,5,6,7,8,9,10,11,12,13};
	
	public Game(int[] roundsNum, int numHands) {
		for (int roundNum : roundsNum) {
			Round newRound = new Round(numHands, roundNum);
			rounds.add(newRound);
		}
	}

	public Game() {
		this(defaultRoundsNum, 4);
	}
	
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

	@Transient
	public List<Round> getRounds() {
		return rounds;
	}

	@Transient
	public Round getCurrentRound() {
		return rounds.get(rounds.size() - 1);
	}

}
