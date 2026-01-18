package com.lostio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB configuration.
 * Enables MongoDB auditing for automatic timestamp management.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.lostio.repository")
@EnableMongoAuditing
public class MongoConfig {
}
