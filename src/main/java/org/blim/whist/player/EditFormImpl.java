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
public class EditFormImpl implements EditForm {

	private PlayerDAO playerDAO;

	public PlayerDAO getPlayerDAO() {
		return playerDAO;
	}

	@Autowired
	public void setPlayerDAO(PlayerDAO playerDAO) {
		this.playerDAO = playerDAO;
	}

	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("username", "roles", "active");
	}

	public ModelAndView setupForm(@PathVariable("username") String username, HttpSession session) throws IOException {
    	Map<String, Object> model = new HashMap<String, Object>();

    	Player player = playerDAO.load(username);
    	
       	Player currPlayer = (Player) player.clone();
       	
       	model.put("player", player);
       	
    	session.setAttribute("currPlayer", currPlayer);

    	return new ModelAndView("players/form", model);
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

    	playerDAO.update(player);
    	
    	status.setComplete();

    	return new ModelAndView("redirect:/players/" + player.getUsername());
    }

}