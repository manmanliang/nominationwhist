package org.blim.whist.player;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

public class PlayerValidator {

    public void validate(Player player, Errors errors) {
    	
    	// Must supply a username
        if (!StringUtils.hasLength(player.getUsername())) {
            errors.rejectValue("username", "required");
        }
        
        // Must supply a password
        if (!StringUtils.hasLength(player.getPassword())) {
            errors.rejectValue("password", "required");
        }
    }
        
}
