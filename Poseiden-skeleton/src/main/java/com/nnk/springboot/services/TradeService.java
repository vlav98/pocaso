package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.NotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {
    @Autowired
    private TradeRepository tradeRepository;

    public List<Trade> findAll() {
        return tradeRepository.findAll();
    }

    public Trade findById(Integer id) {
        return tradeRepository.findById(id).orElseThrow(() -> new NotFoundException("Trade with id " + id + " not found"));
    }

    public Trade save(Trade trade) {
        return tradeRepository.save(trade);
    }

    public void update(Integer id, Trade tradeToUpdate) {
        tradeToUpdate.setTradeId(id);
        tradeRepository.save(tradeToUpdate);
    }

    public void deleteById(Integer id) {
        tradeRepository.findById(id).orElseThrow(() -> new NotFoundException("Trade with id " + id + " not found"));
        tradeRepository.deleteById(id);
    }
}
