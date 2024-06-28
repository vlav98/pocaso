package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleService;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RuleNameControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RuleService mockRuleService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void ruleNameListDisplayTest() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attributeExists("connectedUser"))
                .andExpect(view().name("ruleName/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void ruleNameFormDisplayTest() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleName"))
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void validateFormTest() throws Exception {
        RuleName validRuleName = new RuleName();
        validRuleName.setId(1);

        when(mockRuleService.save(any(RuleName.class))).thenReturn(validRuleName);

        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("id", String.valueOf(validRuleName.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateFormDisplayWithValidIdTest() throws Exception {
        RuleName validRuleName = new RuleName();

        when(mockRuleService.findById(anyInt())).thenReturn(validRuleName);

        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ruleName"))
                .andExpect(view().name("ruleName/update"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateFormDisplayWithInvalidIdTest() {
        when(mockRuleService.findById(anyInt())).thenReturn(null);

        assertThrows(Exception.class, () ->
                mockMvc.perform(get("/ruleName/update/1")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateRuleNameWithValidInputTest() throws Exception {
        int validId = 1;

        mockMvc.perform(post("/ruleName/update/" + validId).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    public void updateRuleNameWithValidInput_WithGuest_Test() throws Exception {
        int validId = 1;
        String domain = "http://localhost";

        mockMvc.perform(post("/ruleName/update/" + validId).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(domain + "/login"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteRuleNameTest() throws Exception {
        RuleName ruleName = new RuleName();
        when(mockRuleService.findById(anyInt())).thenReturn(ruleName);

        mockMvc.perform(get("/ruleName/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(mockRuleService).deleteById(anyInt());
    }
}
