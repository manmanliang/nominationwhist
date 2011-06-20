package org.blim.whist.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
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
public class HumanEditAdminFormImpl implements HumanEditAdminForm {

	private PasswordEncryptor passwordEncryptor;
	private HumanPlayerValidator humanPlayerValidator;
	private PlayerDAO playerDAO;

	public PlayerDAO getPlayerDAO() {
		return playerDAO;
	}

	@Autowired
	public void setPlayerDAO(PlayerDAO playerDAO) {
		this.playerDAO = playerDAO;
	}

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

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("user.username");
		dataBinder.setValidator(humanPlayerValidator);
	}

	public ModelAndView setupForm(@PathVariable("username") String username, HttpSession session) throws IOException {
    	Map<String, Object> model = new HashMap<String, Object>();

    	HumanPlayer humanPlayer = playerDAO.getHumanPlayer(username);
    	
       	model.put("player", humanPlayer);

    	return new ModelAndView("players/humanAdminForm", model);
	}

    public ModelAndView processSubmit(@Valid @ModelAttribute("player") HumanPlayer humanPlayer, BindingResult result, 
    								  SessionStatus status, @PathVariable("username") String username, 
    								  HttpSession session) {    	
    	playerDAO.update(humanPlayer);
    	
    	status.setComplete();

    	return new ModelAndView("redirect:/admin/players");
    }

}