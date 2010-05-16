package org.blim.whist;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;

@Controller
@SessionAttributes(types = User.class)
public class PlayerController {
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

    @Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
    @RequestMapping(value = "/players")
    public ModelAndView listPlayers() {
		Map<String, Object> model = new HashMap<String, Object>();

		Session session = sessionFactory.getCurrentSession();
		
		List<User> players = session.createQuery("from User").list();
		model.put("players", players);
		
		return new ModelAndView("players/list", model);
    }
    
    @RequestMapping(value = "/players/new", method = RequestMethod.GET)
	public ModelAndView setupNewPlayerForm() throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		User newUser = new User();
		
		model.put("user", newUser);
		
		return new ModelAndView("players/newform", model);
	}

    @Transactional
    @RequestMapping(value = "/players/new", method = RequestMethod.POST)
    public ModelAndView processNewPlayerSubmit(@ModelAttribute User user, BindingResult result, SessionStatus status) {
    	user.validate(result);
    	
    	if (result.hasErrors()) {
    		return new ModelAndView("players/newform");
    	} else {
    		user.setEnabled(true);
    		List<String> authorities = Lists.newArrayList();
    		authorities.add("ROLE_USER");
    		user.setAuthorities(authorities);
    		
    		Session session = sessionFactory.getCurrentSession();
    		session.save(user);

    		status.setComplete();
    		
    		return new ModelAndView("redirect:/players");
    	}
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == principal.username")
    @RequestMapping(value = "/players/{username}")
    public ModelAndView showPlayer(@PathVariable String username) {
		Map<String, Object> model = new HashMap<String, Object>();    	
    	User user = new User();
    	
		Session session = sessionFactory.getCurrentSession();
		session.load(user, username);
		
		model.put("player", user);
		
		return new ModelAndView("players/show", model);
    }
    
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == principal.username")
    @RequestMapping(value = "/players/{username}/edit", method = RequestMethod.GET)
	public ModelAndView setupEditPlayerForm(@PathVariable String username, Principal principal) throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		User editingUser = new User();
		
		Session session = sessionFactory.getCurrentSession();
		session.load(editingUser, username);
		
		model.put("player", editingUser);
				
		return new ModelAndView("players/editform", model);
	}

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or #user.username == principal.username")
    @RequestMapping(value = "/players/{username}/edit", method = RequestMethod.PUT)
    public ModelAndView processEditPlayerSubmit(@ModelAttribute User user, BindingResult result, 
    											SessionStatus status, Principal principal) {
    	Session session = sessionFactory.getCurrentSession();

    	// TODO: Maybe have password change as separate form but
    	// for now I'll just copy across the old one
    	if (user.getPassword().isEmpty()) {
    		User oldUser = new User();
    		session.load(oldUser, user.getUsername());
    		session.evict(oldUser);
    		user.setPassword(oldUser.getPassword());
       	}
    	
    	session.update(user);

    	status.setComplete();

    	return new ModelAndView("redirect:/players/" + user.getUsername());
    }

    @Transactional
    @RequestMapping(value = "/players/{username}/delete")
    public ModelAndView deletePlayer(@PathVariable String username) {
    	User user = new User();
    	
		Session session = sessionFactory.getCurrentSession();
		session.load(user, username);
		session.delete(user);
		
		return new ModelAndView("redirect:/players");
    }
}
