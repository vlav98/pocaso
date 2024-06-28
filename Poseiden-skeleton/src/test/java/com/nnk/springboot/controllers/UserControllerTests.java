package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.file.AccessDeniedException;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService mockUserService;


    private User adminUser = new User();
    private User authenticatedUser = new User();

    private User validUser = new User();

    @BeforeEach
    public void setUp() throws AccessDeniedException {
        adminUser.setRole("ADMIN");
        authenticatedUser.setRole("USER");

        validUser.setId(1);
        validUser.setRole("ADMIN");
        validUser.setPassword("1234Admin");
        validUser.setUsername("admin2");
        validUser.setFullname("Full Name Admin");
    }

    @Test
    @WithMockUser(username = "user")
    public void redirectFromUserListDisplayIfNotAdminTest() throws Exception {
        when(mockUserService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void userListDisplayTest() throws Exception {
        when(mockUserService.getAuthenticatedUser()).thenReturn(adminUser);

        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("connectedUser"))
                .andExpect(view().name("user/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void userFormDisplayTest() throws Exception {
        mockMvc.perform(get("/user/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("user/add"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void validateFormTest() throws Exception {
        User validUser = new User();
        validUser.setId(1);
        validUser.setRole("ADMIN");
        validUser.setPassword("1234Admin");
        validUser.setUsername("admin2");
        validUser.setFullname("Full Name Admin");

        when(mockUserService.save(any(User.class))).thenReturn(validUser);

        mockMvc.perform(post("/user/validate")
                        .with(csrf())
                        .param("id", String.valueOf(validUser.getId()))
                        .param("fullname", validUser.getFullname())
                        .param("username", validUser.getUsername())
                        .param("password", validUser.getPassword())
                        .param("role", validUser.getRole())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateFormDisplayWithValidIdTest() throws Exception {
        User validUser = new User();

        when(mockUserService.findById(anyInt())).thenReturn(validUser);

        mockMvc.perform(get("/user/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("user/update"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateFormDisplayWithInvalidIdTest() {
        when(mockUserService.findById(anyInt())).thenReturn(null);

        assertThrows(Exception.class, () ->
                mockMvc.perform(get("/user/update/1")));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateUserWithValidInputTest() throws Exception {
        mockMvc.perform(post("/user/update/" + validUser.getId())
                        .with(csrf())
                        .param("fullname", validUser.getFullname())
                        .param("username", validUser.getUsername())
                        .param("password", validUser.getPassword())
                        .param("role", validUser.getRole())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateUserWithInvalidInput_Test() throws Exception {User validUser = new User();
        validUser.setPassword("");

        mockMvc.perform(post("/user/update/" + validUser.getId())
                        .with(csrf())
                        .param("fullname", validUser.getFullname())
                        .param("username", validUser.getUsername())
                        .param("password", validUser.getPassword())
                        .param("role", validUser.getRole())
                )
                .andExpect(status().isBadRequest());
    }


}
