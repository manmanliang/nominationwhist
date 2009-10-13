package org.blim.whist;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/score")
	public void scoreState(ServletResponse response) throws IOException {
		Game game = new Game();
				
		// Set up data manually for now
		game.getPlayers().add("Rob");
		game.getPlayers().add("Lee");
		game.getPlayers().add("Mum");
		game.getPlayers().add("Dad");

		game.setRoundSequence(new int[] {3, 3});
	    
	    Round previousRound = game.addRound();
	    previousRound.setBids(Arrays.asList(new Integer(1), new Integer(1), new Integer(0), new Integer(0)));
	    previousRound.setTrumps(Card.Suit.SPADES);
		game.addTrick();
		previousRound.playCard(0, previousRound.getHands().get(0).getCards().get(1));
	    previousRound.playCard(1, previousRound.getHands().get(1).getCards().get(1));
	    previousRound.playCard(2, previousRound.getHands().get(2).getCards().get(1));
	    previousRound.playCard(3, previousRound.getHands().get(3).getCards().get(1));
		game.addTrick();
		previousRound.playCard(0, previousRound.getHands().get(1).getCards().get(1));
	    previousRound.playCard(1, previousRound.getHands().get(2).getCards().get(1));
	    previousRound.playCard(2, previousRound.getHands().get(3).getCards().get(1));
	    previousRound.playCard(3, previousRound.getHands().get(0).getCards().get(1));
		game.addTrick();
		previousRound.playCard(0, previousRound.getHands().get(2).getCards().get(0));
	    previousRound.playCard(1, previousRound.getHands().get(3).getCards().get(0));
	    previousRound.playCard(2, previousRound.getHands().get(0).getCards().get(0));
	    previousRound.playCard(3, previousRound.getHands().get(1).getCards().get(0));
	    
	    Round currentRound = game.addRound();
	  /*  currentRound.setBids(Arrays.asList(new Integer(1), new Integer(1), new Integer(1), new Integer(1)));
	    currentRound.setTrumps(Card.Suit.HEARTS);
		game.addTrick();
		currentRound.playCard(0, currentRound.getHands().get(0).getCards().get(1));
	    currentRound.playCard(1, currentRound.getHands().get(1).getCards().get(1));
	    currentRound.playCard(2, currentRound.getHands().get(2).getCards().get(1));
	    currentRound.playCard(3, currentRound.getHands().get(3).getCards().get(1));
		game.addTrick();
		currentRound.playCard(0, currentRound.getHands().get(1).getCards().get(1));
	    currentRound.playCard(1, currentRound.getHands().get(2).getCards().get(1));
	    currentRound.playCard(2, currentRound.getHands().get(3).getCards().get(1));
	    currentRound.playCard(3, currentRound.getHands().get(0).getCards().get(1));
		game.addTrick();
*/
	    JSONObject JSONPreviousRound = new JSONObject();
	    JSONObject JSONCurrentRound = new JSONObject();
	    JSONObject JSONScores = new JSONObject();
	    
	    JSONPreviousRound.put("numberOfCards", previousRound.getNumberOfCards());
	    JSONPreviousRound.put("trumps", previousRound.getTrumps());
	    JSONPreviousRound.put("bids", previousRound.getBids());
	    JSONPreviousRound.put("tricks", previousRound.tricksWon());
	    JSONPreviousRound.put("scores", previousRound.scores());
	    
	    JSONCurrentRound.put("numberOfCards", currentRound.getNumberOfCards());
	    JSONCurrentRound.put("trumps", currentRound.getTrumps());
	    JSONCurrentRound.put("bids", currentRound.getBids());
	    JSONCurrentRound.put("tricks", currentRound.tricksWon());
	    JSONCurrentRound.put("playerToBid", currentRound.getFirstPlayer());
	    JSONCurrentRound.put("bidWinner", currentRound.highestBidder());
	    JSONCurrentRound.put("playerToPlayCard", game.playerToPlayCard());
	    
	    JSONScores.put("previousRound", JSONPreviousRound);
	    JSONScores.put("currentRound", JSONCurrentRound);
	    JSONScores.put("gameFinished", game.finished());
	    
		response.getWriter().print(JSONScores);
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
	public ModelAndView gameBoard(@RequestParam("id") Long gameId, Principal user) {
		Map<String, Object> model = new HashMap<String, Object>();
		Game game = new Game();
		Session session = sessionFactory.getCurrentSession();

		session.load(game, gameId);

		model.put("game", game);
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
		game.setRoundSequence(GameService.ROUND_SEQUENCE_DFLT);
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
