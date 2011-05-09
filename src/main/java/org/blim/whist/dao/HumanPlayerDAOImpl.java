package org.blim.whist.dao;

import java.util.List;

import org.blim.whist.player.HumanPlayer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HumanPlayerDAOImpl implements HumanPlayerDAO {

	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional(readOnly = true)
	public HumanPlayer get(String username) {
		HumanPlayer player = (HumanPlayer) sessionFactory.getCurrentSession().createCriteria(HumanPlayer.class)
					.createCriteria("user")
						.add(Restrictions.eq("username", username))
					.uniqueResult();

    	return player;
	}
	
	@Transactional
	public HumanPlayer update(HumanPlayer player) {
		sessionFactory.getCurrentSession().update(player);
		
		return player;
	}

	@Transactional
	public HumanPlayer save(HumanPlayer player) {
		sessionFactory.getCurrentSession().save(player);
		
		return player;
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<HumanPlayer> listHumanPlayers() {
		
		Session session = sessionFactory.getCurrentSession();		
		List<HumanPlayer> players = session.createQuery("from HumanPlayer").list();

		return players;
	}

	@Transactional
	public void delete(String username) {    	
		Session session = sessionFactory.getCurrentSession();
		
		HumanPlayer player = get(username);

		session.delete(player);
		
	}

}
