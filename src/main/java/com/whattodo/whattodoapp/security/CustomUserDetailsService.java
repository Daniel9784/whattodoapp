package com.whattodo.whattodoapp.security;

import com.whattodo.whattodoapp.model.User.User;
import com.whattodo.whattodoapp.model.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// Custom implementation of UserDetailsService used by Spring Security
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    // Loads user data from the database based on the provided username.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Wrap the user in a UserDetails implementation and return
        return new CustomUserDetails(user);
    }
}
