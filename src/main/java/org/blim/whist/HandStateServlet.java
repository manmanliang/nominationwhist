package org.blim.whist;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;

public class HandStateServlet extends HttpServlet {
	
	private GameService gameService = new GameService();
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Game game = new Game();
		List<Card> sortedCards;

	    gameService.dealRound(Card.createDeck(), game.getCurrentRound());
	    
	    sortedCards = Card.sortCards(game.getCurrentRound().getHand(0).getCards());
		String hand = JSONValue.toJSONString(sortedCards);
		response.getWriter().print(hand);
	}
}