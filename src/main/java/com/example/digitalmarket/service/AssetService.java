package com.example.digitalmarket.service;

import com.example.digitalmarket.model.Asset;
import com.example.digitalmarket.model.User;
import com.example.digitalmarket.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssetService {
    private final AssetRepository assetRepository;
    private final FileStorageService fileStorageService;
    public Asset uploadAsset(Asset asset, MultipartFile file, User uploader) {
        // Store the file and get the filename
        String fileName = fileStorageService.storeFile(file);

        // Set the file URL in the asset
        asset.setFileUrl(fileName);
        asset.setUploader(uploader);

        // Save the asset in the database
        return assetRepository.save(asset);
    }

    // Add missing methods
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Optional<Asset> getAssetById(Long id) {
        return assetRepository.findById(id);
    }

    public List<Asset> searchAssets(String query) {
        return assetRepository.findByTitleContainingIgnoreCase(query);
    }

    public List<Asset> getAssetsByCategory(String category) {
        return assetRepository.findByCategory(category);
    }

    public List<Asset> getAssetsByUploaderId(Long uploaderId) {
        return assetRepository.findByUploaderId(uploaderId);

    }


    public void toggleFavorite(Asset asset, User user) {
        if (asset.getFavoritedBy().contains(user)) {
            asset.getFavoritedBy().remove(user);
        } else {
            asset.getFavoritedBy().add(user);
        }
        assetRepository.save(asset);
    }


}