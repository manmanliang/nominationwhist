package org.blim.whist.dao;

import java.util.List;

import org.blim.whist.game.Game;

public interface GameDAO {

	public abstract Game load(Long gameId);

	public abstract Game update(Game game);

	public abstract Game save(Game game);

	public abstract void delete(Long gameId);

	public abstract List<Game> listGames();

}