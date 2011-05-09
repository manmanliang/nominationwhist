package org.blim.whist.dao;

import java.util.List;

import org.blim.whist.player.User;

public interface UserDAO {

	public abstract User save(User user);

	public abstract User get(String username);

	public abstract User update(User user);

	public abstract void delete(String username);

	public abstract List<User> listUsers();

}