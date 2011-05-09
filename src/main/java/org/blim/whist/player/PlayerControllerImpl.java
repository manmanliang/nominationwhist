package org.blim.whist.player;

import java.util.HashMap;
import java.util.Map;

import org.blim.whist.dao.HumanPlayerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PlayerControllerImpl implements PlayerController {

	private HumanPlayerDAO humanPlayerDAO;
	
    public HumanPlayerDAO getHumanPlayerDAO() {
		return humanPlayerDAO;
	}

    @Autowired
	public void setHumanPlayerDAO(HumanPlayerDAO humanPlayerDAO) {
		this.humanPlayerDAO = humanPlayerDAO;
	}

	public ModelAndView showPlayer(@PathVariable String username) {
		Map<String, Object> model = new HashMap<String, Object>();    	
		
		HumanPlayer humanPlayer = humanPlayerDAO.get(username);
		
		model.put("player", humanPlayer);
		
		return new ModelAndView("players/show", model);
    }


    public ModelAndView listPlayers() {
		Map<String, Object> model = new HashMap<String, Object>();

		model.put("players", humanPlayerDAO.listHumanPlayers());
		
		return new ModelAndView("players/list", model);
    }
    
    public ModelAndView showPlayerAdmin(@PathVariable String username) {
		Map<String, Object> model = new HashMap<String, Object>();    	
    	
		HumanPlayer humanPlayer = humanPlayerDAO.get(username);
		
		model.put("player", humanPlayer);
		
		return new ModelAndView("players/show", model);
    }

    public ModelAndView deletePlayer(@PathVariable String username) {

    	humanPlayerDAO.delete(username);
    	
		return new ModelAndView("redirect:/admin/players");
    }

}