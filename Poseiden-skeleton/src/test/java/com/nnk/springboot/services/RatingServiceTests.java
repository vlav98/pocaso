package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
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
public class RatingServiceTests {
    @Autowired
    private RatingService ratingService;
    @MockBean
    private RatingRepository ratingRepository;

    @Test
    public void testFindAll() {
        ratingService.findAll();
        verify(ratingRepository).findAll();
    }

    @Test
    public void testFindById_Successfully() {
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(new Rating()));

        ratingService.findById(anyInt());
        verify(ratingRepository).findById(anyInt());
    }

    @Test
    public void testFindById_InvalidId() {
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> ratingService.findById(anyInt()));
        verify(ratingRepository).findById(anyInt());
    }

    @Test
    public void testSaveRating() {
        Rating mockRating = new Rating();
        ratingService.save(mockRating);
        verify(ratingRepository).save(mockRating);
    }

    @Test
    public void testUpdateRating_Successfully() {
        Rating mockRating = new Rating();
        ratingService.update(anyInt(), mockRating);
        verify(ratingRepository).save(mockRating);
    }

    @Test
    public void testDeleteRating_Successfully() {
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(new Rating()));
        ratingService.deleteById(anyInt());
        verify(ratingRepository).deleteById(anyInt());
    }

    @Test
    public void testDeleteRating_InvalidId() {
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> ratingService.deleteById(anyInt()));
    }
}
