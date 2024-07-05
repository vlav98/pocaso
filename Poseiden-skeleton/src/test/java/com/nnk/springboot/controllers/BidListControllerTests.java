package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
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
public class BidListControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BidListService mockBidListService;

    @Test
    @WithMockUser(username = "user")
    public void bidListDisplayTest() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidLists"))
                .andExpect(model().attributeExists("connectedUser"))
                .andExpect(view().name("bidList/list"));
    }

    @Test
    @WithMockUser(username = "user")
    public void bidListFormDisplayTest() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }

    @Test
    @WithMockUser(username = "user")
    public void validateFormTest() throws Exception {
        BidList validBidList = new BidList();
        validBidList.setBidListId(1);
        validBidList.setAccount("User");
        validBidList.setType("Type test");
        validBidList.setBidQuantity(2.0);

        when(mockBidListService.save(any(BidList.class))).thenReturn(validBidList);

        mockMvc.perform(post("/bidList/validate").with(csrf())
                        .param("bidListId", String.valueOf(validBidList.getBidListId()))
                        .param("account", validBidList.getAccount())
                        .param("type", validBidList.getType())
                        .param("bidQuantity", String.valueOf(validBidList.getBidQuantity())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(mockBidListService).save(any(BidList.class));
    }

    @Test
    @WithMockUser(username = "user")
    public void updateFormDisplayWithValidIdTest() throws Exception {
        BidList validBidList = new BidList();

        when(mockBidListService.findById(anyInt())).thenReturn(validBidList);

        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bidList"))
                .andExpect(view().name("bidList/update"));
    }

    @Test
    @WithMockUser(username = "user")
    public void updateFormDisplayWithInvalidIdTest() {
        when(mockBidListService.findById(anyInt())).thenReturn(null);

        assertThrows(Exception.class, () ->
                mockMvc.perform(get("/bidList/update/1")));
    }

    @Test
    @WithMockUser(username = "user")
    public void updateBidListWithValidInputTest() throws Exception {
        int validId = 1;

        mockMvc.perform(post("/bidList/update/" + validId).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void updateBidListWithValidInput_WithGuest_Test() throws Exception {
        int validId = 1;

        mockMvc.perform(post("/bidList/update/" + validId).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "user")
    public void deleteBidListTest() throws Exception {
        BidList bidList = new BidList();
        when(mockBidListService.findById(anyInt())).thenReturn(bidList);

        mockMvc.perform(get("/bidList/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(mockBidListService).deleteById(anyInt());
    }
}
