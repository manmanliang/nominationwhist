package org.blim.whist.player;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

public interface PlayerController {

	@PreAuthorize("#username == principal.username")
	@RequestMapping(value = "/player/{username}")
	public abstract ModelAndView showHumanPlayer(@PathVariable("username") String username);

	@RequestMapping(value = "/admin/players")
	public abstract ModelAndView listPlayers();

	@RequestMapping(value = "/admin/human/{username}")
	public abstract ModelAndView showHumanPlayerAdmin(@PathVariable("username") String username);

	@RequestMapping(value = "/admin/computer/{id}")
	public abstract ModelAndView showComputerPlayerAdmin(@PathVariable("id") Long id);

	@RequestMapping(value = "/admin/player/{id}/delete")
	public abstract ModelAndView deletePlayer(@PathVariable("id") Long id);
	
}