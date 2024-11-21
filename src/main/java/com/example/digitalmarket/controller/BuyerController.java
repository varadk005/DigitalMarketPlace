package com.example.digitalmarket.controller;


import com.example.digitalmarket.model.User;
import com.example.digitalmarket.service.AssetService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/buyer")
@RequiredArgsConstructor
public class BuyerController {
    private final AssetService assetService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.BUYER) {
            return "redirect:/login";
        }
        model.addAttribute("assets", assetService.getAllAssets());
        return "buyer/dashboard";
    }

    @GetMapping("/search")
    public String search(@RequestParam String query, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.BUYER) {
            return "redirect:/login";
        }
        model.addAttribute("assets", assetService.searchAssets(query));
        return "buyer/dashboard";
    }
    @GetMapping("/assets/details/{id}")
    public String viewAssetDetails(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.BUYER) {
            return "redirect:/login";
        }

        assetService.getAssetById(id).ifPresentOrElse(asset -> model.addAttribute("asset", asset),
                () -> model.addAttribute("error", "Asset not found"));

        return "assets/asset-details";
    }
    @PostMapping("/assets/favorite/{id}")
    @ResponseBody
    public String toggleFavorite(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.BUYER) {
            return "redirect:/login";
        }

        assetService.getAssetById(id).ifPresent(asset -> assetService.toggleFavorite(asset, user));
        return "success";
    }


}