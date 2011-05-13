package org.blim.whist.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.blim.whist.dao.HumanPlayerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;

@Controller
public class HumanNewFormImpl implements HumanNewForm {

	private HumanPlayerDAO humanPlayerDAO;
	private PasswordEncryptor passwordEncryptor;
	private HumanPlayerValidator humanPlayerValidator;

	public HumanPlayerValidator getHumanPlayerValidator() {
		return humanPlayerValidator;
	}

	@Autowired
	public void setHumanPlayerValidator(HumanPlayerValidator humanPlayerValidator) {
		this.humanPlayerValidator = humanPlayerValidator;
	}

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

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("user.roles", "user.active");
		dataBinder.setValidator(humanPlayerValidator);
	}

	public ModelAndView setupForm() throws IOException {
    	Map<String, Object> model = new HashMap<String, Object>();

    	HumanPlayer humanPlayer = new HumanPlayer();
    	
       	model.put("player", humanPlayer);

    	return new ModelAndView("players/form", model);
	}

    public ModelAndView processSubmit(@Valid @ModelAttribute("player") HumanPlayer humanPlayer, BindingResult result, 
    								  SessionStatus status) {
    	Map<String, Object> model = new HashMap<String, Object>();

    	if (result.hasErrors()) {
    		return new ModelAndView("players/form", model);
    	}

    	User user = humanPlayer.getUser();

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
