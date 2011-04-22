package org.blim.whist;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;

@Controller
@SessionAttributes("player")
public class PlayerControllerImpl implements PlayerController {
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

	public ModelAndView setupRegisterPlayerForm() throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		Player player = new Player();
		
		model.put("player", player);
		
		return new ModelAndView("players/form", model);
	}

    @Transactional
    public ModelAndView processRegisterPlayerSubmit(@ModelAttribute("player") Player player, BindingResult result, SessionStatus status) {
    	player.validate(result);
    	
    	if (result.hasErrors()) {
    		return new ModelAndView("players/form");
    	}

    	if (player.getRoles().size() != 0 ||
        	player.getActive() != null) {
    		return new ModelAndView("redirect:/access-denied");
        }
        	
    	Map<String, Object> model = new HashMap<String, Object>();

    	player = setupBasePlayer(player);

    	List<String> authorities = Lists.newArrayList();
    	authorities.add("ROLE_USER");
    	player.setRoles(authorities);

    	player.setActive(false);

    	sessionFactory.getCurrentSession().save(player);

    	status.setComplete();

    	model.put("player", player);

    	return new ModelAndView("players/registrationComplete", model);
    }

    @Transactional(readOnly = true)
    public ModelAndView showPlayer(@PathVariable String username) {
		Map<String, Object> model = new HashMap<String, Object>();    	
    	Player player = new Player();
    	
		sessionFactory.getCurrentSession().load(player, username);
		
		model.put("player", player);
		
		return new ModelAndView("players/show", model);
    }

    @Transactional(readOnly = true)
	public ModelAndView setupEditPlayerForm(@PathVariable String username) throws IOException {
    	Map<String, Object> model = new HashMap<String, Object>();

    	Player player = new Player();

    	sessionFactory.getCurrentSession().load(player, username);

    	model.put("player", player);

    	return new ModelAndView("players/form", model);
	}

    @Transactional
    public ModelAndView processEditPlayerSubmit(@ModelAttribute("player") Player player, BindingResult result, 
    											SessionStatus status) {
    	Session session = sessionFactory.getCurrentSession();

    	if (player.getRoles().size() != 0 ||
    		player.getActive() != null) {
        	return new ModelAndView("redirect:/access-denied");
    	}

    	if (player.getPassword().isEmpty()) {
    		Player prevPlayer = new Player();
    		session.load(prevPlayer, player.getUsername());
    		session.evict(prevPlayer);
    		player.setPassword(prevPlayer.getPassword());
       	} else {
       		player.setPassword(encryptPassword(player));
       	}
    	
    	session.update(player);

    	status.setComplete();

    	return new ModelAndView("redirect:/players/" + player.getUsername());
    }

    @Transactional(readOnly = true)
	@SuppressWarnings("unchecked")
    public ModelAndView listPlayers() {
		Map<String, Object> model = new HashMap<String, Object>();

		Session session = sessionFactory.getCurrentSession();
		
		List<Player> players = session.createQuery("from Player").list();
		model.put("players", players);
		
		return new ModelAndView("players/list", model);
    }
    
	public ModelAndView setupNewPlayerForm() throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		Player player = new Player();
		
		model.put("player", player);
		
		return new ModelAndView("players/adminForm", model);
	}

    @Transactional
    public ModelAndView processNewPlayerSubmit(@ModelAttribute("player") Player player, BindingResult result, SessionStatus status) {
    	player.validate(result);
    	
    	if (result.hasErrors()) {
    		return new ModelAndView("/admin/players/adminForm");
    	} else {
    		player = setupBasePlayer(player);
    		
    	    sessionFactory.getCurrentSession().save(player);

    		status.setComplete();
    		
    		return new ModelAndView("redirect:/admin/players");
    	}
    }

    @Transactional(readOnly = true)
    public ModelAndView showPlayerAdmin(@PathVariable String username) {
		Map<String, Object> model = new HashMap<String, Object>();    	
    	Player player = new Player();
    	
		sessionFactory.getCurrentSession().load(player, username);
		
		model.put("player", player);
		
		return new ModelAndView("players/show", model);
    }

    @Transactional(readOnly = true)
	public ModelAndView setupAdminEditPlayerForm(@PathVariable String username) throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		Player player = new Player();
		
		sessionFactory.getCurrentSession().load(player, username);
		
		model.put("player", player);
				
		return new ModelAndView("players/adminForm", model);
	}

    @Transactional
    public ModelAndView processAdminEditPlayerSubmit(@ModelAttribute("player") Player player, BindingResult result, 
    											SessionStatus status) {
    	Session session = sessionFactory.getCurrentSession();

    	if (player.getPassword().isEmpty()) {
    		Player prevPlayer = new Player();
    		session.load(prevPlayer, player.getUsername());
    		session.evict(prevPlayer);
    		player.setPassword(prevPlayer.getPassword());
       	} else {
       		player.setPassword(encryptPassword(player));
       	}
    	
    	session.update(player);

    	status.setComplete();

    	return new ModelAndView("redirect:/admin/players/" + player.getUsername());
    }

    @Transactional
    public ModelAndView deletePlayer(@PathVariable String username) {
    	Player player = new Player();
    	
		Session session = sessionFactory.getCurrentSession();
		session.load(player, username);
		session.delete(player);
		
		return new ModelAndView("redirect:/admin/players");
    }

    private String encryptPassword(User user) {
	    Object salt = saltSource.getSalt(user);  
	    
	    return passwordEncoder.encodePassword(user.getPassword(), salt);
    }

    private Player setupBasePlayer(Player player) {
		if (player.getPrettyName().isEmpty()) {
			player.setPrettyName(player.getUsername());
		}
		
		if (player.getShortName().isEmpty()) {
			if (player.getPrettyName().length() > 6) {
				player.setShortName(player.getPrettyName().substring(0, 7));
			} else {
				player.setShortName(player.getPrettyName());
			}
		}
		
		player.setPassword(encryptPassword(player));
		
		return player;
    }

}
