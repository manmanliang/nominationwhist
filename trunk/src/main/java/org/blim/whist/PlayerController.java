package org.blim.whist;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;

@Controller
@RequestMapping("/players/new")
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

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView setupForm() throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		
		User newUser = new User();
		
		model.put("user", newUser);
		
		return new ModelAndView("players/form", model);
	}

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(@ModelAttribute User user, BindingResult result, SessionStatus status) {
    	user.validate(result);
    	
    	if (result.hasErrors()) {
    		return "players/form";
    	} else {
    		user.setEnabled(false);
    		List<String> authorities = Lists.newArrayList();
    		authorities.add("ROLE_USER");
    		user.setAuthorities(authorities);
    		
    		Session session = sessionFactory.getCurrentSession();
    		session.save(user);

    		status.setComplete();
    		
    		return "redirect:/players/new";
    	}
    }
}
