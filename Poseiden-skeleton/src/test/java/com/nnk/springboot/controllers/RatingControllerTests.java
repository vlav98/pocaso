package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.runner.RunWith;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RatingControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RatingService mockRatingService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void ratingListDisplayTest() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attributeExists("connectedUser"))
                .andExpect(view().name("rating/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void ratingFormDisplayTest() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("rating"))
                .andExpect(view().name("rating/add"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void validateFormTest() throws Exception {
        Rating validRating = new Rating();
        validRating.setId(1);

        when(mockRatingService.save(any(Rating.class))).thenReturn(validRating);

        mockMvc.perform(post("/rating/validate")
                        .with(csrf())
                        .param("id", String.valueOf(validRating.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateFormDisplayWithValidIdTest() throws Exception {
        Rating validRating = new Rating();

        when(mockRatingService.findById(anyInt())).thenReturn(validRating);

        mockMvc.perform(get("/rating/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("rating"))
                .andExpect(view().name("rating/update"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateFormDisplayWithInvalidIdTest() {
        when(mockRatingService.findById(anyInt())).thenReturn(null);

        assertThrows(Exception.class, () ->
                mockMvc.perform(get("/rating/update/1")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateRatingWithValidInputTest() throws Exception {
        int validId = 1;

        mockMvc.perform(post("/rating/update/" + validId).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    public void updateRatingWithValidInput_WithGuest_Test() throws Exception {
        int validId = 1;
        String domain = "http://localhost";

        mockMvc.perform(post("/curvePoint/update/" + validId).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(domain + "/login"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteRatingTest() throws Exception {
        Rating rating = new Rating();
        when(mockRatingService.findById(anyInt())).thenReturn(rating);

        mockMvc.perform(get("/rating/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        verify(mockRatingService).deleteById(anyInt());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void throwsWhenDeleteRatingWithInvalidIdTest() {
        when(mockRatingService.findById(anyInt())).thenReturn(null);

        assertThrows(Exception.class, () ->
                mockMvc.perform(get("/rating/delete/1")));
    }
}
