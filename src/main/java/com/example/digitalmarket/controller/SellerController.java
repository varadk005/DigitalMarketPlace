package com.example.digitalmarket.controller;

import com.example.digitalmarket.model.Asset;
import com.example.digitalmarket.model.User;
import com.example.digitalmarket.service.AssetService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {
    private final AssetService assetService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.SELLER) {
            return "redirect:/auth/login";
        }
        model.addAttribute("assets", assetService.getAssetsByUploaderId(user.getId()));
        return "seller/dashboard";
    }

    @GetMapping("/upload")
    public String showUploadForm(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.SELLER) {
            return "redirect:/auth/login";
        }
        return "seller/upload";
    }

    @PostMapping("/upload")
    public String uploadAsset(@RequestParam("title") String title,
                              @RequestParam("description") String description,
                              @RequestParam("category") String category,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam(value = "tags", required = false) String tags,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != User.Role.SELLER) {
            return "redirect:/auth/login";
        }

        try {
            Asset asset = new Asset();
            asset.setTitle(title);
            asset.setDescription(description);
            asset.setCategory(category);

            // Process tags if provided
            if (tags != null && !tags.trim().isEmpty()) {
                Set<String> tagSet = new HashSet<>(Arrays.asList(tags.split("\\s*,\\s*")));
                asset.setTags(tagSet);
            }

            assetService.uploadAsset(asset, file, user);
            return "redirect:/seller/dashboard?success";
        } catch (Exception e) {
            return "redirect:/seller/upload?error=" + e.getMessage();
        }
    }
}