package org.blim.whist.player;

import org.blim.whist.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class UserValidator implements Validator {

	private UserDAO userDAO;
	
	public UserDAO getUserDAO() {
		return userDAO;
	}

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
	       return User.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object target, Errors errors) {
    	
		User user = (User) target;

    	// Must supply a username
        if (!StringUtils.hasLength(user.getUsername())) {
            errors.rejectValue("username", "required");
        }
        
    	// Must not be new
        if (user.getUsername().equals("new")) {
            errors.rejectValue("username", "notallowed");
        }
        
        User dbUser = userDAO.get(user.getUsername());

        // Check username is free
        if (user.isNew() && dbUser != null) {
            errors.rejectValue("username", "duplicate");        	
        }

    	// username must contain only a-zA-Z0-9
        if (!user.getUsername().matches("[a-zA-z0-9]*")) {
            errors.rejectValue("username", "mustBeAlphanumeric");
        }

        // Must supply a password
        if (!StringUtils.hasLength(user.getPassword())) {
        	if (dbUser != null) {
               	user.setPassword(dbUser.getPassword());
        	} else {
                errors.rejectValue("password", "required");
        	}
        }
        
        userDAO.evict(dbUser);
    }
        
}
