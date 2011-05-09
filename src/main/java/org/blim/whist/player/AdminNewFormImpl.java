package org.blim.whist.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.blim.whist.dao.HumanPlayerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AdminNewFormImpl implements AdminNewForm {

	private HumanPlayerDAO humanPlayerDAO;
	private PasswordEncryptor passwordEncryptor;

	public PasswordEncryptor getPasswordEncryptor() {
		return passwordEncryptor;
	}

	@Autowired
	public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
		this.passwordEncryptor = passwordEncryptor;
	}

	public HumanPlayerDAO getHumanPlayerDAO() {
		return humanPlayerDAO;
	}

	@Autowired
	public void setHumanPlayerDAO(HumanPlayerDAO humanPlayerDAO) {
		this.humanPlayerDAO = humanPlayerDAO;
	}

	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("username", "roles", "active");
	}

	public ModelAndView setupForm() throws IOException {
    	Map<String, Object> model = new HashMap<String, Object>();

    	HumanPlayer humanPlayer = new HumanPlayer();
    	
       	model.put("player", humanPlayer);

    	return new ModelAndView("players/form", model);
	}

    public ModelAndView processSubmit(@ModelAttribute("player") HumanPlayer humanPlayer, BindingResult result, 
    								  SessionStatus status) {

    	Map<String, Object> model = new HashMap<String, Object>();

    	User user = humanPlayer.getUser();

    	new UserValidator().validate(user, result);

        // Check username is free
    	// TODO: This should be in validate but it doesn't have a database session
        if (humanPlayerDAO.get(user.getUsername()) != null) {
            result.rejectValue("user.username", "duplicate");        	
        }
   	
    	if (result.hasErrors()) {
    		model.put("new", 1);
    		return new ModelAndView("/players/form", model);
    	}

		if (humanPlayer.getPrettyName().isEmpty()) {
			humanPlayer.setPrettyName(user.getUsername());
		}
		
		if (humanPlayer.getShortName().isEmpty()) {
			if (humanPlayer.getPrettyName().length() > 6) {
				humanPlayer.setShortName(humanPlayer.getPrettyName().substring(0, 7));
			} else {
				humanPlayer.setShortName(humanPlayer.getPrettyName());
			}
		}
		
		user.setPassword(passwordEncryptor.encryptPassword(user));

    	humanPlayerDAO.save(humanPlayer);

    	status.setComplete();

    	return new ModelAndView("redirect:/admin/players", model);
    }
}
