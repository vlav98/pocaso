package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.CurvePointService;
import com.nnk.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
public class CurveController {
    // TODO: Inject Curve Point service
    @Autowired
    private CurvePointService curvePointService;
    @Autowired
    private UserService userService;

    @RequestMapping("/curvePoint/list")
    public String home(Model model) throws AccessDeniedException {
        // TODO: find all Curve Point, add to model
        User connectedUser = userService.getAuthenticatedUser();
        List<CurvePoint> curvePointList = curvePointService.findAll();
        model.addAttribute("connectedUser", connectedUser);
        model.addAttribute("curvePoints", curvePointList);
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addBidForm(CurvePoint bid) {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "curvePoint/add";
        }
        curvePointService.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePointToUpdate = curvePointService.findById(id);
        if (curvePointToUpdate == null) {
            throw new IllegalArgumentException("Invalid curvePoint id" + id);
        }
        model.addAttribute("curvePoint", curvePointToUpdate);
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "curvePoint/update";
        }
        curvePointService.update(id, curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        CurvePoint curvePointToUpdate = curvePointService.findById(id);
        if (curvePointToUpdate == null) {
            throw new IllegalArgumentException("Invalid curvePoint id" + id);
        }
        curvePointService.deleteById(id);
        return "redirect:/curvePoint/list";
    }
}
