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
		
        // Check username is free
        if (userDAO.get(user.getUsername()) != null) {
            errors.rejectValue("username", "duplicate");        	
        }
   	
    	// Must supply a username
        if (!StringUtils.hasLength(user.getUsername())) {
            errors.rejectValue("username", "required");
        }
        
    	// username must contain only a-zA-Z0-9
        if (!user.getUsername().matches("[a-zA-z0-9]*")) {
            errors.rejectValue("username", "mustBeAlphanumeric");
        }
        
        // Must supply a password
        if (!StringUtils.hasLength(user.getPassword())) {
            errors.rejectValue("password", "required");
        }
    }
        
}
