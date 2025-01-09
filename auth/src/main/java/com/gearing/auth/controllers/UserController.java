package com.gearing.auth.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gearing.auth.models.User;
import com.gearing.auth.services.UserService;
import com.gearing.auth.validator.UserValidator;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {
	@Autowired
	private UserService userServ;
	@Autowired
	private UserValidator userValid;
	
	@GetMapping("/register")
	public String registerForm(@ModelAttribute("user") User user) {
		return "registrationPage.jsp";
	}
	
	@PostMapping("/register")
	public String registration(@Valid @ModelAttribute("user") User user, BindingResult result,
			Model model, HttpSession session) {
		userValid.validate(user, result);
		if(result.hasErrors()) {
			return "registrationPage.jsp";
		}
		
		userServ.saveUserWithAdminRole(user);
		return "redirect:/login";
	}
	
	@GetMapping("/login")
	public String login(@RequestParam(required=false) String error,
			@RequestParam(required=false) String logout, Model model) {
		if(error != null)
			model.addAttribute("errorMessage", "Invalid Credentials. Please try again.");
		if(logout != null)
			model.addAttribute("logoutMessage", "Logout Successful!");
		
		return "loginPage.jsp";
	}
	
	// After authentication
	
	@GetMapping(value = {"/", "/home"})
	public String home(Principal principal, Model model) {
		String username = principal.getName();
		model.addAttribute("currentUser", userServ.findByUsername(username));
		
		return "homePage.jsp";
	}
	
	@GetMapping("/admin")
	public String adminPage(Principal principal, Model model) {
		String username = principal.getName();
		model.addAttribute("currentUser", userServ.findByUsername(username));
		
		return "adminPage.jsp";
	}
}
