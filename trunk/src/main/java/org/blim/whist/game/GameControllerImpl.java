package org.blim.whist.game;

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

import org.blim.whist.WhistException;
import org.blim.whist.dao.GameDAO;
import org.blim.whist.dao.PlayerDAO;
import org.blim.whist.player.ComputerPlayer;
import org.blim.whist.player.HumanPlayer;
import org.blim.whist.player.Player;
import org.blim.whist.player.PlayerStats;
import org.blim.whist.player.PlayersStats;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Controller
public class GameControllerImpl implements GameController {

	private GameDAO gameDAO;
	private PlayersStats playersStats;
	private PlayerDAO playerDAO;

	public PlayerDAO getPlayerDAO() {
		return playerDAO;
	}

	@Autowired
	public void setPlayerDAO(PlayerDAO playerDAO) {
		this.playerDAO = playerDAO;
	}

	public GameDAO getGameDAO() {
		return gameDAO;
	}

	@Autowired
	public void setGameDAO(GameDAO gameDAO) {
		this.gameDAO = gameDAO;
	}
	
	public PlayersStats getPlayerStats() {
		return playersStats;
	}

	@Autowired
	public void setPlayerStats(PlayersStats playerStats) {
		this.playersStats = playerStats;
	}

	public ModelAndView gameList(
			HttpServletRequest request,
			Principal user) throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		Player player = playerDAO.getHumanPlayer(user.getName());

		model.put("player", player.getPrettyName());
		
		return new ModelAndView("listGames", model);
	}

	public ModelAndView createGame(Principal user) {
		Map<String, Object> model = new HashMap<String, Object>();
		Game game = new Game();
	
		game.setCreationDate(new Date());
		game.setRoundSequence(Game.ROUND_SEQUENCE_DFLT);
		
		HumanPlayer player = playerDAO.getHumanPlayer(user.getName());
		game.setCreator(player);
		game.getPlayers().add(player);

		gameDAO.save(game);
	
		model.put("id", game.getId());
	
		return new ModelAndView("redirect:/game", model);
	}

	public ModelAndView joinGame(HttpServletResponse response, @RequestParam("id") Long gameId, Principal user) {
		Map<String, Object> model = new HashMap<String, Object>();
		
		Game game = gameDAO.load(gameId);
		Player player = playerDAO.getHumanPlayer(user.getName());
		
		if (!game.getPlayers().contains(player)) {
			game.getPlayers().add(player);
			gameDAO.update(game);
		}
	
		model.put("id", gameId);
		
		return new ModelAndView("redirect:/game", model);
	}

	public ModelAndView startGame(HttpServletResponse response, @RequestParam("id") Long gameId, Principal user) {
		Map<String, Object> model = new HashMap<String, Object>();

		Game game = gameDAO.load(gameId);
		Player player = playerDAO.getHumanPlayer(user.getName());
			
		if (game.getPlayers().get(0).equals(player)) {
			game.start();
			gameDAO.update(game);
		}
	
		model.put("id", gameId);
		
		return new ModelAndView("redirect:/game", model);
	}

	public ModelAndView gameState(
			HttpServletRequest request,
			@RequestParam("id") Long gameId,
			@RequestParam(value = "ajaxtimeout", required = false) Boolean AJAXTimeout,
			Principal user) throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		List<JSONObject> JSONRounds = Lists.newArrayList();

		if (AJAXTimeout == null) {
			AJAXTimeout = true;
		}
		
		Game game = gameDAO.load(gameId);
		HumanPlayer humanPlayer = playerDAO.getHumanPlayer(user.getName());
		
		for (int idx = 0; idx < game.getRounds().size(); idx++) {
			JSONRounds.add(roundAsJSON(game, idx));
		}
				
		int trickNum = 0;
		if (game.getCurrentRound() != null) {
			trickNum = game.getCurrentRound().getTricks().size() - 1;
		}
		
		model.put("game", game);
		model.put("rounds", JSONRounds.toString());
		model.put("roundCount", game.getRoundSequence().length);
		// TODO: Should just pass user here when I can be bothered
		model.put("username", humanPlayer.getUser().getUsername());
		model.put("userShortName", humanPlayer.getShortName());
		model.put("userPrettyName", humanPlayer.getPrettyName());
		model.put("userId", humanPlayer.getUser().getId());
		model.put("userIndex", game.getPlayerIndex(humanPlayer));
		model.put("trickNum", trickNum);
		model.put("AJAXTimeout", AJAXTimeout);
		
		return new ModelAndView("gameBoard", model);
	}
	
	@SuppressWarnings("unchecked")
	public void games(
			HttpServletResponse response,
			Principal user) throws IOException {
		JSONObject JSONGames = new JSONObject();
		
		List<Game> games = gameDAO.listGames();

		JSONArray JSONNewGames = new JSONArray();
		JSONArray JSONRunningGames = new JSONArray();

		boolean gameAdded;
		
		for (Game game : games) {
			int roundSize = game.getRounds().size();
			
			// Get user names
			JSONArray JSONPlayers = new JSONArray();
			for (Player player : game.getPlayers()) {
				JSONPlayers.add(player.getPrettyName());
			}
			
			JSONObject JSONGame = new JSONObject();
			JSONGame.put("players", JSONPlayers);
			JSONGame.put("creationDate",game.getCreationDate().toString());
			JSONGame.put("id",game.getId());
			JSONGame.put("roundNum", roundSize);

			if (!game.isFinished()) {
				gameAdded = false;
				for (Player player : game.getPlayers()) {
					// TODO: is this test right?
					if (player instanceof HumanPlayer &&
						((HumanPlayer) player).getUser().getUsername().equals(user.getName())) {
						JSONRunningGames.add(JSONGame);
						gameAdded = true;
						break;
					}
				}
				if (!gameAdded && roundSize == 0) {
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

		response.setContentType("application/json");
		response.getWriter().print(JSONGames);
	}

	@SuppressWarnings("unchecked")
	public void gameStartCheck(
			HttpServletResponse response,
			HttpServletRequest request,
			Principal user) throws IOException {
		JSONObject JSONResult = new JSONObject();
		
		JSONObject JSONInput = null;
		
		try {
		 	JSONInput = parseInput(request);	
		} catch (RuntimeException e) {
			response.getWriter().print(e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		if (JSONInput == null) {
			return;
		}
								
		Long gameId = ((Number) JSONInput.get("id")).longValue();
		Game game = gameDAO.load(gameId);
		List<ComputerPlayer> computerPlayers = playerDAO.listComputerPlayers();
	
		Round currentRound = game.getCurrentRound();
		
		if (currentRound == null) {
			// Not started yet
			JSONResult.put("phase", -1);
		} else {
			JSONResult.put("phase", 0);
		}
		
		playersStats.update();

		JSONArray JSONPlayersStats = new JSONArray();
		JSONArray JSONPlayers = new JSONArray();
		JSONArray JSONComputerPlayers = new JSONArray();
		
		for (Player player : game.getPlayers()) {
			// Get players
			JSONObject JSONPlayer = new JSONObject();
			JSONPlayer.put("shortName", player.getShortName());
			JSONPlayer.put("id", player.getId());
			
			JSONPlayers.add(JSONPlayer);
			
			// Get players stats
			PlayerStats playerStats = playersStats.getPlayerStats(player);
			
			JSONObject JSONPlayerStats = new JSONObject();

			JSONPlayerStats.put("favBid", playerStats.getFavBid());
			JSONPlayerStats.put("favTrumps", playerStats.getFavTrumps());
			JSONPlayerStats.put("win", playerStats.getWin());
			JSONPlayerStats.put("correctBid", playerStats.getCorrectBid());
			
			JSONPlayersStats.add(JSONPlayerStats);
		}
	
		for (Player player : computerPlayers) {
			
			if (!game.getPlayers().contains(player)) {				
				JSONObject JSONComputerPlayer = new JSONObject();

				JSONComputerPlayer.put("shortName", player.getShortName());
				JSONComputerPlayer.put("id", player.getId());
			
				JSONComputerPlayers.add(JSONComputerPlayer);
			}
		}
	
		JSONObject JSONCreator = new JSONObject();
		JSONCreator.put("shortName", game.getCreator().getShortName());
		JSONCreator.put("id", game.getCreator().getUser().getId());
		
		JSONResult.put("playersStats", JSONPlayersStats);
		JSONResult.put("players", JSONPlayers);
		JSONResult.put("creator", JSONCreator);		
		JSONResult.put("availCompPlayers", JSONComputerPlayers);

		response.setContentType("application/json");
		response.getWriter().print(JSONResult);
	}

	@SuppressWarnings("unchecked")
	public void addPlayer(HttpServletResponse response, 
			HttpServletRequest request,
			Principal user) throws IOException {
		JSONObject JSONInput = null;
		
		try {
		 	JSONInput = parseInput(request);	
		} catch (RuntimeException e) {
			response.getWriter().print(e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		if (JSONInput == null) {
			return;
		}
								
		Long gameId = ((Number) JSONInput.get("id")).longValue();
		Long playerId = ((Number) JSONInput.get("player")).longValue();
		Game game = gameDAO.load(gameId);

		Player player = playerDAO.get(playerId);
		
		if (!game.getPlayers().contains(player)) {
			game.getPlayers().add(player);
			gameDAO.update(game);
		}
	
		JSONObject JSONResult = new JSONObject();

		JSONResult.put("result", "0");

		response.setContentType("application/json");
		response.getWriter().print(JSONResult);
	}

	@SuppressWarnings("unchecked")
	public void removePlayer(HttpServletResponse response, 
			HttpServletRequest request,
			Principal user) throws IOException {
		JSONObject JSONInput = null;
		
		try {
		 	JSONInput = parseInput(request);	
		} catch (RuntimeException e) {
			response.getWriter().print(e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		if (JSONInput == null) {
			return;
		}
								
		Long gameId = ((Number) JSONInput.get("id")).longValue();
		Long playerId = ((Number) JSONInput.get("player")).longValue();
		Game game = gameDAO.load(gameId);

		Player player = playerDAO.get(playerId);
		
		if (game.getPlayers().contains(player)) {
			game.getPlayers().remove(player);
			gameDAO.update(game);
		}
	
		JSONObject JSONResult = new JSONObject();

		JSONResult.put("result", "0");

		response.setContentType("application/json");
		response.getWriter().print(JSONResult);
	}

	@SuppressWarnings("unchecked")
	public void update(
			HttpServletResponse response,
			HttpServletRequest request,
			Principal user) throws IOException {
		JSONObject JSONResult = new JSONObject();
	
		JSONObject JSONInput = null;
		
		try {
		 	JSONInput = parseInput(request);	
		} catch (RuntimeException e) {
			response.getWriter().print(e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		if (JSONInput == null) {
			return;
		}
					
		Long gameId = ((Number) JSONInput.get("id")).longValue();
		Integer clientPhase = ((Number) JSONInput.get("phase")).intValue();
	
		Game game = gameDAO.load(gameId);
		Player player = playerDAO.getHumanPlayer(user.getName());
	
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

			response.setContentType("application/json");
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
			JSONResult.put("hand", handAsJSON(game, game.getPlayerIndex(player)));
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
		
		// and the phase
		JSONResult.put("phase", currentPhase);
		
		response.setContentType("application/json");
		response.getWriter().print(JSONResult);
	}


	public ModelAndView deleteGame(HttpServletResponse response, @RequestParam("id") Long gameId, Principal user) {
		Map<String, Object> model = new HashMap<String, Object>();
		
		Game game = gameDAO.load(gameId);
		Player player = playerDAO.getHumanPlayer(user.getName());
		
		for (Player currPlayer : game.getPlayers()) {
			if (currPlayer.equals(player)) {
				gameDAO.delete(gameId);
				break;
			}
		}
		
		return new ModelAndView("redirect:/", model);
	}

	@SuppressWarnings("unchecked")
	public void bid(
			HttpServletResponse response,
			HttpServletRequest request,
			Principal user) throws IOException {

		JSONObject JSONResult = new JSONObject();
		JSONObject JSONInput = null;
		
		try {
		 	JSONInput = parseInput(request);	
		} catch (RuntimeException e) {
			response.getWriter().print(e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		if (JSONInput == null) {
			return;
		}
					
		Long gameId = ((Number) JSONInput.get("id")).longValue();
		Integer bid = ((Number) JSONInput.get("bid")).intValue();

		Game game = gameDAO.load(gameId);
		Player player = playerDAO.getHumanPlayer(user.getName());

		try {
			game.bid(player, bid);
		} catch (WhistException whistException) {
			JSONResult.put("errorMessage", whistException.getMessage());
			JSONResult.put("result", "-1");

			response.setContentType("application/json");
			response.getWriter().print(JSONResult);
			return;
		}
		
		gameDAO.update(game);

		JSONResult.put("result", "0");
		
		response.setContentType("application/json");
		response.getWriter().print(JSONResult);
	}	

	@SuppressWarnings("unchecked")
	public void trumps(
			HttpServletResponse response,
			HttpServletRequest request,
			Principal user) throws IOException {

		JSONObject JSONResult = new JSONObject();
		JSONObject JSONInput = null;
		
		try {
		 	JSONInput = parseInput(request);	
		} catch (RuntimeException e) {
			response.getWriter().print(e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		if (JSONInput == null) {
			return;
		}

		Long gameId = ((Number) JSONInput.get("id")).longValue();
		Card.Suit trumps = Enum.valueOf(Card.Suit.class, JSONInput.get("trumps").toString().replace("-", "_"));

		Game game = gameDAO.load(gameId);
		Player player = playerDAO.getHumanPlayer(user.getName());
		
		try {
			game.selectTrumps(player, trumps);
		} catch (WhistException whistException) {
			JSONResult.put("errorMessage", whistException.getMessage());
			JSONResult.put("result", "-1");

			response.setContentType("application/json");
			response.getWriter().print(JSONResult);
			return;
		}
		
		gameDAO.update(game);

		JSONResult.put("result", "0");
		
		response.setContentType("application/json");
		response.getWriter().print(JSONResult);
	}
	
	@SuppressWarnings("unchecked")
	public void playCard(
			HttpServletResponse response,
			HttpServletRequest request,
			Principal user) throws IOException {
		
		JSONObject JSONResult = new JSONObject();
		JSONObject JSONInput = null;
		
		try {
		 	JSONInput = parseInput(request);	
		} catch (RuntimeException e) {
			response.getWriter().print(e.getMessage());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		if (JSONInput == null) {
			return;
		}
					
		Long gameId = ((Number) JSONInput.get("id")).longValue();
		String enumConstant = JSONInput.get("card").toString().replace("-", "_");
		Card card = Enum.valueOf(Card.class, enumConstant);

		Game game = gameDAO.load(gameId);
		Player player = playerDAO.getHumanPlayer(user.getName());

		try {
			game.playCard(player, card);
		} catch (WhistException whistException) {
			JSONResult.put("errorMessage", whistException.getMessage());
			JSONResult.put("result", "1");

			response.setContentType("application/json");
			response.getWriter().print(JSONResult);
			return;
		}
		
		gameDAO.update(game);
		
		JSONResult.put("result", "0");

		response.setContentType("application/json");
		response.getWriter().print(JSONResult);
	}	
	
	public ModelAndView login(
			@RequestParam(value = "error", required = false) Boolean error,
			HttpServletRequest request,
			Principal user) throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		if (error == null) {
			error = false;
		}
		
		model.put("error", error);
		
		return new ModelAndView("login", model);
	}

	public ModelAndView error(
			HttpServletRequest request) throws IOException {
		return new ModelAndView("error");
	}

	public ModelAndView accessDenied(
			HttpServletRequest request) throws IOException {
		return new ModelAndView("accessDenied");
	}

	public ModelAndView gameInformation(
			HttpServletRequest request) throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		String version = getVersion(request);

		model.put("version", version);
		
		return new ModelAndView("information", model);
	}

	public ModelAndView footer(
			HttpServletRequest request,
			Principal principal) throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();

		if (principal != null) {
			Player player = playerDAO.getHumanPlayer(principal.getName());

			if (player != null) {
				model.put("playerName", player.getPrettyName());
				model.put("username", principal.getName());
			}
		}

		return new ModelAndView("footer", model);
	}

	private JSONObject parseInput(HttpServletRequest request) throws IOException, RuntimeException {
		JSONObject json = new JSONObject();
		
		BufferedReader reader = request.getReader();
		Object obj = JSONValue.parse(reader);
		
		if (!(obj instanceof JSONObject)) {
			throw new RuntimeException("Received an invalid JSONObject");
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
