package com.example.digitalmarket.controller;

import com.example.digitalmarket.model.User;
import com.example.digitalmarket.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")  // Added base path for authentication
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {
        return userService.login(username, password)
                .map(user -> {
                    session.setAttribute("user", user);
                    // Redirect based on role
                    switch (user.getRole()) {
                        case BUYER:
                            return "redirect:/buyer/dashboard";
                        case SELLER:
                            return "redirect:/seller/dashboard";
                        case ADMIN:
                            return "redirect:/admin/dashboard";
                        default:
                            return "redirect:/auth/login?error";
                    }
                })
                .orElse("redirect:/auth/login?error");
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}