package org.blim.whist.player;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

public class UserValidator {

    public void validate(User user, Errors errors) {
    	
    	// Must supply a username
        if (!StringUtils.hasLength(user.getUsername())) {
            errors.rejectValue("user.username", "required");
        }
        
    	// username must contain only a-zA-Z0-9
        if (!user.getUsername().matches("[a-zA-z0-9]*")) {
            errors.rejectValue("user.username", "mustBeAlphanumeric");
        }
        
        // Must supply a password
        if (!StringUtils.hasLength(user.getPassword())) {
            errors.rejectValue("user.password", "required");
        }
    }
        
}
