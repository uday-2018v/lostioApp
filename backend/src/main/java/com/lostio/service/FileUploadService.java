package com.lostio.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lostio.dto.response.FileUploadResponse;
import com.lostio.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Service for file upload operations using Cloudinary
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {
    
    private final Cloudinary cloudinary;
    
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_FORMATS = {"jpg", "jpeg", "png", "gif", "webp"};
    
    /**
     * Upload an image to Cloudinary
     */
    public FileUploadResponse uploadImage(MultipartFile file, String folder) {
        log.debug("Uploading image to folder: {}", folder);
        
        // Validate file
        validateFile(file);
        
        try {
            // Upload to Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "folder", folder,
                    "resource_type", "image"
                )
            );
            
            String url = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");
            String format = (String) uploadResult.get("format");
            
            log.info("Image uploaded successfully: {}", publicId);
            
            return new FileUploadResponse(url, publicId, format);
            
        } catch (IOException e) {
            log.error("Error uploading image: {}", e.getMessage());
            throw new BadRequestException("Failed to upload image: " + e.getMessage());
        }
    }
    
    /**
     * Delete an image from Cloudinary
     */
    public void deleteImage(String publicId) {
        log.debug("Deleting image with publicId: {}", publicId);
        
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Image deleted successfully: {}", publicId);
        } catch (IOException e) {
            log.error("Error deleting image: {}", e.getMessage());
            throw new BadRequestException("Failed to delete image: " + e.getMessage());
        }
    }
    
    /**
     * Validate uploaded file
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required");
        }
        
        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds maximum limit of 10MB");
        }
        
        // Check file format
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BadRequestException("Invalid file name");
        }
        
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        boolean isValidFormat = false;
        for (String format : ALLOWED_FORMATS) {
            if (format.equals(extension)) {
                isValidFormat = true;
                break;
            }
        }
        
        if (!isValidFormat) {
            throw new BadRequestException("Invalid file format. Allowed formats: jpg, jpeg, png, gif, webp");
        }
    }
}
