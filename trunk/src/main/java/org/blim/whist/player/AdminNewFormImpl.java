package org.blim.whist.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes(types = Player.class)
public class AdminNewFormImpl implements AdminNewForm {

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

	public ModelAndView setupForm() throws IOException {
    	Map<String, Object> model = new HashMap<String, Object>();

    	Player player = new Player();
    	
       	model.put("player", player);
       	// TODO: This var is really sucky but we need to know if this is a new
       	// user and what's the alternative? Have redundant ID column to see
       	// if it's already been registered?
		model.put("new", 1);

    	return new ModelAndView("players/adminForm", model);
	}

    public ModelAndView processSubmit(@ModelAttribute("player") Player player, BindingResult result, 
    								  SessionStatus status) {

    	Map<String, Object> model = new HashMap<String, Object>();

    	new PlayerValidator().validate(player, result);

        // Check username is free
    	// TODO: This should be in validate but it doesn't have a database session
        if (playerDAO.get(player.getUsername()) != null) {
            result.rejectValue("username", "duplicate");        	
        }
   	
    	if (result.hasErrors()) {
    		model.put("new", 1);
    		return new ModelAndView("/players/adminForm", model);
    	}

		if (player.getPrettyName().isEmpty()) {
			player.setPrettyName(player.getUsername());
		}
		
		if (player.getShortName().isEmpty()) {
			if (player.getPrettyName().length() > 6) {
				player.setShortName(player.getPrettyName().substring(0, 7));
			} else {
				player.setShortName(player.getPrettyName());
			}
		}
		
		player.setPassword(playerDAO.encryptPassword(player));

    	playerDAO.save(player);

    	status.setComplete();

    	return new ModelAndView("redirect:/admin/players", model);
    }
}
