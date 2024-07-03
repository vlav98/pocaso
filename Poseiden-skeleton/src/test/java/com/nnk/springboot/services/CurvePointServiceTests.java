package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class CurvePointServiceTests {
    @Autowired
    private CurvePointService curvePointService;
    @MockBean
    private CurvePointRepository curvePointRepository;

    @Test
    public void testFindAll() {
        curvePointService.findAll();
        verify(curvePointRepository).findAll();
    }

    @Test
    public void testFindById_Successfully() {
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(new CurvePoint()));

        curvePointService.findById(anyInt());
        verify(curvePointRepository).findById(anyInt());
    }

    @Test
    public void testFindById_InvalidId() {
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> curvePointService.findById(anyInt()));
        verify(curvePointRepository).findById(anyInt());
    }

    @Test
    public void testSaveCurvePoint() {
        CurvePoint mockCurvePoint = new CurvePoint();
        curvePointService.save(mockCurvePoint);
        verify(curvePointRepository).save(mockCurvePoint);
    }

    @Test
    public void testUpdateCurvePoint_Successfully() {
        CurvePoint mockCurvePoint = new CurvePoint();
        curvePointService.update(anyInt(), mockCurvePoint);
        verify(curvePointRepository).save(mockCurvePoint);
    }

    @Test
    public void testDeleteCurvePoint_Successfully() {
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(new CurvePoint()));
        curvePointService.deleteById(anyInt());
        verify(curvePointRepository).deleteById(anyInt());
    }

    @Test
    public void testDeleteCurvePoint_InvalidId() {
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> curvePointService.deleteById(anyInt()));
    }
}
