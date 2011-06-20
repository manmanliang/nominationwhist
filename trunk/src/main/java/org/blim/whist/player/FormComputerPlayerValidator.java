package org.blim.whist.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class FormComputerPlayerValidator implements Validator {

	private ComputerPlayerValidator computerPlayerValidator;
	
	public ComputerPlayerValidator getComputerPlayerValidator() {
		return computerPlayerValidator;
	}

	@Autowired
	public void setComputerPlayerValidator(
			ComputerPlayerValidator computerPlayerValidator) {
		this.computerPlayerValidator = computerPlayerValidator;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
	       return FormComputerPlayer.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object target, Errors errors) {
    	
		FormComputerPlayer formComputerPlayer = (FormComputerPlayer) target;
		
		errors.pushNestedPath("player");
		
		computerPlayerValidator.validate(formComputerPlayer.getPlayer(), errors);
		
		errors.popNestedPath();
    }

}
