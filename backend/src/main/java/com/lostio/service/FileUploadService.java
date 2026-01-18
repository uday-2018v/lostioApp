package com.lostio.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.lostio.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Service for file upload operations using Cloudinary.
 */
@Service
public class FileUploadService {
    
    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif"};
    
    @Autowired(required = false)
    private Cloudinary cloudinary;
    
    /**
     * Upload an image file to Cloudinary.
     * @param file Multipart file to upload
     * @param folder Folder name in Cloudinary
     * @return Public URL of the uploaded image
     */
    public String uploadImage(MultipartFile file, String folder) {
        logger.info("Uploading image to Cloudinary, folder: {}", folder);
        
        // Validate file
        validateFile(file);
        
        // Check if Cloudinary is configured
        if (cloudinary == null) {
            logger.warn("Cloudinary not configured, returning mock URL");
            return "https://via.placeholder.com/500x500.png?text=Image+Upload+Placeholder";
        }
        
        try {
            // Upload to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "image"
                    ));
            
            String url = (String) uploadResult.get("secure_url");
            logger.info("Image uploaded successfully: {}", url);
            
            return url;
        } catch (IOException e) {
            logger.error("Error uploading image to Cloudinary", e);
            throw new BadRequestException("Failed to upload image: " + e.getMessage());
        }
    }
    
    /**
     * Upload user avatar.
     * @param file Multipart file
     * @param userId User ID
     * @return Public URL of the uploaded avatar
     */
    public String uploadAvatar(MultipartFile file, String userId) {
        return uploadImage(file, "lostioapp/avatars/" + userId);
    }
    
    /**
     * Upload report photo.
     * @param file Multipart file
     * @param reportId Report ID
     * @return Public URL of the uploaded photo
     */
    public String uploadReportPhoto(MultipartFile file, String reportId) {
        return uploadImage(file, "lostioapp/reports/" + reportId);
    }
    
    /**
     * Validate uploaded file.
     * @param file Multipart file
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required");
        }
        
        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds maximum limit of 10MB");
        }
        
        // Check file extension
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new BadRequestException("Invalid file");
        }
        
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        boolean validExtension = false;
        for (String allowedExt : ALLOWED_EXTENSIONS) {
            if (allowedExt.equals(extension)) {
                validExtension = true;
                break;
            }
        }
        
        if (!validExtension) {
            throw new BadRequestException("Invalid file format. Allowed formats: jpg, jpeg, png, gif");
        }
        
        logger.info("File validation passed: {} ({})", filename, file.getSize());
    }
}
