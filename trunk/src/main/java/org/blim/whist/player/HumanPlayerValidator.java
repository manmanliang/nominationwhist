package org.blim.whist.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class HumanPlayerValidator implements Validator {

	private UserValidator userValidator;
	private PlayerValidator playerValidator;
	
	public PlayerValidator getPlayerValidator() {
		return playerValidator;
	}

	@Autowired
	public void setPlayerValidator(PlayerValidator playerValidator) {
		this.playerValidator = playerValidator;
	}

	public UserValidator getUserValidator() {
		return userValidator;
	}

	@Autowired
	public void setUserValidator(UserValidator userValidator) {
		this.userValidator = userValidator;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
	       return HumanPlayer.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object target, Errors errors) {
    	
		HumanPlayer humanPlayer = (HumanPlayer) target;
		
		playerValidator.validate(humanPlayer, errors);

		errors.pushNestedPath("user");
		
		userValidator.validate(humanPlayer.getUser(), errors);
		
		errors.popNestedPath();
    }
        
}
