package org.blim.whist.dao;

import java.util.List;

import org.blim.whist.player.HumanPlayer;

public interface HumanPlayerDAO {

	public abstract HumanPlayer save(HumanPlayer player);

	public abstract HumanPlayer get(String username);

	public abstract HumanPlayer update(HumanPlayer player);

	public abstract void delete(String username);

	public abstract List<HumanPlayer> listHumanPlayers();

}