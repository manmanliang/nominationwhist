package org.blim.whist.game;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

public interface GameController {

	@RequestMapping("/")
	public abstract ModelAndView gameList(HttpServletRequest request,
			Principal user) throws IOException;

	@RequestMapping(value = "/create-game", method = RequestMethod.POST)
	public abstract ModelAndView createGame(Principal user);

	@RequestMapping(value = "/join-game", method = RequestMethod.POST)
	public abstract ModelAndView joinGame(HttpServletResponse response,
			@RequestParam("id") Long gameId, Principal user);

	@RequestMapping(value = "/start-game", method = RequestMethod.POST)
	public abstract ModelAndView startGame(HttpServletResponse response,
			@RequestParam("id") Long gameId, Principal user);

	@RequestMapping("/game")
	public abstract ModelAndView gameState(
			HttpServletRequest request,
			@RequestParam("id") Long gameId,
			@RequestParam(value = "ajaxtimeout", required = false) Boolean AJAXTimeout,
			Principal user) throws IOException;

	@RequestMapping("/games")
	public abstract void games(HttpServletResponse response, Principal user)
			throws IOException;

	@RequestMapping(value = "/gameStart", method = RequestMethod.POST)
	public abstract void gameStartCheck(HttpServletResponse response,
			HttpServletRequest request, Principal user) throws IOException;

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public abstract void update(HttpServletResponse response,
			HttpServletRequest request, Principal user) throws IOException;

	@RequestMapping(value = "/delete-game", method = RequestMethod.POST)
	public abstract ModelAndView deleteGame(HttpServletResponse response,
			@RequestParam("id") Long gameId, Principal user);

	@RequestMapping(value = "/bid", method = RequestMethod.POST)
	public abstract void bid(HttpServletResponse response,
			HttpServletRequest request, Principal user) throws IOException;

	@RequestMapping(value = "/trumps", method = RequestMethod.POST)
	public abstract void trumps(HttpServletResponse response,
			HttpServletRequest request, Principal user) throws IOException;

	@RequestMapping(value = "/playCard", method = RequestMethod.POST)
	public abstract void playCard(HttpServletResponse response,
			HttpServletRequest request, Principal user) throws IOException;

	@RequestMapping("/login")
	public abstract ModelAndView login(
			@RequestParam(value = "error", required = false) Boolean error,
			HttpServletRequest request, Principal user) throws IOException;

	@RequestMapping("/error")
	public abstract ModelAndView error(HttpServletRequest request)
			throws IOException;

	@RequestMapping("/access-denied")
	public abstract ModelAndView accessDenied(HttpServletRequest request)
			throws IOException;

	@RequestMapping("/information")
	public abstract ModelAndView gameInformation(HttpServletRequest request)
			throws IOException;

	@RequestMapping("/footer")
	public abstract ModelAndView footer(HttpServletRequest request,
			Principal principal) throws IOException;

}