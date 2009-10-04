package org.blim.whist;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;

public class Hand extends HttpServlet {
	private GameData gameData = new GameData();
	private GameService gameService = new GameService();
	
	public void doGet (HttpServletRequest request,
	                       HttpServletResponse response) {

	    try {
	    	gameData.resetData();
	    	gameService.dealRound(gameData.getDeck(), 13, gameData.playerOne(), gameData.playerTwo(), gameData.playerThree(), gameData.playerFour());
	    	String jsonHand = JSONValue.toJSONString(gameData.playerOne().getCards());
	    	
	        request.setAttribute ("data", gameData.playerOne().getCards());
	        getServletConfig().getServletContext().getRequestDispatcher("/jsp/hand.jsp").forward(request, response);
	    } catch (Exception ex) {
	        ex.printStackTrace ();
	    }
	}
}
