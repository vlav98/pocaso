package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
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
public class TradeServiceTests {
    @Autowired
    private TradeService tradeService;
    @MockBean
    private TradeRepository tradeRepository;

    @Test
    public void testFindAll() {
        tradeService.findAll();
        verify(tradeRepository).findAll();
    }

    @Test
    public void testFindById_Successfully() {
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(new Trade()));

        tradeService.findById(anyInt());
        verify(tradeRepository).findById(anyInt());
    }

    @Test
    public void testFindById_InvalidId() {
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> tradeService.findById(anyInt()));
        verify(tradeRepository).findById(anyInt());
    }

    @Test
    public void testSaveTrade() {
        Trade mockTrade = new Trade();
        tradeService.save(mockTrade);
        verify(tradeRepository).save(mockTrade);
    }

    @Test
    public void testUpdateTrade_Successfully() {
        Trade mockTrade = new Trade();
        tradeService.update(anyInt(), mockTrade);
        verify(tradeRepository).save(mockTrade);
    }

    @Test
    public void testDeleteTrade_Successfully() {
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(new Trade()));
        tradeService.deleteById(anyInt());
        verify(tradeRepository).deleteById(anyInt());
    }

    @Test
    public void testDeleteTrade_InvalidId() {
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> tradeService.deleteById(anyInt()));
    }
}
