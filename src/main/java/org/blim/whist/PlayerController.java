package org.blim.whist;

import java.io.IOException;

import org.hibernate.SessionFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

public interface PlayerController {

	public abstract SessionFactory getSessionFactory();

	public abstract void setSessionFactory(SessionFactory sessionFactory);

	public abstract PasswordEncoder getPasswordEncoder();

	public abstract void setPasswordEncoder(PasswordEncoder passwordEncoder);

	public abstract SaltSource getSaltSource();

	public abstract void setSaltSource(SaltSource saltSource);

	@RequestMapping(value = "/players/register", method = RequestMethod.GET)
	public abstract ModelAndView setupRegisterPlayerForm() throws IOException;

	@RequestMapping(value = "/players/register", method = RequestMethod.PUT)
	public abstract ModelAndView processRegisterPlayerSubmit(
			@ModelAttribute("player") Player player, BindingResult result,
			SessionStatus status);

	@PreAuthorize("#username == principal.username")
	@RequestMapping(value = "/players/{username}")
	public abstract ModelAndView showPlayer(@PathVariable String username);

	@PreAuthorize("#username == principal.username")
	@RequestMapping(value = "/players/{username}/edit", method = RequestMethod.GET)
	public abstract ModelAndView setupEditPlayerForm(
			@PathVariable String username) throws IOException;

	@PreAuthorize("#player.username == principal.username")
	@RequestMapping(value = "/players/{username}/edit", method = RequestMethod.PUT)
	public abstract ModelAndView processEditPlayerSubmit(
			@ModelAttribute("player") Player player, BindingResult result,
			SessionStatus status);

	@RequestMapping(value = "/admin/players")
	public abstract ModelAndView listPlayers();

	@RequestMapping(value = "/admin/players/new", method = RequestMethod.GET)
	public abstract ModelAndView setupNewPlayerForm() throws IOException;

	@RequestMapping(value = "/admin/players/new", method = RequestMethod.PUT)
	public abstract ModelAndView processNewPlayerSubmit(
			@ModelAttribute("player") Player player, BindingResult result,
			SessionStatus status);

	@RequestMapping(value = "/admin/players/{username}")
	public abstract ModelAndView showPlayerAdmin(@PathVariable String username);

	@RequestMapping(value = "/admin/players/{username}/edit", method = RequestMethod.GET)
	public abstract ModelAndView setupAdminEditPlayerForm(
			@PathVariable String username) throws IOException;

	@RequestMapping(value = "/admin/players/{username}/edit", method = RequestMethod.PUT)
	public abstract ModelAndView processAdminEditPlayerSubmit(
			@ModelAttribute("player") Player player, BindingResult result,
			SessionStatus status);

	@RequestMapping(value = "/admin/players/{username}/delete")
	public abstract ModelAndView deletePlayer(@PathVariable String username);

}