package com.example.digitalmarket.controller;

import com.example.digitalmarket.model.Asset;
import com.example.digitalmarket.model.User;
import com.example.digitalmarket.service.AssetService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Add this import
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/assets")  // Changed base path
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;

    @PostMapping("/favorite/{id}")
    public String toggleFavorite(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }

        int totalAssets = assetService.getAllAssets().size();
        int userAssets = assetService.getAssetsByUploaderId(user.getId()).size();

        return assetService.getAssetById(id)
                .map(asset -> {
                    assetService.toggleFavorite(asset, user);
                    return "redirect:/buyer/dashboard";
                })
                .orElse("redirect:/buyer/dashboard?error=asset-not-found");
    }


}