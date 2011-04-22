package org.blim.whist;

import org.hibernate.SessionFactory;

public interface PlayersStats {

	public abstract SessionFactory getSessionFactory();

	public abstract void setSessionFactory(SessionFactory sessionFactory);

	public abstract PlayerStats getPlayerStats(String player);

	public abstract void update();

}