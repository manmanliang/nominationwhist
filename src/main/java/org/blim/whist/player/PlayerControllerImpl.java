package org.blim.whist.player;

import java.util.HashMap;
import java.util.Map;

import org.blim.whist.dao.PlayerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PlayerControllerImpl implements PlayerController {

	private PlayerDAO playerDAO;
	
	public PlayerDAO getPlayerDAO() {
		return playerDAO;
	}

	@Autowired
	public void setPlayerDAO(PlayerDAO playerDAO) {
		this.playerDAO = playerDAO;
	}

	public ModelAndView showHumanPlayer(@PathVariable String username) {
		Map<String, Object> model = new HashMap<String, Object>();    	
		
		HumanPlayer humanPlayer = playerDAO.getHumanPlayer(username);
		
		model.put("player", humanPlayer);
		
		return new ModelAndView("players/showHuman", model);
    }

    public ModelAndView listPlayers() {
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("humanPlayers", playerDAO.listHumanPlayers());
		model.put("computerPlayers", playerDAO.listComputerPlayers());
		
		return new ModelAndView("players/list", model);
    }
    
    public ModelAndView showHumanPlayerAdmin(@PathVariable String username) {
		Map<String, Object> model = new HashMap<String, Object>();    	
    	
		HumanPlayer humanPlayer = playerDAO.getHumanPlayer(username);
		
		model.put("player", humanPlayer);
		
		return new ModelAndView("players/showHuman", model);
    }

    public ModelAndView showComputerPlayerAdmin(@PathVariable Long id) {
		Map<String, Object> model = new HashMap<String, Object>();    	
    	
		ComputerPlayer computerPlayer = playerDAO.getComputerPlayer(id);
		
		model.put("player", computerPlayer);
		
		return new ModelAndView("players/showComputer", model);
    }

    public ModelAndView deletePlayer(@PathVariable Long id) {

    	playerDAO.delete(id);
    	
		return new ModelAndView("redirect:/admin/players");
    }

}