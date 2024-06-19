package com.nnk.springboot.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CurveControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CurvePointService mockCurvePointService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void homeTest() throws Exception {
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

//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    public void validateFormTest() throws Exception {
//        CurvePoint validCurvePoint = new CurvePoint();
//        validCurvePoint.setCurveId(1);
//        validCurvePoint.setTerm(10.0);
//        validCurvePoint.setValue(10.0);
//
//        when(mockCurvePointService.save(any(CurvePoint.class))).thenReturn(validCurvePoint);
//
//        mockMvc.perform(post("/curvePoint/validate")
//                        .param("curveId", "1")
//                        .param("term", "10.0")
//                        .param("value", "10.0"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/curvePoint/list"));
//
//        verify(mockCurvePointService).save(any(CurvePoint.class));
//    }

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


//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    public void updateBidWithValidInputTest() throws Exception {
//        int validId = 1;
//        CurvePoint validCurvePoint = new CurvePoint();
//
//        mockMvc.perform(post("/curvePoint/update/" + validId))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("curvePoint"))
//                .andExpect(view().name("curvePoint/update"));
//    }


//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    public void updateBidWithErrorsTest() throws Exception {
//        mockMvc.perform(post("/curvePoint/update/1")
//                        .param("curveId", "")
//                        .param("term", "")
//                        .param("value", ""))
//                .andExpect(status().isOk())
//                .andExpect(view().name("curvePoint/update"));
//    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteBidTest() throws Exception {
        CurvePoint curvePoint = new CurvePoint();
        when(mockCurvePointService.findById(anyInt())).thenReturn(curvePoint);

        mockMvc.perform(get("/curvePoint/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(mockCurvePointService).deleteById(anyInt());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteBidWithInvalidIdTest() throws Exception {
        when(mockCurvePointService.findById(anyInt())).thenReturn(null);

        doThrow(new IllegalArgumentException()).when(mockCurvePointService).deleteById(null);

        assertThrows(Exception.class, () -> mockMvc.perform(get("/curvePoint/delete/1"))
                .andExpect(status().is4xxClientError()));
    }
}
