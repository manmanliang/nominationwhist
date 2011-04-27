package org.blim.whist.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminEditFormImpl implements AdminEditForm {

	private PlayerDAO playerDAO;

	public PlayerDAO getPlayerDAO() {
		return playerDAO;
	}

	@Autowired
	public void setPlayerDAO(PlayerDAO playerDAO) {
		this.playerDAO = playerDAO;
	}

	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("username");
	}

	public ModelAndView setupForm(@PathVariable("username") String username, HttpSession session) throws IOException {
    	Map<String, Object> model = new HashMap<String, Object>();

    	Player player = playerDAO.load(username);
    	
       	model.put("player", player);
       	
       	Player currPlayer = (Player) player.clone();

    	session.setAttribute("currPlayer", currPlayer);

    	return new ModelAndView("players/adminForm", model);
	}

    public ModelAndView processSubmit(@ModelAttribute("player") Player player, BindingResult result, 
    								  SessionStatus status, @PathVariable("username") String username, 
    								  HttpSession session) {
    	Player currPlayer = (Player) session.getAttribute("currPlayer");
    	session.removeAttribute("currPlayer");
    	
    	if (player.getPassword().isEmpty()) {
    		player.setPassword(currPlayer.getPassword());
       	} else {
       		player.setPassword(playerDAO.encryptPassword(player));
       	}

    	player.setUsername(username);
    	playerDAO.update(player);
    	
    	status.setComplete();

    	return new ModelAndView("redirect:/admin/players/" + player.getUsername());
    }

}