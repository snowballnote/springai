package com.example.rag;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOriginPatterns("http://localhost:5173", "http://localhost")
		.allowedMethods("GET","POST","DELTE","OPTIONS")
		.allowedHeaders("*")
		.allowCredentials(true); // 쿠키허용
	}
}