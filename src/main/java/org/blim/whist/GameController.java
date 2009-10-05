package org.blim.whist;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * TODO: Does not protect against unauthenticated users.
 * 
 * @author Lee Denison (lee@longlost.info)
 */
@Controller
public class GameController {

	private SessionFactory sessionFactory;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/")
	public ModelAndView gameList(Principal user) {
		Map<String, Object> model = new HashMap<String, Object>();
		Session session = sessionFactory.getCurrentSession();
		
		List<Game> games = session.createQuery("from Game").list();
		
		model.put("games", games);
		model.put("user", user.getName());
		
		return new ModelAndView("ListGames", model);
	}
	
	@Transactional
	@RequestMapping(value = "/create-game", method = RequestMethod.POST)
	public ModelAndView createGame(Principal user) {
		Game game = new Game();
		Session session = sessionFactory.getCurrentSession();
	
		game.setCreationDate(new Date());
		game.setPlayerOne(user.getName());
		session.save(game);

		return new ModelAndView("redirect:/");
	}
	
}
