package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.PasswordNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsServicesImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }

        if (user.get().getPassword() == null || user.get().getPassword().isEmpty()) {
            throw new PasswordNotFoundException("Wrong Password");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.get().getPassword())
                .roles(user.get().getRole())
                .build();
    }
}
