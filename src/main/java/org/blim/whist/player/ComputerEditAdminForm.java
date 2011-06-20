package org.blim.whist.player;

import java.io.IOException;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@SessionAttributes(types = FormComputerPlayer.class)
@RequestMapping("/admin/computer/{id}/edit")
public interface ComputerEditAdminForm {

	@RequestMapping(method = RequestMethod.GET)
	public abstract ModelAndView setupForm(Long id) throws IOException;

	@RequestMapping(method = RequestMethod.PUT)
	public abstract ModelAndView processSubmit(Player player,
			BindingResult result, SessionStatus status);

}