package com.tsyhanok.bulletinboard.controller;

import com.tsyhanok.bulletinboard.beans.User;
import com.tsyhanok.bulletinboard.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@AllArgsConstructor
public class AuthController {

    final UserRepository userRepository;

    final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("userRegisterModel") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public ModelAndView registerUser(){
        ModelAndView model = new ModelAndView("signup");
        model.addObject("userRegisterModel", new User());
        return model;
    }


}
