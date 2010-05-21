package org.blim.whist;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
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
	private PasswordEncoder passwordEncoder;
	private SaltSource saltSource;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	@Autowired
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public SaltSource getSaltSource() {
		return saltSource;
	}

	@Autowired
	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
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
    
    @RequestMapping(value = "/players/register", method = RequestMethod.GET)
	public ModelAndView setupRegisterPlayerForm() throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		User newUser = new User();
		
		model.put("player", newUser);
		
		return new ModelAndView("players/newForm", model);
	}

    @Transactional
    @RequestMapping(value = "/players/register", method = RequestMethod.POST)
    public ModelAndView processRegisterPlayerSubmit(@ModelAttribute("player") User player, BindingResult result, SessionStatus status) {
    	player.validate(result);
    	
    	if (result.hasErrors()) {
    		return new ModelAndView("players/newForm");
    	} else {
    		Map<String, Object> model = new HashMap<String, Object>();

    		player.setActive(false);
    		List<String> authorities = Lists.newArrayList();
    		authorities.add("ROLE_USER");
    		player.setRoles(authorities);
    		
    		player.setPassword(encryptPassword(player));
    		
    	    sessionFactory.getCurrentSession().save(player);

    		status.setComplete();
    		
    		model.put("user", player);
    		
    		return new ModelAndView("players/registrationComplete", model);
    	}
    }

    @RequestMapping(value = "/players/new", method = RequestMethod.GET)
	public ModelAndView setupNewPlayerForm() throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		User newUser = new User();
		
		model.put("player", newUser);
		
		return new ModelAndView("players/newForm", model);
	}

    @Transactional
    @RequestMapping(value = "/players/new", method = RequestMethod.POST)
    public ModelAndView processNewPlayerSubmit(@ModelAttribute("player") User player, BindingResult result, SessionStatus status) {
    	player.validate(result);
    	
    	if (result.hasErrors()) {
    		return new ModelAndView("players/newForm");
    	} else {
    		List<String> authorities = Lists.newArrayList();
    		authorities.add("ROLE_USER");
    		player.setRoles(authorities);
    		
    		player.setPassword(encryptPassword(player));
    		
    	    sessionFactory.getCurrentSession().save(player);

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
    	
		sessionFactory.getCurrentSession().load(user, username);
		
		model.put("player", user);
		
		return new ModelAndView("players/show", model);
    }
    
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN') or #username == principal.username")
    @RequestMapping(value = "/players/{username}/edit", method = RequestMethod.GET)
	public ModelAndView setupEditPlayerForm(@PathVariable String username) throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		User editingUser = new User();
		
		sessionFactory.getCurrentSession().load(editingUser, username);
		
		model.put("player", editingUser);
				
		return new ModelAndView("players/editForm", model);
	}

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or #player.username == principal.username")
    @RequestMapping(value = "/players/{username}/edit", method = RequestMethod.PUT)
    public ModelAndView processEditPlayerSubmit(@ModelAttribute("player") User player, BindingResult result, 
    											SessionStatus status) {
    	Session session = sessionFactory.getCurrentSession();

    	if (player.getPassword().isEmpty()) {
    		User oldUser = new User();
    		session.load(oldUser, player.getUsername());
    		session.evict(oldUser);
    		player.setPassword(oldUser.getPassword());
       	} else {
       		player.setPassword(encryptPassword(player));
       	}
    	
    	session.update(player);

    	status.setComplete();

    	return new ModelAndView("redirect:/players/" + player.getUsername());
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

    private String encryptPassword(User user) {
	    Object salt = saltSource.getSalt(user);  
	    
	    return passwordEncoder.encodePassword(user.getPassword(), salt);
    }

}
