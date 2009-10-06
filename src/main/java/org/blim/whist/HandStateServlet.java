package org.blim.whist;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;

import com.google.common.collect.Lists;

public class HandStateServlet extends HttpServlet {
	
	private GameService gameService = new GameService();
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Round round = new Round();
		List<Card> sortedCards;

	    gameService.dealRound(
	    		Card.createDeck(), 
	    		13, 
	    		round.getHandOne(),
				round.getHandTwo(), 
				round.getHandThree(), 
				round.getHandFour());
	    
	    sortedCards = Card.sortCards(round.getHandOne().getCards());
		String hand = JSONValue.toJSONString(sortedCards);
		response.getWriter().print(hand);
	}
}