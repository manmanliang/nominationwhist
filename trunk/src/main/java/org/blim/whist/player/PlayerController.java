package org.blim.whist.player;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

public interface PlayerController {

	@PreAuthorize("#username == principal.username")
	@RequestMapping(value = "/players/{username}")
	public abstract ModelAndView showPlayer(@PathVariable("username") String username);

	@RequestMapping(value = "/admin/players")
	public abstract ModelAndView listPlayers();

	@RequestMapping(value = "/admin/players/{username}")
	public abstract ModelAndView showPlayerAdmin(@PathVariable("username") String username);

	@RequestMapping(value = "/admin/players/{username}/delete")
	public abstract ModelAndView deletePlayer(@PathVariable("username") String username);
	
}