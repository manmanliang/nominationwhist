package org.blim.whist.player;

import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.blim.whist.BaseEntity;
import org.hibernate.annotations.CollectionOfElements;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.common.collect.Lists;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails, Cloneable {

	private String username;
	private String password;
	private Boolean active;
	private List<String> roles = Lists.newArrayList();

	public User() {}
	
	protected User(User another) {
		super(another);
		
		username = another.username;
		password = another.password;
		active = another.active;
		roles.addAll(another.roles);
	}

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
	
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

	@CollectionOfElements
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
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
		if (active != null) {
			return active;
		} else {
			return false;
		}
	}
	
	@Transient
	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = Lists.newArrayList();
		
		for (String role : roles) {
			authorities.add(new GrantedAuthorityImpl(role));
		}
		
		return authorities;
	}
	
	@Override
	public Object clone() {
		return new User(this);
	}

}
