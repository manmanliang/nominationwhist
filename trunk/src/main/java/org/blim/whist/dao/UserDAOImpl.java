package org.blim.whist.dao;

import java.util.List;

import org.blim.whist.player.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDAOImpl implements UserDAO {

	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional(readOnly = true)
	public User get(String username) {
		User user = (User) sessionFactory.getCurrentSession().createCriteria(User.class)
					.add(Restrictions.eq("username", username))
					.uniqueResult();

    	return user;
	}
	
	@Transactional
	public User update(User user) {
		sessionFactory.getCurrentSession().update(user);
		
		return user;
	}

	@Transactional
	public User save(User user) {
		sessionFactory.getCurrentSession().save(user);
		
		return user;
	}

	@Transactional
	public void delete(String username) {
    	
		Session session = sessionFactory.getCurrentSession();

		User user = get(username);

		session.delete(user);
		
	}

	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public List<User> listUsers() {
		
		Session session = sessionFactory.getCurrentSession();		
		List<User> users = session.createQuery("from User").list();

		return users;
	}

}
