package org.blim.whist.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.blim.whist.dao.HumanPlayerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;

@Controller
public class NewFormImpl implements NewForm {

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
        if (humanPlayerDAO.get(user.getUsername()) != null) {
            result.rejectValue("user.username", "duplicate");        	
        }

    	if (result.hasErrors()) {
    		return new ModelAndView("players/form", model);
    	}

    	// Default some stuff
		if (humanPlayer.getShortName().isEmpty()) {
			humanPlayer.setShortName(user.getUsername());
		}
    	
		if (humanPlayer.getPrettyName().isEmpty()) {
			humanPlayer.setPrettyName(humanPlayer.getShortName());
		}
		
		user.setPassword(passwordEncryptor.encryptPassword(user));

		List<String> authorities = Lists.newArrayList();
    	authorities.add("ROLE_USER");
    	user.setRoles(authorities);

    	user.setActive(false);

    	humanPlayerDAO.save(humanPlayer);

    	status.setComplete();

    	model.put("player", humanPlayer);

    	return new ModelAndView("players/registrationComplete", model);
    }

}
