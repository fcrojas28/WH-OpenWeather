package com.fcrojas.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


/**
 * 
 * @author frojas
 *
 */
@EnableCaching
@Configuration
public class EhCacheConfig {

	@Bean
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(cacheMangerFactory().getObject());
	}

	@Bean
	public EhCacheManagerFactoryBean cacheMangerFactory() {
		EhCacheManagerFactoryBean bean = new EhCacheManagerFactoryBean();
		bean.setConfigLocation(new ClassPathResource("myehcache.xml"));
		bean.setShared(true);
		return bean;
	}
	
}
