package com.tsyhanok.bulletinboard.controller;

import com.tsyhanok.bulletinboard.beans.User;
import com.tsyhanok.bulletinboard.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class UserController {

    final UserRepository userRepository;

    final PasswordEncoder passwordEncoder;

    @GetMapping("/updateUser")
    public ModelAndView updateUser() {
        ModelAndView model = new ModelAndView("user-page");

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());

        if(user.isPresent()){
            model.addObject("newUser",user.get());
        } else {
            model.addObject("newUser", new User());
        }
        return model;
    }

    @PostMapping("/updateUser/{id}")
    public String updateUser(@ModelAttribute User newUser, @PathVariable Long id) {
        userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(newUser.getFirstName());
                    user.setEmail(newUser.getEmail());
                    user.setPassword(passwordEncoder.encode(newUser.getPassword()));
                    return userRepository.save(user);
                });
        return "redirect:/";
    }
}
