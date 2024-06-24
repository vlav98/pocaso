package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.AccessDeniedException;

@Controller
public class HomeController
{
	@Autowired
	private UserService userService;

	@ModelAttribute
	public void addAttributes(Model model) throws AccessDeniedException {
		User connectedUser = userService.getAuthenticatedUser();
		model.addAttribute("connectedUser", connectedUser);
	}

	@RequestMapping("/")
	public String home(Model model) throws AccessDeniedException {
		return "home";
	}

	@RequestMapping("/admin/home")
	public String adminHome(Model model)
	{
		return "redirect:/bidList/list";
	}


}
