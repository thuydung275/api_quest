package com.quest.etna.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.quest.etna.model.User.UserRole;

public class JwtUserDetails implements org.springframework.security.core.userdetails.UserDetails {
	
	private static final long serialVersionUID = -7858869558953243875L;
	
	private Integer id;
	private String username;
    private String password;
    private UserRole userRole;


    public JwtUserDetails(User user) {
    	this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.userRole = user.getUserRole();
    }
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.userRole.toString()));
        return authorities;
	}

	public Integer getId() {
		return this.id;
	}

	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public String getPassword() {
		return this.password;
	}
	
	public UserRole getUserRole() {
		return this.userRole;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
