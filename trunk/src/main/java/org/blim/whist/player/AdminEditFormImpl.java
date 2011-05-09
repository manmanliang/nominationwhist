package org.blim.whist.player;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.blim.whist.dao.HumanPlayerDAO;
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

	private HumanPlayerDAO humanPlayerDAO;
	private PasswordEncryptor passwordEncryptor;
	
	public HumanPlayerDAO getHumanPlayerDAO() {
		return humanPlayerDAO;
	}

	@Autowired
	public void setHumanPlayerDAO(HumanPlayerDAO humanPlayerDAO) {
		this.humanPlayerDAO = humanPlayerDAO;
	}

	public PasswordEncryptor getPasswordEncryptor() {
		return passwordEncryptor;
	}

	@Autowired
	public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
		this.passwordEncryptor = passwordEncryptor;
	}

	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("username");
	}

	public ModelAndView setupForm(@PathVariable("username") String username, HttpSession session) throws IOException {
    	Map<String, Object> model = new HashMap<String, Object>();

    	HumanPlayer humanPlayer = humanPlayerDAO.get(username);
    	
       	HumanPlayer currPlayer = (HumanPlayer) humanPlayer.clone();
       	
       	model.put("player", humanPlayer);
       	
    	session.setAttribute("currPlayer", currPlayer);

    	return new ModelAndView("players/form", model);
	}

    public ModelAndView processSubmit(@ModelAttribute("player") HumanPlayer humanPlayer, BindingResult result, 
    								  SessionStatus status, @PathVariable("username") String username, 
    								  HttpSession session) {
    	HumanPlayer currUser = (HumanPlayer) session.getAttribute("currPlayer");
    	session.removeAttribute("currPlayer");
    	
    	User user = humanPlayer.getUser();
    	
		if (user.getPassword().isEmpty()) {
    		user.setPassword(currUser.getUser().getPassword());
       	} else {
       		user.setPassword(passwordEncryptor.encryptPassword(user));
       	}

    	user.setUsername(username);
    	
    	humanPlayerDAO.update(humanPlayer);
    	
    	status.setComplete();

    	return new ModelAndView("redirect:/admin/players/" + user.getUsername());
    }

}