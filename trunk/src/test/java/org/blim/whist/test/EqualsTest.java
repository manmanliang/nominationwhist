package org.blim.whist.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.blim.whist.player.HumanPlayer;
import org.blim.whist.player.Player;

import com.google.common.collect.Lists;

import junit.framework.TestCase;

public class EqualsTest extends TestCase {

	public void testSimpleEquals() throws Exception {
		Player playerOne = new HumanPlayer();
		playerOne.setId(1L);
		Player playerOneDup = new HumanPlayer();
		playerOneDup.setId(1L);
		Player playerTwo = new HumanPlayer();
		playerTwo.setId(2L);

		assertTrue("pOne (id " + playerOne.getId() + ") is not equal to pOneDup (id " + playerOneDup.getId() + ")", 
				playerOne.equals(playerOneDup));
		assertFalse("pOne (id " + playerOne.getId() + ") is equal to pTwo (id " + playerTwo.getId() + ")", 
				playerOne.equals(playerTwo));
	}

	public void testCollections() throws Exception {
		Player playerOne = new HumanPlayer();
		playerOne.setId(1L);
		Player playerOneDup = new HumanPlayer();
		playerOneDup.setId(1L);
		Player playerTwo = new HumanPlayer();
		playerTwo.setId(2L);

		List<Player> players = Lists.newArrayList();
		players.add(playerOneDup);
		
		assertTrue("pOne (id " + playerOne.getId() + ") is not in List containing pOneDup (id " + playerOneDup.getId() + ")", 
				players.contains(playerOne));
		assertFalse("pTwo (id " + playerTwo.getId() + ") is in List containing only pOneDup (id " + playerOneDup.getId() + ")", 
				players.contains(playerTwo));
		
		Map<Player, Integer> playersHash = new HashMap<Player, Integer>();
		playersHash.put(playerOneDup, 0);
		
		assertTrue("pOne (id " + playerOne.getId() + ") is not in HashMap containing pOneDup (id " + playerOneDup.getId() + ")", 
				playersHash.containsKey(playerOne));
		assertFalse("pTwo (id " + playerTwo.getId() + ") is in HashMap containing only pOneDup (id " + playerOneDup.getId() + ")", 
				playersHash.containsKey(playerTwo));
				
	}
	
}
