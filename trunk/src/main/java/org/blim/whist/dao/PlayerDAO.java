package org.blim.whist.dao;

import java.util.List;

import org.blim.whist.player.Player;

public interface PlayerDAO {

	public abstract Player save(Player player);

	public abstract Player get(Long id);

	public abstract Player update(Player player);

	public abstract List<Player> listPlayers();

}