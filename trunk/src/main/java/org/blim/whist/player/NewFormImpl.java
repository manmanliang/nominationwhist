package org.blim.whist.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;

@Controller
@SessionAttributes(types = Player.class)
public class NewFormImpl implements NewForm {

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
		model.put("new", 1);

    	return new ModelAndView("players/form", model);
	}

    public ModelAndView processSubmit(@ModelAttribute("player") Player player, BindingResult result, 
    								  SessionStatus status) {
    	Map<String, Object> model = new HashMap<String, Object>();

    	new PlayerValidator().validate(player, result);
    	
        // Check username is free
        if (playerDAO.get(player.getUsername()) != null) {
            result.rejectValue("username", "duplicate");        	
        }

    	if (result.hasErrors()) {
    		model.put("new", 1);
    		return new ModelAndView("players/form", model);
    	}

    	// Default some stuff
		if (player.getShortName().isEmpty()) {
			player.setShortName(player.getUsername());
		}
    	
		if (player.getPrettyName().isEmpty()) {
			player.setPrettyName(player.getShortName());
		}
		
		player.setPassword(playerDAO.encryptPassword(player));

		List<String> authorities = Lists.newArrayList();
    	authorities.add("ROLE_USER");
    	player.setRoles(authorities);

    	player.setActive(false);

    	playerDAO.save(player);

    	status.setComplete();

    	model.put("player", player);

    	return new ModelAndView("players/registrationComplete", model);
    }

}
