package com.studies.catalog.admin.infrastructure.configuration;

import com.google.cloud.storage.Storage;
import com.studies.catalog.admin.infrastructure.configuration.properties.google.GoogleStorageProperties;
import com.studies.catalog.admin.infrastructure.configuration.properties.storage.StorageProperties;
import com.studies.catalog.admin.infrastructure.services.StorageService;
import com.studies.catalog.admin.infrastructure.services.impl.GCStorageService;
import com.studies.catalog.admin.infrastructure.services.local.InMemoryStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {

    @Bean
    @ConfigurationProperties(value = "storage.catalog-videos")
    public StorageProperties storageProperties() {
        return new StorageProperties();
    }

    @Bean
    @Profile({"dev", "prod"})
    public StorageService gcStorageAPI(
            final GoogleStorageProperties props,
            final Storage storage
    ) {
        return new GCStorageService(props.getBucket(), storage);
    }

    @Bean
    @ConditionalOnMissingBean
    public StorageService localStorageAPI() {
        return new InMemoryStorageService();
    }

}