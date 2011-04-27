package org.blim.whist.player;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;

public interface PlayerDAO {

	public abstract SessionFactory getSessionFactory();

	public abstract void setSessionFactory(SessionFactory sessionFactory);

	public abstract PasswordEncoder getPasswordEncoder();

	public abstract void setPasswordEncoder(PasswordEncoder passwordEncoder);

	public abstract SaltSource getSaltSource();

	public abstract void setSaltSource(SaltSource saltSource);

	public abstract Player load(String username);

	public abstract Player get(String username);

	public abstract Player update(Player player);

	public abstract Player save(Player player);

	public abstract void delete(String username);

	public abstract List<Player> listPlayers();

	public abstract String encryptPassword(User user);

}