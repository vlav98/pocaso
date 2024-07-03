package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository mockUserRepository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @Test
    public void testGetAuthenticatedUser_Successfully() throws AccessDeniedException {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user");

        User expectedUser = new User();
        expectedUser.setUsername("user");
        when(userService.getUserByUsername("user")).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getAuthenticatedUser();
        assertEquals(actualUser, expectedUser);
    }

    @Test
    public void testGetAuthenticatedUser_InvalidUsername() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("unknown_user");

        when(userService.getUserByUsername("unknown_user")).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> userService.getAuthenticatedUser());
    }

    @Test
    public void testFindAll() {
        userService.findAll();
        verify(mockUserRepository).findAll();
    }

    @Test
    public void testFindById_Successfully() {
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(new User()));

        userService.findById(anyInt());
        verify(mockUserRepository).findById(anyInt());
    }

    @Test
    public void testFindById_InvalidId() {
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> userService.findById(anyInt()));
        verify(mockUserRepository).findById(anyInt());
    }

    @Test
    public void testSaveUser() {
        User mockUser = new User();
        userService.save(mockUser);
        verify(mockUserRepository).save(mockUser);
    }

    @Test
    public void testDeleteUser_Successfully() {
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        userService.delete(anyInt());
        verify(mockUserRepository).deleteById(anyInt());
    }

    @Test
    public void testDeleteUser_InvalidId() {
        when(mockUserRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> userService.delete(anyInt()));
    }
}
