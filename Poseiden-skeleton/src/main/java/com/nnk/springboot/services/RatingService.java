package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exceptions.NotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    public Rating findById(Integer id) {
        return ratingRepository.findById(id).orElseThrow(() -> new NotFoundException("Rating with id " + id + " not found"));
    }

    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    public void update(Integer id, Rating ratingToUpdate) {
        ratingToUpdate.setId(id);
        ratingRepository.save(ratingToUpdate);
    }

    public void deleteById(Integer id) {
        ratingRepository.findById(id).orElseThrow(() -> new NotFoundException("Rating with id " + id + " not found"));
        ratingRepository.deleteById(id);
    }
}
