package com.oddscorp.internetbanking.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.oddscorp.internetbanking.domain.PrimaryAccount;
import com.oddscorp.internetbanking.domain.SavingAccount;
import com.oddscorp.internetbanking.domain.User;
import com.oddscorp.internetbanking.domain.security.UserRole;
import com.oddscorp.internetbanking.service.RoleService;
import com.oddscorp.internetbanking.service.UserService;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@RequestMapping("/")
	public String home() {
		return "redirect:/index";
	}
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signup(Model model) {
		
		User user = new User();
		
		model.addAttribute("user",user);
		
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST )
	public String signupPost(@ModelAttribute("user") User user, Model model) {
		
        if(userService.checkUserExists(user.getUsername(), user.getEmail()))  {

            if (userService.checkEmailExists(user.getEmail())) {
                model.addAttribute("emailExists", true);
            }

            if (userService.checkUsernameExists(user.getUsername())) {
                model.addAttribute("usernameExists", true);
            }

            return "signup";
        } else {
        	Set<UserRole> userRoles = new HashSet<>();
        	userRoles.add(new UserRole(user, roleService.findByName("ROLE_USER")));
        	
            userService.createUser(user, userRoles);

            return "redirect:/";
        }
	}
	
	@RequestMapping("/userFront")
	public String userFront(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        PrimaryAccount primaryAccount = user.getPrimaryAccount();
        SavingAccount savingsAccount = user.getSavingAccount();

        model.addAttribute("primaryAccount", primaryAccount);
        model.addAttribute("savingAccount", savingsAccount);

        return "userFront";
    }

}
