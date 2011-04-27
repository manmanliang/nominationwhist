package org.blim.whist.player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.blim.whist.game.Card;
import org.blim.whist.game.Game;
import org.blim.whist.game.Round;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Component
public class PlayersStatsImpl implements PlayersStats {
	private Map<String, Integer> favBid = new HashMap<String, Integer>();
	private Map<String, Float> win = new HashMap<String, Float>();
	private Map<String, Float> correctBid = new HashMap<String, Float>();
	private Map<String, Card.Suit> favTrumps = new HashMap<String, Card.Suit>();
	
	private SessionFactory sessionFactory;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public PlayerStats getPlayerStats(String player) {
		PlayerStats playerStats = new PlayerStats();
		
		playerStats.setCorrectBid(correctBid.get(player));
		playerStats.setFavBid(favBid.get(player));
		playerStats.setFavTrumps(favTrumps.get(player));
		playerStats.setWin(win.get(player));
		
		return playerStats;
	}
	
	@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
	public void update() {
		Session session = sessionFactory.getCurrentSession();
		List<Game> games = session.createQuery("from Game").list();
		List<Game> finishedGames = Lists.newArrayList();

		for (Game game : games) {
			if (game.isFinished() && game.getPlayers().size() > 1) {
				finishedGames.add(game);
			}
		}
		
		updateFavBid(finishedGames);
		updateWin(finishedGames);
		updateCorrectBid(finishedGames);
		updateFavTrumps(finishedGames);
	}
	
	private void updateFavBid(List<Game> games) {
		Map<String, Map<Integer, Integer>> bidsCount = new HashMap<String, Map<Integer, Integer>>();
		
		for (Game game : games) {
			for (String player : game.getPlayers()) {
				// Add new players
				if (!bidsCount.containsKey(player)) {
					bidsCount.put(player, new HashMap<Integer, Integer>());
				}
				
				int bid;
				int playerIndex = game.getPlayerIndex(player);
				
				for (Round round : game.getRounds()) {
					bid = round.getBids().get(playerIndex);
					if (bidsCount.get(player).containsKey(bid)) {
						bidsCount.get(player).put(bid, bidsCount.get(player).get(bid) + 1);
					} else {
						bidsCount.get(player).put(bid, 1);
					}
				}
			}
		}
		
		for (String player : bidsCount.keySet()) {
			Integer highestBidCount = 0;
			Integer playerFavBid = null;

			for (Integer bid : bidsCount.get(player).keySet()) {
				if (bidsCount.get(player).get(bid) > highestBidCount) {
					playerFavBid = bid;
					highestBidCount = bidsCount.get(player).get(bid);
				}
			}
			
			favBid.put(player, playerFavBid);
		}
	}
	
	private void updateWin(List<Game> games) {
		Map<String, Integer> winCount = new HashMap<String, Integer>();
		
		for (Game game : games) {
			List<Integer> scores = game.scores(0, game.getRounds().size() - 1);

			Integer highestScore = 0;
			String highestPlayer = null;

			for (String player : game.getPlayers()) {
				if (!winCount.containsKey(player)) {
					winCount.put(player, 0);
				}

				int playerIndex = game.getPlayerIndex(player);
				if (scores.get(playerIndex) > highestScore) {
					highestScore = scores.get(playerIndex);
					highestPlayer = player;
				}
			}
			winCount.put(highestPlayer, winCount.get(highestPlayer) + 1);
		}
		
		float numberOfGames = games.size();
		for (String player : winCount.keySet()) {
			Float winPercent = (winCount.get(player) / numberOfGames) * 100;
			win.put(player, winPercent);
		}
	}
	
	private void updateCorrectBid(List<Game> games) {
		Map<String, Integer> correctBidCount = new HashMap<String, Integer>();
		float roundCount = 0;
		
		for (Game game : games) {
			for (Round round : game.getRounds()) {
				roundCount++;
				
				List<Integer> tricksWon = round.tricksWon();
				List<Integer> bids = round.getBids();
				
				for (String player : game.getPlayers()) {
					if (!correctBidCount.containsKey(player)) {
						correctBidCount.put(player, 0);
					}

					int playerIndex = game.getPlayerIndex(player);
					if (tricksWon.get(playerIndex).equals(bids.get(playerIndex))) {
						correctBidCount.put(player, correctBidCount.get(player) + 1);
					}
				}
			}
		}
		
		for (String player : correctBidCount.keySet()) {
			float playerBidCorrect = 0;
			if (correctBidCount.get(player) != 0) {
				playerBidCorrect = (correctBidCount.get(player) / roundCount) * 100;
			}
			correctBid.put(player, playerBidCorrect);
		}
	}

	private void updateFavTrumps(List<Game> games) {
		Map<String, Map<Card.Suit, Integer>> trumpsCount = new HashMap<String, Map<Card.Suit, Integer>>();
		
		for (Game game : games) {
			for (String player : game.getPlayers()) {
				// Add new players
				if (!trumpsCount.containsKey(player)) {
					trumpsCount.put(player, new HashMap<Card.Suit, Integer>());
				}
			}
			
			for (Round round : game.getRounds()) {
				String trumpsPlayer = game.getPlayers().get(round.highestBidder());
				Card.Suit trumps = round.getTrumps();

				if (trumpsCount.get(trumpsPlayer).containsKey(trumps)) {
					trumpsCount.get(trumpsPlayer).put(trumps, trumpsCount.get(trumpsPlayer).get(trumps) + 1);
				} else {
					trumpsCount.get(trumpsPlayer).put(trumps, 1);
				}
			}
		}
		
		for (String player : trumpsCount.keySet()) {
			Integer trumpsChosenCount = 0;
			Card.Suit playerFavTrumps = null;

			for (Card.Suit trumps : trumpsCount.get(player).keySet()) {
				if (trumpsCount.get(player).get(trumps) > trumpsChosenCount) {
					playerFavTrumps = trumps;
					trumpsChosenCount = trumpsCount.get(player).get(trumps);
				}
			}
			
			favTrumps.put(player, playerFavTrumps);
		}
	}
	
}