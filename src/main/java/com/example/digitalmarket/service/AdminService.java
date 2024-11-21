package com.example.digitalmarket.service;


import com.example.digitalmarket.model.Asset;
import com.example.digitalmarket.model.User;
import com.example.digitalmarket.repository.AssetRepository;
import com.example.digitalmarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final AssetRepository assetRepository;
    private final String uploadDir = "uploads";

    public long getUserCount() {
        return userRepository.count();
    }

    public long getBuyerCount() {
        return userRepository.countByRole(User.Role.BUYER);
    }

    public long getSellerCount() {
        return userRepository.countByRole(User.Role.SELLER);
    }

    public long getAssetCount() {
        return assetRepository.count();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == User.Role.ADMIN) {
            throw new RuntimeException("Cannot delete admin user");
        }

        // Delete user's assets if they're a seller
        if (user.getRole() == User.Role.SELLER) {
            List<Asset> userAssets = assetRepository.findByUploaderId(id);
            for (Asset asset : userAssets) {
                deleteAsset(asset.getId());
            }
        }

        userRepository.deleteById(id);
    }

    public void deleteAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        // Delete the physical file
        try {
            Path filePath = Paths.get(uploadDir, asset.getFileUrl());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log the error but continue with database deletion
            e.printStackTrace();
        }

        // Delete from database
        assetRepository.deleteById(id);
    }
}