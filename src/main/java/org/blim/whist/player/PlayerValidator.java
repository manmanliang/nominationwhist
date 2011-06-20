package org.blim.whist.player;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class PlayerValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
	       return Player.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object target, Errors errors) {
    	
		Player player = (Player) target;

    	// Must supply a shortName
        if (!StringUtils.hasLength(player.getShortName())) {
            errors.rejectValue("shortName", "required");
        }

    	// If no PrettyName user ShortName
		if (player.getPrettyName().isEmpty()) {
			player.setPrettyName(player.getShortName());
		}
    }
        
}
