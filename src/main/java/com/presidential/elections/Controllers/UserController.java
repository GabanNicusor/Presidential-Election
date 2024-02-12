package com.presidential.elections.Controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
            return "error";
        }
        else {
            String newPassword = bCryptPasswordEncoder.encode(Password);
            user.setUsername(Username);
            user.setPassword(newPassword);
            User registerUser = userService.registerUser(Username, newPassword);
            if (registerUser != null) {
                return "/login";
            }
            else{
                return "error";
            }
        }
    }
    
    @GetMapping("/register")
    public String getRegisterPage() {
        return "register";
    }

    @PostMapping("/UpdateProfileData") 
    public String SaveUserProfileSettingController(
        @ModelAttribute("SaveProfileRequest") 
        @RequestParam("family_name") String Family_Name,
        @RequestParam("surname") String Surname,
        @RequestParam("address") String Address, 
        @RequestParam("about_me") String AboutMe,
        @RequestParam("city") String City, 
        @RequestParam("state") String State, 
        @RequestParam("zip") String Zip) 
        {
        userService.saveUserProfileSetting(userService.getUsernameAuthenticated(), Family_Name, Surname, Address, AboutMe, City, State, Zip);
        return "ProfileSettings";
    }

    @GetMapping("/displayUserProfile")
    public String addData(Model model) {
        Optional<User> optionalUser = userRepository.findByusername(userService.getUsernameAuthenticated());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("family_name_id", user.getFamily_name());
            model.addAttribute("surname_id", user.getSurname());
            model.addAttribute("about_me_id", user.getAbout_me());
            model.addAttribute("address_id", user.getAddress());
            model.addAttribute("city_id", user.getCity());
            model.addAttribute("state_id", user.getState());
            model.addAttribute("zip_id", user.getZip());
            model.addAttribute("link_photo", user.getLink_photo());
            return "ProfilePage";
        }
        return "The user is not authenticated";
    }

}
