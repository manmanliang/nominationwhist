package org.blim.whist.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.blim.whist.WhistConfig;
import org.blim.whist.dao.PlayerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ComputerNewAdminFormImpl implements ComputerNewAdminForm {

	private PlayerDAO playerDAO;
	private FormComputerPlayerValidator formComputerPlayerValidator;
	private WhistConfig whistConfig;
	
	public WhistConfig getWhistConfig() {
		return whistConfig;
	}

	@Autowired
	public void setWhistConfig(WhistConfig whistConfig) {
		this.whistConfig = whistConfig;
	}

	public FormComputerPlayerValidator getFormComputerPlayerValidator() {
		return formComputerPlayerValidator;
	}

	@Autowired
	public void setFormComputerPlayerValidator(
			FormComputerPlayerValidator formComputerPlayerValidator) {
		this.formComputerPlayerValidator = formComputerPlayerValidator;
	}

	public PlayerDAO getPlayerDAO() {
		return playerDAO;
	}

	@Autowired
	public void setPlayerDAO(PlayerDAO playerDAO) {
		this.playerDAO = playerDAO;
	}

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(formComputerPlayerValidator);
	}

	public ModelAndView setupForm() throws IOException {
    	Map<String, Object> model = new HashMap<String, Object>();

    	Player player = new BaseComputerPlayer();
    	
    	FormComputerPlayer formComputerPlayer = new FormComputerPlayer();
    	formComputerPlayer.setPlayer(player);
    	
    	Map<String, String> types = whistConfig.getComputerPlayerTypes();
    	
       	model.put("computerPlayer", formComputerPlayer);
       	model.put("types", types);

    	return new ModelAndView("players/computerAdminForm", model);
	}

    public ModelAndView processSubmit(@Valid @ModelAttribute("computerPlayer") FormComputerPlayer formComputerPlayer, BindingResult result, 
    								  SessionStatus status) {

    	Map<String, Object> model = new HashMap<String, Object>();
    	Player computerPlayer = null;

    	try {
    		Class<?> selectedClass = Class.forName(formComputerPlayer.getType());
    		computerPlayer = (Player) selectedClass.newInstance();
    	} catch (Exception e) {
            result.rejectValue("type", "cannotcreateclass");        	
    	}
    	    
    	if (result.hasErrors()) {
    		return new ModelAndView("players/computerAdminForm", model);
    	}

    	// TODO: Is copying necessary, or correct here
    	computerPlayer.setShortName(formComputerPlayer.getPlayer().getShortName());
    	computerPlayer.setPrettyName(formComputerPlayer.getPlayer().getPrettyName());
		
    	playerDAO.save(computerPlayer);

    	status.setComplete();

    	return new ModelAndView("redirect:/admin/players", model);
    }
}
