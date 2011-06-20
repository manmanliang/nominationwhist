package org.blim.whist.player;

import java.io.IOException;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@SessionAttributes(types = FormComputerPlayer.class)
@RequestMapping("/admin/computer/new")
public interface ComputerNewAdminForm {

	@RequestMapping(method = RequestMethod.GET)
	public abstract ModelAndView setupForm() throws IOException;

	@RequestMapping(method = RequestMethod.PUT)
	public abstract ModelAndView processSubmit(FormComputerPlayer formComputerPlayer,
			BindingResult result, SessionStatus status);

}