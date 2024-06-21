package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.RuleService;
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
public class RuleNameController {
    // TODO: Inject RuleName service
    @Autowired
    private RuleService ruleService;
    @Autowired
    private UserService userService;


    @RequestMapping("/ruleName/list")
    public String home(Model model) throws AccessDeniedException {
        // TODO: find all RuleName, add to model
        List<RuleName> ruleNameList = ruleService.findAll();
        model.addAttribute("ruleNames", ruleNameList);

        User connectedUser = userService.getAuthenticatedUser();
        model.addAttribute("connectedUser", connectedUser);

        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName bid, Model model) {
        model.addAttribute("ruleName", new RuleName());
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return RuleName list
        if (result.hasErrors()) {
            return "ruleName/add";
        }
        ruleService.save(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get RuleName by Id and to model then show to the form
        RuleName ruleNameToUpdate = ruleService.findById(id);
        if (ruleNameToUpdate == null) {
            throw new IllegalArgumentException("Invalid ruleName id" + id);
        }
        model.addAttribute("ruleName", ruleNameToUpdate);
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update RuleName and return RuleName list
        if (result.hasErrors()) {
            return "ruleName/update";
        }
        ruleService.update(id, ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        // TODO: Find RuleName by Id and delete the RuleName, return to Rule list
        RuleName ruleNameToUpdate = ruleService.findById(id);
        if (ruleNameToUpdate == null) {
            throw new IllegalArgumentException("Invalid ruleName id " + id);
        }
        ruleService.deleteById(id);
        return "redirect:/ruleName/list";
    }
}
