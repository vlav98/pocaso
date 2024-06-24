package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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
public class TradeControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TradeService mockTradeService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void tradeListDisplayTest() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trades"))
                .andExpect(model().attributeExists("connectedUser"))
                .andExpect(view().name("trade/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void tradeFormDisplayTest() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trade"))
                .andExpect(view().name("trade/add"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void validateFormTest() throws Exception {
        Trade validTrade = new Trade();
        validTrade.setTradeId(1);

        when(mockTradeService.save(any(Trade.class))).thenReturn(validTrade);

        mockMvc.perform(post("/trade/validate")
                        .with(csrf())
                        .param("id", String.valueOf(validTrade.getTradeId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateFormDisplayWithValidIdTest() throws Exception {
        Trade validTrade = new Trade();

        when(mockTradeService.findById(anyInt())).thenReturn(validTrade);

        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("trade"))
                .andExpect(view().name("trade/update"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateFormDisplayWithInvalidIdTest() {
        when(mockTradeService.findById(anyInt())).thenReturn(null);

        assertThrows(Exception.class, () ->
                mockMvc.perform(get("/trade/update/1")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateTradeWithValidInputTest() throws Exception {
        int validId = 1;

        mockMvc.perform(post("/trade/update/" + validId).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    public void updateTradeWithValidInput_WithGuest_Test() throws Exception {
        int validId = 1;
        String domain = "http://localhost";

        mockMvc.perform(post("/trade/update/" + validId).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(domain + "/login"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteTradeTest() throws Exception {
        Trade trade = new Trade();
        when(mockTradeService.findById(anyInt())).thenReturn(trade);

        mockMvc.perform(get("/trade/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(mockTradeService).deleteById(anyInt());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void throwsWhenDeleteTradeWithInvalidIdTest() {
        when(mockTradeService.findById(anyInt())).thenReturn(null);

        assertThrows(Exception.class, () ->
                mockMvc.perform(get("/trade/delete/1")));
    }
}
