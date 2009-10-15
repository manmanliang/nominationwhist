package org.blim.whist;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

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
	
	@RequestMapping("/hand")
	public void handState(ServletResponse response, @RequestParam("id") Long gameId, Principal user) throws IOException {
		Game game = new Game();
		List<Card> sortedCards = new ArrayList<Card>();
		Session session = sessionFactory.getCurrentSession();

		session.load(game, gameId);
		
		int player = game.getPlayerIndex(user.getName());
		
	    sortedCards.addAll(Iterables.getLast(game.getRounds()).getHands().get(player).getCards());
	    Collections.sort(sortedCards, new OrderComparator());
	    
	    String hand = JSONValue.toJSONString(sortedCards);
		response.getWriter().print(hand);
	}
	
	@RequestMapping("/round")
	public void roundState(ServletResponse response, @RequestParam("id") Long gameId, Principal user) throws IOException {
		Game game = new Game();
		Session session = sessionFactory.getCurrentSession();

		session.load(game, gameId);
		
		int idx = game.getRounds().size();
		JSONObject JSONRound = roundAsJSON(game, idx);
	    
		response.getWriter().print(JSONRound);
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject roundAsJSON(Game game, int idx) {
		Round round = game.getRounds().get(idx);
	    JSONObject JSONRound = new JSONObject();
	    
	    JSONRound.put("idx", idx);
	    JSONRound.put("trumps", round.getTrumps());
	    JSONRound.put("bids", round.getBids());
	    JSONRound.put("tricksWon", round.tricksWon());
	    JSONRound.put("scores", round.scores());
	    JSONRound.put("highestBidder", round.highestBidder());
	    JSONRound.put("finished", round.isFinished());

	    return JSONRound;
	}
	
	@RequestMapping("/trick")
	public void trickState(ServletResponse response, @RequestParam("id") Long gameId) throws IOException {
		Game game = new Game();
		Session session = sessionFactory.getCurrentSession();

		session.load(game, gameId);
		
		Round round = game.getCurrentRound();
		Trick trick = null;
		String JSONTrick = "[]";
			
		if (round.getTricks().size() > 0) {
			trick = Iterables.getLast(round.getTricks());
			JSONTrick = JSONValue.toJSONString(trick.getCards());
		}

		response.getWriter().print(JSONTrick);
	}
	
	@RequestMapping("/game")
	public ModelAndView gameState(@RequestParam("id") Long gameId, Principal user) {
		Map<String, Object> model = new HashMap<String, Object>();
		Game game = new Game();
		List<JSONObject> JSONRounds = Lists.newArrayList();
		Session session = sessionFactory.getCurrentSession();

		session.load(game, gameId);

		for (int i = 0; i < game.getRounds().size(); i++) {
			JSONRounds.add(roundAsJSON(game, i));
		}
		
		model.put("game", game);
		model.put("rounds", JSONRounds);
		model.put("roundCount", game.getRoundSequence().length);
		model.put("user", user.getName());
		model.put("userIndex", game.getPlayerIndex(user.getName()));
		
		return new ModelAndView("GameBoard", model);
	}
	
	@Transactional
	@RequestMapping(value = "/create-game", method = RequestMethod.POST)
	public ModelAndView createGame(Principal user) {
		Map<String, Object> model = new HashMap<String, Object>();
		Game game = new Game();
		Session session = sessionFactory.getCurrentSession();
	
		game.setCreationDate(new Date());
		game.setRoundSequence(Game.ROUND_SEQUENCE_DFLT);
		game.getPlayers().add(user.getName());
		session.save(game);
		session.flush();

		model.put("id", game.getId());

		return new ModelAndView("redirect:/game", model);
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
	
	@Transactional
	@RequestMapping(value = "/start-game", method = RequestMethod.POST)
	public ModelAndView startGame(HttpServletResponse response, @RequestParam("id") Long gameId, Principal user) {
		Map<String, Object> model = new HashMap<String, Object>();
		Game game = new Game();
		Session session = sessionFactory.getCurrentSession();

		session.load(game, gameId);
				
		if (game.getPlayers().get(0).equals(user.getName())) {
			game.addRound();
			game.addTrick();
			session.save(game);
		}

		model.put("id", gameId);
		
		return new ModelAndView("redirect:/game", model);
	}
	
	@Transactional
	@RequestMapping(value = "/play-card", method = RequestMethod.POST)
	public void playCard(
			HttpServletResponse response,
			HttpServletRequest request,
			Principal user) throws IOException {
		Game game = new Game();
		Session session = sessionFactory.getCurrentSession();

		BufferedReader reader = request.getReader();

		Object obj = JSONValue.parse(reader);
		
		if (obj instanceof JSONObject) {
			JSONObject json = (JSONObject) obj;
			
			Long gameId = ((Number) json.get("id")).longValue();
			Card card = ((Card) json.get("card"));

			session.load(game, gameId);

			int player = game.getPlayerIndex(user.getName());
			game.playCard(player, card);
			
			session.save(game);
		}

		String JSONTrick = "[]";
		
		response.getWriter().print(JSONTrick);
	}	
	
	@Transactional
	@RequestMapping(value = "/bid", method = RequestMethod.POST)
	public void bid(
			HttpServletResponse response,
			HttpServletRequest request,
			Principal user) throws IOException {
		Game game = new Game();
		Session session = sessionFactory.getCurrentSession();

		BufferedReader reader = request.getReader();

		Object obj = JSONValue.parse(reader);
		
		if (obj instanceof JSONObject) {
			JSONObject json = (JSONObject) obj;
			
			Long gameId = ((Number) json.get("id")).longValue();
			Integer bid = ((Number) json.get("bid")).intValue();

			session.load(game, gameId);
				
			Round currentRound = game.getCurrentRound();
			int player = game.getPlayerIndex(user.getName());
			currentRound.bid(player, bid);
			
			session.save(game);
		}

		String JSONTrick = "[]";
		
		response.getWriter().print(JSONTrick);
	}	
}
