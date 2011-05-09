package org.blim.whist.dao;

import java.util.List;

import org.blim.whist.game.Game;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class GameDAOImpl implements GameDAO {

	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional(readOnly = true)
	public Game load(Long gameId) {
		Game game = new Game();
		
		sessionFactory.getCurrentSession().load(game, gameId);

    	return game;
	}
	
	@Transactional
	public Game update(Game game) {
		sessionFactory.getCurrentSession().update(game);
		
		return game;
	}

	@Transactional
	public Game save(Game game) {
		sessionFactory.getCurrentSession().save(game);
		
		return game;
	}

	@Transactional
	public void delete(Long gameId) {
    	
		Session session = sessionFactory.getCurrentSession();

		Game game = new Game(); 
			
		session.load(game, gameId);
		
		session.delete(game);
		
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<Game> listGames() {
		
		Session session = sessionFactory.getCurrentSession();		
		List<Game> games = session.createQuery("from Game").list();

		return games;
	}

}
