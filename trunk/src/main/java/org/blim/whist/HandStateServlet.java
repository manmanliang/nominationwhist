package org.blim.whist;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;

public class HandStateServlet extends HttpServlet {
	
	private Round round = new Round();
	private GameService gameService = new GameService();
	
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	    gameService.dealRound(
	    		round.getDeck(), 
	    		13, 
	    		round.getHandOne(),
				round.getHandTwo(), 
				round.getHandThree(), 
				round.getHandFour());
	    
		String hand = JSONValue.toJSONString(round.getHandOne().getCards());
		response.getWriter().print(hand);
		
	}
}
