package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
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
public class CurveControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CurvePointService mockCurvePointService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void curveListDisplayTest() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attributeExists("connectedUser"))
                .andExpect(view().name("curvePoint/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void bidFormDisplayTest() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void validateFormTest() throws Exception {
        CurvePoint validCurvePoint = new CurvePoint();
        validCurvePoint.setCurveId(1);
        validCurvePoint.setTerm(10.0);
        validCurvePoint.setValue(10.0);

        when(mockCurvePointService.save(any(CurvePoint.class))).thenReturn(validCurvePoint);

        mockMvc.perform(post("/curvePoint/validate").with(csrf())
                        .param("curveId", "1")
                        .param("term", "10.0")
                        .param("value", "10.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(mockCurvePointService).save(any(CurvePoint.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateFormDisplayWithValidIdTest() throws Exception {
        CurvePoint validCurvePoint = new CurvePoint();

        when(mockCurvePointService.findById(anyInt())).thenReturn(validCurvePoint);

        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(view().name("curvePoint/update"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateFormDisplayWithInvalidIdTest() {
        when(mockCurvePointService.findById(anyInt())).thenReturn(null);

        assertThrows(Exception.class, () ->
                mockMvc.perform(get("/curvePoint/update/1")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void updateCurvePointWithValidInputTest() throws Exception {
        int validId = 1;

        mockMvc.perform(post("/curvePoint/update/" + validId).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    public void updateCurvePointWithValidInput_WithGuest_Test() throws Exception {
        int validId = 1;
        String domain = "http://localhost";

        mockMvc.perform(post("/curvePoint/update/" + validId).with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(domain + "/login"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteCurvePointTest() throws Exception {
        CurvePoint curvePoint = new CurvePoint();
        when(mockCurvePointService.findById(anyInt())).thenReturn(curvePoint);

        mockMvc.perform(get("/curvePoint/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(mockCurvePointService).deleteById(anyInt());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteCurvePointWithInvalidIdTest() {
        when(mockCurvePointService.findById(anyInt())).thenReturn(null);

        doThrow(new IllegalArgumentException()).when(mockCurvePointService).deleteById(null);

        assertThrows(Exception.class, () -> mockMvc.perform(get("/curvePoint/delete/1"))
                .andExpect(status().is4xxClientError()));
    }
}
