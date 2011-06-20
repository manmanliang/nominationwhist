package org.blim.whist.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class ComputerPlayerValidator implements Validator {

	private PlayerValidator playerValidator;
	
	public PlayerValidator getPlayerValidator() {
		return playerValidator;
	}

	@Autowired
	public void setPlayerValidator(PlayerValidator playerValidator) {
		this.playerValidator = playerValidator;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
	       return ComputerPlayer.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object target, Errors errors) {
    	
		Player computerPlayer = (Player) target;
		
		playerValidator.validate(computerPlayer, errors);
    }
        
}
