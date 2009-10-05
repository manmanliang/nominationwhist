package org.blim.whist;
import javax.servlet.http.*;

public class GameBoardServlet extends HttpServlet {
	private Round gameData = new Round();
	private GameService gameService = new GameService();
	
	public void doGet (HttpServletRequest request,
	                       HttpServletResponse response) {

	    try {
	    	gameData.resetData();
	    	gameService.dealRound(gameData.getDeck(), 13, gameData.getHandOne(), gameData.getHandTwo(), gameData.getHandThree(), gameData.getHandFour());
	        request.setAttribute ("playerOne", gameData.getHandOne());
	        request.setAttribute ("playerTwo", gameData.getHandTwo());
	        request.setAttribute ("playerThree", gameData.getHandThree());
	        request.setAttribute ("playerFour", gameData.getHandFour());
	        getServletConfig().getServletContext().getRequestDispatcher("/jsp/GameBoard.jsp").forward(request, response);
	    } catch (Exception ex) {
	        ex.printStackTrace ();
	    }
	}
}
