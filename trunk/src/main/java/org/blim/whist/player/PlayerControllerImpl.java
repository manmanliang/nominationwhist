package org.blim.whist.player;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("player")
public class PlayerControllerImpl implements PlayerController {

	private PlayerDAO playerDAO;
	
	public PlayerDAO getPlayerDAO() {
		return playerDAO;
	}

	@Autowired
	public void setPlayerDAO(PlayerDAO playerDAO) {
		this.playerDAO = playerDAO;
	}

    public ModelAndView showPlayer(@PathVariable String username) {
		Map<String, Object> model = new HashMap<String, Object>();    	
		
		Player player = playerDAO.load(username);
		
		model.put("player", player);
		
		return new ModelAndView("players/show", model);
    }


    public ModelAndView listPlayers() {
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("players", playerDAO.listPlayers());
		
		return new ModelAndView("players/list", model);
    }
    
    public ModelAndView showPlayerAdmin(@PathVariable String username) {
		Map<String, Object> model = new HashMap<String, Object>();    	
    	
		Player player = playerDAO.load(username);
		
		model.put("player", player);
		
		return new ModelAndView("players/show", model);
    }

    public ModelAndView deletePlayer(@PathVariable String username) {

    	playerDAO.delete(username);
    	
		return new ModelAndView("redirect:/admin/players");
    }

}