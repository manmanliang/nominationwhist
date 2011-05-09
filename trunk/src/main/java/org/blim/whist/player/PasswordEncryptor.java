package org.blim.whist.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncryptor {

	private PasswordEncoder passwordEncoder;
	private SaltSource saltSource;
	
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
	
    public String encryptPassword(User user) {
	    Object salt = saltSource.getSalt(user);  
	    
	    return passwordEncoder.encodePassword(user.getPassword(), salt);
    }

}
