package com.example.digitalmarket.controller;

import com.example.digitalmarket.model.Asset;
import com.example.digitalmarket.model.User;
import com.example.digitalmarket.service.AdminService;
import com.example.digitalmarket.service.AssetService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
private final AssetService assetService;
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/auth/login";
        }

        model.addAttribute("userCount", adminService.getUserCount());
        model.addAttribute("buyerCount", adminService.getBuyerCount());
        model.addAttribute("sellerCount", adminService.getSellerCount());
        model.addAttribute("assetCount", adminService.getAssetCount());
        model.addAttribute("users", adminService.getAllUsers());
        model.addAttribute("assets", adminService.getAllAssets());

        return "admin/dashboard";


    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/auth/login";
        }
        adminService.deleteUser(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/assets/{id}/delete")
    public String deleteAsset(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.ADMIN) {
            return "redirect:/auth/login";
        }
        adminService.deleteAsset(id);
        return "redirect:/admin/dashboard";
    }
}