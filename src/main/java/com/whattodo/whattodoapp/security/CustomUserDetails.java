package com.whattodo.whattodoapp.security;

import com.whattodo.whattodoapp.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;



 // Custom implementation of UserDetails that wraps User entity.
 // Provides user details to the Spring Security framework during authentication and authorization.
@Getter
public class CustomUserDetails implements UserDetails {
     private final User user;


    // Wraps the User entity
    public CustomUserDetails(User user) {
        this.user = user;
    }

     /**
     * Returns the roles of the user as a list of GrantedAuthority objects.
     * Each role is prefixed with "ROLE_" as required by Spring Security.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    @Override public String getPassword() { return user.getPassword(); }
    @Override public String getUsername() { return user.getUsername(); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
