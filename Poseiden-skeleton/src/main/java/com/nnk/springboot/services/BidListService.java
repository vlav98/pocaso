package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.NotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidListService {
    @Autowired
    private BidListRepository bidListRepository;

    public List<BidList> findAll() {
        return bidListRepository.findAll();
    }

    public BidList findById(Integer id) {
        return bidListRepository.findById(id).orElseThrow(() -> new NotFoundException("Bid List with id " + id + "not found."));
    }

    public BidList save(BidList bidList) {
        return bidListRepository.save(bidList);
    }

    public void update(Integer id, BidList bidListToUpdate) {
        bidListToUpdate.setBidListId(id);
        bidListRepository.save(bidListToUpdate);
    }

    public void deleteById(Integer id) {
        bidListRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Bid List with id " + id+ " not found"));
        bidListRepository.deleteById(id);
    }
}
