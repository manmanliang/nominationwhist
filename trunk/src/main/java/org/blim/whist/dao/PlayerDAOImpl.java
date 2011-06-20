package org.blim.whist.dao;

import java.util.List;

import org.blim.whist.player.ComputerPlayer;
import org.blim.whist.player.HumanPlayer;
import org.blim.whist.player.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PlayerDAOImpl implements PlayerDAO {

	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional(readOnly = true)
	public Player get(Long id) {
		Player player = (Player) sessionFactory.getCurrentSession().createCriteria(Player.class)
					.add(Restrictions.idEq(id))
					.uniqueResult();

    	return player;
	}
	
	@Transactional(readOnly = true)
	public ComputerPlayer getComputerPlayer(Long id) {
		ComputerPlayer player = (ComputerPlayer) sessionFactory.getCurrentSession().createCriteria(ComputerPlayer.class)
					.add(Restrictions.idEq(id))
					.uniqueResult();

    	return player;
	}
	
	@Transactional(readOnly = true)
	public HumanPlayer getHumanPlayer(String username) {
		HumanPlayer player = (HumanPlayer) sessionFactory.getCurrentSession().createCriteria(HumanPlayer.class)
							.createCriteria("user")
								.add(Restrictions.eq("username", username))
							.uniqueResult();

    	return player;
	}
	
	@Transactional
	public Player update(Player player) {
		sessionFactory.getCurrentSession().update(player);
		
		return player;
	}

	@Transactional
	public Player save(Player player) {
		sessionFactory.getCurrentSession().save(player);
		
		return player;
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<Player> listPlayers() {
		
		Session session = sessionFactory.getCurrentSession();		
		List<Player> players = session.createQuery("from Player").list();

		return players;
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<HumanPlayer> listHumanPlayers() {
		
		Session session = sessionFactory.getCurrentSession();		
		List<HumanPlayer> players = session.createQuery("from HumanPlayer").list();

		return players;
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<ComputerPlayer> listComputerPlayers() {
		
		Session session = sessionFactory.getCurrentSession();		
		List<ComputerPlayer> players = session.createQuery("from ComputerPlayer").list();

		return players;
	}

	@Transactional
	public void delete(Long id) {    	
		Session session = sessionFactory.getCurrentSession();
		
		Player player = get(id);

		session.delete(player);
		
	}

	@Transactional
	public void deleteHumanPlayer(String username) {    	
		Session session = sessionFactory.getCurrentSession();
		
		Player player = getHumanPlayer(username);

		session.delete(player);
		
	}

}
