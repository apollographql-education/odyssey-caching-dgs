package com.example.listings;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

@Configuration
@EnableCaching
public class CacheJavaConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("listings");
        String specAsString = "initialCapacity=100,maximumSize=500,expireAfterAccess=5m,recordStats";
        cacheManager.setCacheSpecification(specAsString);

        return cacheManager;
    }
}
