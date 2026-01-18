package com.lostio.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cloudinary configuration for image upload.
 */
@Configuration
public class CloudinaryConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(CloudinaryConfig.class);
    
    @Value("${cloudinary.cloud-name:}")
    private String cloudName;
    
    @Value("${cloudinary.api-key:}")
    private String apiKey;
    
    @Value("${cloudinary.api-secret:}")
    private String apiSecret;
    
    /**
     * Create Cloudinary bean if credentials are provided.
     * @return Cloudinary instance or null
     */
    @Bean
    public Cloudinary cloudinary() {
        if (cloudName.isEmpty() || apiKey.isEmpty() || apiSecret.isEmpty()) {
            logger.warn("Cloudinary credentials not configured. Image upload will return mock URLs.");
            return null;
        }
        
        logger.info("Configuring Cloudinary with cloud name: {}", cloudName);
        
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }
}
