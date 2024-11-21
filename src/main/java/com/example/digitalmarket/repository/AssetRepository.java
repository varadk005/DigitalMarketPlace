package com.example.digitalmarket.repository;


import com.example.digitalmarket.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByTitleContainingIgnoreCase(String title);
    List<Asset> findByCategory(String category);
    List<Asset> findByTagsContaining(String tag);
    List<Asset> findByUploaderId(Long uploaderId);

}
