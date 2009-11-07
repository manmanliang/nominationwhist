package org.blim.whist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.simple.JSONArray;
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
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@RequestMapping("/")
	public ModelAndView gameList(
			HttpServletRequest request,
			Principal user) throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		String version = getVersion(request);

		model.put("user", user.getName());
		model.put("version", version);
		
		return new ModelAndView("ListGames", model);
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

	@RequestMapping("/game")
	public ModelAndView gameState(
			HttpServletRequest request,
			@RequestParam("id") Long gameId,
			Principal user) throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		Game game = new Game();
		List<JSONObject> JSONRounds = Lists.newArrayList();
		Session session = sessionFactory.getCurrentSession();

		session.load(game, gameId);

		for (int idx = 0; idx < game.getRounds().size(); idx++) {
			JSONRounds.add(roundAsJSON(game, idx));
		}
		
		String version = getVersion(request);
		
		int trickNum = 0;
		if (game.getCurrentRound() != null) {
			trickNum = game.getCurrentRound().getTricks().size() - 1;
		}
		
		model.put("game", game);
		model.put("rounds", JSONRounds.toString());
		model.put("roundCount", game.getRoundSequence().length);
		model.put("user", user.getName());
		model.put("userIndex", game.getPlayerIndex(user.getName()));
		model.put("version", version);
		model.put("trickNum", trickNum);
		
		return new ModelAndView("GameBoard", model);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/games")
	public void games(
			HttpServletResponse response,
			Principal user) throws IOException {
		Session session = sessionFactory.getCurrentSession();
		JSONObject JSONGames = new JSONObject();
		
		List<Game> games = session.createQuery("from Game").list();

		JSONArray JSONNewGames = new JSONArray();
		JSONArray JSONRunningGames = new JSONArray();

		boolean gameAdded;
		
		for (Game game : games) {
			int roundSize = game.getRounds().size();

			if (!game.isFinished()) {
				gameAdded = false;
				for (String player : game.getPlayers()) {
					if (player.equals(user.getName())) {
						JSONObject JSONGame = new JSONObject();
						JSONGame.put("creationDate",game.getCreationDate().toString());
						JSONGame.put("players",game.getPlayers());
						JSONGame.put("id",game.getId());
						JSONGame.put("roundNum", roundSize);
						JSONRunningGames.add(JSONGame);
						gameAdded = true;
						break;
					}
				}
				if (!gameAdded && roundSize == 0) {
					JSONObject JSONGame = new JSONObject();
					JSONGame.put("creationDate",game.getCreationDate().toString());
					JSONGame.put("players",game.getPlayers());
					JSONGame.put("id",game.getId());
					JSONGame.put("roundNum", roundSize);
					JSONNewGames.add(JSONGame);
				}
			}
		}
		
		if (JSONNewGames.size() > 0) {
			JSONGames.put("newGames", JSONNewGames);
		}
		
		if (JSONRunningGames.size() > 0) {
			JSONGames.put("runningGames", JSONRunningGames);
		}

		response.getWriter().print(JSONGames);
	}

	@Transactional
	@RequestMapping(value = "/gameStart", method = RequestMethod.POST)
	@SuppressWarnings("unchecked")
	public void gameStartCheck(
			HttpServletResponse response,
			HttpServletRequest request,
			Principal user) throws IOException {
		Game game = new Game();
		JSONObject JSONResult = new JSONObject();
	
		JSONObject JSONInput = parseInput(request);		
		if (JSONInput.containsKey("internalError")) {
			response.getWriter().print(JSONInput);
			return;
		}
					
		Session session = sessionFactory.getCurrentSession();
			
		Long gameId = ((Number) JSONInput.get("id")).longValue();
		session.load(game, gameId);
	
		Round currentRound = game.getCurrentRound();
		
		if (currentRound == null) {
			// Not started yet
			JSONResult.put("phase", -1);
		} else {
			JSONResult.put("phase", 0);
		}
	
		JSONResult.put("players", game.getPlayers());
		response.getWriter().print(JSONResult);
	}

	@Transactional
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@SuppressWarnings("unchecked")
	public void update(
			HttpServletResponse response,
			HttpServletRequest request,
			Principal user) throws IOException {
		Game game = new Game();
		JSONObject JSONResult = new JSONObject();
	
		JSONObject JSONInput = parseInput(request);		
		if (JSONInput.containsKey("internalError")) {
			response.getWriter().print(JSONInput);
			return;
		}
					
		Session session = sessionFactory.getCurrentSession();
			
		Long gameId = ((Number) JSONInput.get("id")).longValue();
		Integer clientPhase = ((Number) JSONInput.get("phase")).intValue();
	
		session.load(game, gameId);
	
		Round currentRound = game.getCurrentRound();
		int idx = game.getRounds().indexOf(currentRound);
	
		// Check for game end
		if (game.getRounds().size() == game.getRoundSequence().length &&
				Iterables.getLast(game.getRounds()).isFinished()) {
			JSONResult.put("round", roundAsJSON(game, idx));
			JSONResult.put("trick", trickAsJSON(game));
			JSONResult.put("phase", 3);
			if (idx > 0) {
				JSONResult.put("previousRound", roundAsJSON(game, idx - 1));
			}
			response.getWriter().print(JSONResult);
			return;
		}
	
		int currentPhase;
		if (currentRound.getNumberOfBids() < game.getPlayers().size()) {
			currentPhase = 0;
		} else if (currentRound.getTrumps() == null) {
			currentPhase = 1;
		} else {
			currentPhase = 2;
		}
	
		if (clientPhase <= 0 || currentPhase == 0) {
			JSONResult.put("hand", handAsJSON(game, game.getPlayerIndex(user.getName())));
			// We have changed round, need to let the client know how the last round finished
			if (idx > 0) {
				JSONResult.put("previousRound", roundAsJSON(game, idx - 1));
			}
		}
		if (clientPhase != 2 || currentPhase != 2) {
			JSONResult.put("round", roundAsJSON(game, idx));
		}
		if (clientPhase == 2 || currentPhase == 2) {
			JSONResult.put("trick", trickAsJSON(game));
		}
		
		// Calculate activePlayer
		JSONResult.put("activePlayer", game.activePlayer());
		JSONResult.put("phase", currentPhase);
		
		response.getWriter().print(JSONResult);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/bid", method = RequestMethod.POST)
	public void bid(
			HttpServletResponse response,
			HttpServletRequest request,
			Principal user) throws IOException {
		Game game = new Game();
		JSONObject JSONResult = new JSONObject();

		JSONObject JSONInput = parseInput(request);		
		if (JSONInput.containsKey("internalError")) {
			response.getWriter().print(JSONInput);
			return;
		}
					
		Session session = sessionFactory.getCurrentSession();

		Long gameId = ((Number) JSONInput.get("id")).longValue();
		Integer bid = ((Number) JSONInput.get("bid")).intValue();

		session.load(game, gameId);

		int player = game.getPlayerIndex(user.getName());

		try {
			game.bid(player, bid);
		} catch (WhistException whistException) {
			JSONResult.put("errorMessage", whistException.getMessage());
			JSONResult.put("result", "-1");

			response.getWriter().print(JSONResult);
			return;
		}
		
		session.save(game);

		JSONResult.put("result", "0");
		
		response.getWriter().print(JSONResult);
	}	

	@Transactional
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/trumps", method = RequestMethod.POST)
	public void trumps(
			HttpServletResponse response,
			HttpServletRequest request,
			Principal user) throws IOException {
		Game game = new Game();
		JSONObject JSONResult = new JSONObject();

		JSONObject JSONInput = parseInput(request);		
		if (JSONInput.containsKey("internalError")) {
			response.getWriter().print(JSONInput);
			return;
		}

		Session session = sessionFactory.getCurrentSession();

		Long gameId = ((Number) JSONInput.get("id")).longValue();
		Card.Suit trumps = Enum.valueOf(Card.Suit.class, JSONInput.get("trumps").toString().replace("-", "_"));

		session.load(game, gameId);
			
		Round currentRound = game.getCurrentRound();
		int player = game.getPlayerIndex(user.getName());

		try {
			currentRound.selectTrumps(player, trumps);
		} catch (WhistException whistException) {
			JSONResult.put("errorMessage", whistException.getMessage());
			JSONResult.put("result", "-1");

			response.getWriter().print(JSONResult);
			return;
		}
		
		session.save(game);

		JSONResult.put("result", "0");
		
		response.getWriter().print(JSONResult);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/play-card", method = RequestMethod.POST)
	public void playCard(
			HttpServletResponse response,
			HttpServletRequest request,
			Principal user) throws IOException {
		Game game = new Game();
		JSONObject JSONResult = new JSONObject();

		JSONObject JSONInput = parseInput(request);		
		if (JSONInput.containsKey("internalError")) {
			response.getWriter().print(JSONInput);
			return;
		}

		Session session = sessionFactory.getCurrentSession();

		Long gameId = ((Number) JSONInput.get("id")).longValue();
		JSONResult.put("card", JSONInput.get("card").toString());
		String enumConstant = JSONInput.get("card").toString().replace("-", "_");
		Card card = Enum.valueOf(Card.class, enumConstant);

		session.load(game, gameId);

		int player = game.getPlayerIndex(user.getName());
		
		try {
			game.playCard(player, card);
		} catch (WhistException whistException) {
			JSONResult.put("errorMessage", whistException.getMessage());
			JSONResult.put("result", "1");

			response.getWriter().print(JSONResult);
			return;
		}
		
		session.save(game);
		
		JSONResult.put("result", "0");

		response.getWriter().print(JSONResult);
	}	
	
	@SuppressWarnings("unchecked")
	private JSONObject parseInput(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		BufferedReader reader = null;
		
		try {
			reader = request.getReader();
		} catch (IOException error) {
			json.put("internalError", error.getMessage());
			return json;
		}

		Object obj = JSONValue.parse(reader);
		
		if (!(obj instanceof JSONObject)) {
			json.put("internalError", "Received an invalid JSONObject");
		} else {
			json = (JSONObject) obj;			
		}
		
		return json;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray handAsJSON(Game game, int player) {
		JSONArray JSONHand = new JSONArray();
		List<Card> sortedCards = new ArrayList<Card>();
		
	    sortedCards.addAll(Iterables.getLast(game.getRounds()).getHands().get(player).getCards());
	    Collections.sort(sortedCards, new OrderComparator());
	    
	    JSONHand.addAll(sortedCards);
	    
	    return JSONHand;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject roundAsJSON(Game game, int idx) {
	    JSONObject JSONRound = new JSONObject();
		Round round = game.getRounds().get(idx);
	    
	    JSONRound.put("idx", idx);
	    JSONRound.put("trumps", round.getTrumps());
	    JSONRound.put("bids", round.getBids());
	    JSONRound.put("scores", game.scores(0, idx));
	    JSONRound.put("highestBidder", round.highestBidder());
	    JSONRound.put("numberOfCards", round.getNumberOfCards());
	    JSONRound.put("tricksWon", round.tricksWon());

	    return JSONRound;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject trickAsJSON(Game game) {
		Round round = game.getCurrentRound();
		JSONObject JSONTrick = new JSONObject();
			
		if (round.getTricks().size() > 0) {
			Trick trick = Iterables.getLast(round.getTricks());
			JSONTrick.put("cards", trick.getCards());
		    JSONTrick.put("tricksWon", round.tricksWon());
		    JSONTrick.put("trickNum", round.getTricks().size() - 1);
		}
		
	    Trick previousTrick = null;
	    int idx = game.getRounds().indexOf(round);
	    if (round.getTricks().size() > 1) {
	    	previousTrick = round.getTricks().get(round.getTricks().size() - 2);
	    } else if (idx > 0) {
	    	previousTrick = Iterables.getLast(game.getRounds().get(idx - 1).getTricks());		    	
	    }
	    
	    if (previousTrick != null) {
	    	JSONTrick.put("previousCards", previousTrick.getCards());
	    }

		return JSONTrick;
	}
	
	private String getVersion(HttpServletRequest request) throws IOException {
		InputStream manifestFile = request.getSession().getServletContext().getResourceAsStream("META-INF/MANIFEST.MF");
		Manifest manifest = new Manifest(manifestFile);
		Attributes attributes = manifest.getMainAttributes();

		String version = attributes.getValue("Implementation-Version") + " Build [" + attributes.getValue("Implementation-Build") + "]";
		
		return version;
	}
	
}
