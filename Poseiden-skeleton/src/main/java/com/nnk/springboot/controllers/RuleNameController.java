package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.RuleService;
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
public class RuleNameController {
    // TODO: Inject RuleName service
    @Autowired
    private RuleService ruleService;
    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addAttributes(Model model) throws AccessDeniedException {
        User connectedUser = userService.getAuthenticatedUser();
        model.addAttribute("connectedUser", connectedUser);
    }


    @RequestMapping("/ruleName/list")
    public String home(Model model) throws AccessDeniedException {
        List<RuleName> ruleNameList = ruleService.findAll();
        model.addAttribute("ruleNames", ruleNameList);

        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName bid, Model model) {
        model.addAttribute("ruleName", new RuleName());
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/add";
        }
        ruleService.save(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
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
        if (result.hasErrors()) {
            return "ruleName/update";
        }
        ruleService.update(id, ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        ruleService.deleteById(id);
        return "redirect:/ruleName/list";
    }
}
