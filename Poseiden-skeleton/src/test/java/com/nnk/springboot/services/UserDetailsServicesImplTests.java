package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exceptions.PasswordNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@SpringBootTest
public class UserDetailsServicesImplTests {
    @Autowired
    private UserDetailsServicesImpl userDetailsServices;
    @MockBean
    private UserRepository userRepository;

    @Test
    public void loadUserByUsername_UserEmptyException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsServices.loadUserByUsername(anyString()));
    }

    @Test
    public void loadUserByUsername_PasswordNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(PasswordNotFoundException.class, () -> userDetailsServices.loadUserByUsername(anyString()));
    }
}
