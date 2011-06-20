package org.blim.whist.dao;

import java.util.List;

import org.blim.whist.player.ComputerPlayer;
import org.blim.whist.player.HumanPlayer;
import org.blim.whist.player.Player;

public interface PlayerDAO {

	public abstract Player save(Player player);

	public abstract Player get(Long id);

	public abstract ComputerPlayer getComputerPlayer(Long id);

	public abstract HumanPlayer getHumanPlayer(String username);

	public abstract Player update(Player player);

	public abstract List<Player> listPlayers();

	public abstract List<HumanPlayer> listHumanPlayers();

	public abstract List<ComputerPlayer> listComputerPlayers();

	public void delete(Long id);    	

	public void deleteHumanPlayer(String username);    	

}