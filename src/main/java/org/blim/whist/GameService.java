package org.blim.whist;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GameService {

	public static final int MAX_CARDS = 52;
	
	public static final int[] ROUND_SEQUENCE_DFLT = {13,12,11,10,9,8,7,6,5,4,3,2,2,2,2,3,4,5,6,7,8,9,10,11,12,13};

	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
			
}
