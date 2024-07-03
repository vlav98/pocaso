package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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
public class BidListServiceTests {
    @Autowired
    private BidListService bidListService;
    @MockBean
    private BidListRepository bidListRepository;

    @Test
    public void testFindAll() {
        bidListService.findAll();
        verify(bidListRepository).findAll();
    }

    @Test
    public void testFindById_Successfully() {
        when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(new BidList()));

        bidListService.findById(anyInt());
        verify(bidListRepository).findById(anyInt());
    }

    @Test
    public void testFindById_InvalidId() {
        when(bidListRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> bidListService.findById(anyInt()));
        verify(bidListRepository).findById(anyInt());
    }

    @Test
    public void testSaveBidList() {
        BidList mockBidList = new BidList();
        bidListService.save(mockBidList);
        verify(bidListRepository).save(mockBidList);
    }

    @Test
    public void testUpdateBidList_Successfully() {
        BidList mockBidList = new BidList();
        bidListService.update(anyInt(), mockBidList);
        verify(bidListRepository).save(mockBidList);
    }

    @Test
    public void testDeleteBidList_Successfully() {
        when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(new BidList()));
        bidListService.deleteById(anyInt());
        verify(bidListRepository).deleteById(anyInt());
    }

    @Test
    public void testDeleteBidList_InvalidId() {
        when(bidListRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> bidListService.deleteById(anyInt()));
    }
}
