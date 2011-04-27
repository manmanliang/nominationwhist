package org.blim.whist.player;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PlayerDAOImpl implements PlayerDAO {

	private SessionFactory sessionFactory;
	private PasswordEncoder passwordEncoder;
	private SaltSource saltSource;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public SaltSource getSaltSource() {
		return saltSource;
	}

	@Autowired
	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}
	
	@Transactional(readOnly = true)
	public Player load(String username) {
    	Player player = new Player();

    	sessionFactory.getCurrentSession().load(player, username);

    	return player;
	}
	
	@Transactional(readOnly = true)
	public Player get(String username) {
    	Player player = (Player) sessionFactory.getCurrentSession().get(Player.class, username);

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

	@Transactional
	public void delete(String username) {
    	Player player = new Player();
    	
		Session session = sessionFactory.getCurrentSession();
		session.load(player, username);
		session.delete(player);
		
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Player> listPlayers() {
		
		Session session = sessionFactory.getCurrentSession();		
		List<Player> players = session.createQuery("from Player").list();

		return players;
	}

    public String encryptPassword(User user) {
	    Object salt = saltSource.getSalt(user);  
	    
	    return passwordEncoder.encodePassword(user.getPassword(), salt);
    }

}
