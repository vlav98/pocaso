package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.RatingService;
import com.nnk.springboot.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
public class RatingController {
    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addAttributes(Model model) throws AccessDeniedException {
        User connectedUser = userService.getAuthenticatedUser();
        model.addAttribute("connectedUser", connectedUser);
    }

    @RequestMapping("/rating/list")
    public String home(Model model) throws AccessDeniedException {
        List<Rating> ratings = ratingService.findAll();
        model.addAttribute("ratings", ratings);

        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating, Model model) {
        model.addAttribute("rating", rating);
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/add";
        }
        ratingService.save(rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Rating ratingToUpdate = ratingService.findById(id);
        if (ratingToUpdate == null) {
            throw new IllegalArgumentException("Invalid rating id " + id);
        }
        model.addAttribute("rating", ratingToUpdate);
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/update";
        }
        ratingService.update(id, rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        ratingService.deleteById(id);
        return "redirect:/rating/list";
    }
}
