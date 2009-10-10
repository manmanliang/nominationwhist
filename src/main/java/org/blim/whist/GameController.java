package org.blim.whist;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Iterables;

/**
 * TODO: Does not protect against unauthenticated users.
 * 
 * @author Lee Denison (lee@longlost.info)
 */
@Controller
public class GameController {

	private SessionFactory sessionFactory;
	
	private GameService gameService;
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setGameService(GameService gameService) {
		this.gameService = gameService;
	}

	public GameService getGameService() {
		return gameService;
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
	
	@RequestMapping("/hand")
	public void handState(ServletResponse response) throws IOException {
		Game game = new Game();
		List<Card> sortedCards = new ArrayList<Card>();
				
		game.getPlayers().add("Rob");
		game.getPlayers().add("Lee");
		game.getPlayers().add("Mum");
		game.getPlayers().add("Dad");

		// Set up data manually for now
	    gameService.createRound(game);
	    sortedCards.addAll(Iterables.getLast(game.getRounds()).getHands().get(0).getCards());
	    Collections.sort(sortedCards, new OrderComparator());
	    
	    String hand = JSONValue.toJSONString(sortedCards);
		response.getWriter().print(hand);
	}
	
	@RequestMapping("/trick")
	public void trickState(ServletResponse response) throws IOException {
		Game game = new Game();

		game.getPlayers().add("Rob");
		game.getPlayers().add("Lee");
		game.getPlayers().add("Mum");
		game.getPlayers().add("Dad");

		Round round = gameService.createRound(game);
		round.getTrickHistory().add(new Trick(0));
		
		// Set up data manually for now
	    gameService.playCard(round, 0, round.getHands().get(0).getCards().get(4));
	    gameService.playCard(round, 1, round.getHands().get(1).getCards().get(2));
	    gameService.playCard(round, 2, round.getHands().get(2).getCards().get(7));

		Trick trick = round.getTrickHistory().get(round.getTrickHistory().size() - 1);		

	    String JSONTrick = JSONValue.toJSONString(trick.getCards());
		response.getWriter().print(JSONTrick);
	}
	
	@RequestMapping("/game")
	public ModelAndView gameBoard(@RequestParam("id") Long gameId, Principal user) {
		return new ModelAndView("GameBoard");
	}
	
	@Transactional
	@RequestMapping(value = "/create-game", method = RequestMethod.POST)
	public ModelAndView createGame(Principal user) {
		Game game = new Game();
		Session session = sessionFactory.getCurrentSession();
	
		game.setCreationDate(new Date());
		game.getPlayers().add(user.getName());
		session.save(game);

		return new ModelAndView("redirect:/");
	}
	
	@Transactional
	@RequestMapping(value = "/join-game", method = RequestMethod.POST)
	public ModelAndView joinGame(HttpServletResponse response, @RequestParam("id") Long gameId, Principal user) {
		Map<String, Object> model = new HashMap<String, Object>();
		Game game = new Game();
		Session session = sessionFactory.getCurrentSession();

		session.load(game, gameId);
				
		if (!game.getPlayers().contains(user.getName())) {
			game.getPlayers().add(user.getName());
			session.save(game);
		}

		model.put("id", gameId);
		
		return new ModelAndView("redirect:/game", model);
	}

}
