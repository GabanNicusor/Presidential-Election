package com.presidential.elections.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.presidential.elections.Entities.User;
import com.presidential.elections.Repository.UserRepository;
import com.presidential.elections.Service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("loginRequest") User user, @RequestParam("username") String Username, @RequestParam("password") String Password) {
        if (userRepository.findByusername(Username).isPresent()) {
            return "error_page";
        }else {
            String newPassword = bCryptPasswordEncoder.encode(Password);
            user.setUsername(Username);
            user.setPassword(newPassword);
            User registerUser = userService.registerUser(Username, newPassword);
        if(registerUser != null) {
            return "/login";
        }else{
            return "error_page";
        }
        }
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "register";
    }

}
