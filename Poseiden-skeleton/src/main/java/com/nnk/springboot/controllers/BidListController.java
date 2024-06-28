package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.BidListService;
import com.nnk.springboot.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;


@Controller
public class BidListController {
    @Autowired
    private UserService userService;
    @Autowired
    private BidListService bidListService;

    @ModelAttribute
    public void addAttributes(Model model) throws AccessDeniedException {
        User connectedUser = userService.getAuthenticatedUser();
        model.addAttribute("connectedUser", connectedUser);
    }

    @RequestMapping("/bidList/list")
    public String home(Model model) throws AccessDeniedException {
        model.addAttribute("bidLists", bidListService.findAll());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bidList, Model model){
        model.addAttribute("bidList", bidList);
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bidList, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/add";
        }
        bidListService.save(bidList);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        BidList bidListToUpdate = bidListService.findById(id);
        if (bidListToUpdate == null) {
            throw new IllegalArgumentException("Invalid curvePoint id" + id);
        }
        model.addAttribute("bidList", bidListToUpdate);
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "bidList/update";
        }
        bidListService.update(id, bidList);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        bidListService.deleteById(id);
        return "redirect:/bidList/list";
    }
}
