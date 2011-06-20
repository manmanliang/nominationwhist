package org.blim.whist.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.blim.whist.dao.PlayerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ComputerEditAdminFormImpl implements ComputerEditAdminForm {

	private PlayerDAO playerDAO;
	private FormComputerPlayerValidator formComputerPlayerValidator;

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

	public ModelAndView setupForm(@PathVariable("id") Long id) throws IOException {
    	Map<String, Object> model = new HashMap<String, Object>();

    	Player player = playerDAO.get(id);

       	model.put("computerPlayer", player);

    	return new ModelAndView("players/computerAdminForm", model);
	}

    public ModelAndView processSubmit(@Valid @ModelAttribute("computerPlayer") Player player, BindingResult result, 
    								  SessionStatus status) {

    	Map<String, Object> model = new HashMap<String, Object>();
    	    
    	if (result.hasErrors()) {
    		return new ModelAndView("players/computerAdminForm", model);
    	}
		
    	playerDAO.save(player);

    	status.setComplete();

    	return new ModelAndView("redirect:/admin/players", model);
    }
}
