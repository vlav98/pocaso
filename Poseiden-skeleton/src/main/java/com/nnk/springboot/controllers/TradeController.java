package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.TradeService;
import com.nnk.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
public class TradeController {
    // TODO: Inject Trade service
    @Autowired
    private TradeService tradeService;
    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addAttributes(Model model) throws AccessDeniedException {
        User connectedUser = userService.getAuthenticatedUser();
        model.addAttribute("connectedUser", connectedUser);
    }

    @RequestMapping("/trade/list")
    public String home(Model model) throws AccessDeniedException {
        List<Trade> tradeList = tradeService.findAll();
        model.addAttribute("trades", tradeList);

        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(Trade bid, Model model) {
        model.addAttribute("trade", new Trade());
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/add";
        }
        tradeService.save(trade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Trade tradeToUpdate = tradeService.findById(id);
        if (tradeToUpdate == null) {
            throw new IllegalArgumentException("Invalid tradeId " + id);
        }
        model.addAttribute("trade", tradeToUpdate);
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/update";
        }
        tradeService.update(id, trade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        Trade tradeToUpdate = tradeService.findById(id);
        if (tradeToUpdate == null) {
            throw new IllegalArgumentException("Invalid tradeId " + id);
        }
        tradeService.deleteById(id);
        return "redirect:/trade/list";
    }
}
