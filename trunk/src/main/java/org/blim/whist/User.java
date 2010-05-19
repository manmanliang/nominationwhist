package org.blim.whist;

import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.google.common.collect.Lists;

@Entity
@Table(name="users")
public class User implements UserDetails {

	private String username;
	private String password;
	private Boolean enabled;
	private List<String> roles = Lists.newArrayList();

	@Id
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@CollectionOfElements
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public void validate(Errors errors) {
		if (!StringUtils.hasLength(username)) {
			errors.rejectValue("username", "required", "required");
		}
		if (!StringUtils.hasLength(password)) {
			errors.rejectValue("password", "required", "required");
		}
	}

	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Transient
	public boolean isEnabled() {
		return enabled;
	}
	
	@Transient
	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = Lists.newArrayList();
		
		for (String role : roles) {
			authorities.add(new GrantedAuthorityImpl(role));
		}
		
		return authorities;
	}
	
}
