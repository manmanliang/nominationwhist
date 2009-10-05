package org.blim.whist;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

public class GameBoardServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		getServletConfig().getServletContext().getRequestDispatcher("/jsp/GameBoard.jsp").forward(request, response);
	}

}
