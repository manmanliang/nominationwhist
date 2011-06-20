package org.blim.whist.player;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@SessionAttributes(types = HumanPlayer.class)
@RequestMapping("/player/{username}/edit")
public interface HumanEditForm {

	@InitBinder
	public abstract void initBinder(WebDataBinder dataBinder);

	@PreAuthorize("#username == principal.username")
	@RequestMapping(method = RequestMethod.GET)
	public abstract ModelAndView setupForm(@PathVariable("username") String username,
			HttpSession session)
			throws IOException;

	@PreAuthorize("#username == principal.username")
	@RequestMapping(method = RequestMethod.PUT)
	public abstract ModelAndView processSubmit(@Valid @ModelAttribute("player") HumanPlayer humanPlayer, 
			  BindingResult result, SessionStatus status, 
			  @PathVariable("username") String username, 
			  HttpSession session);

}