package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    public Rating findById(Integer id) {
        Optional<Rating> optionalRating = ratingRepository.findById(id);
        return optionalRating.orElse(null);
    }

    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }

    public void update(Integer id, Rating ratingToUpdate) {
        ratingToUpdate.setId(id);
        ratingRepository.save(ratingToUpdate);
    }

    public void delete(Integer id) {
        ratingRepository.deleteById(id);
    }
}
