package com.example.openai0113;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
        // API서버는 Cross가 아니므로 접근 허용 ex) API서버주소가 http://localhost:8080이면 여기서는 접근 가능
		.allowedOrigins("http://localhost:80", "http://localhost:5173") // 접근 허용 @CrossOrigin는 모두 허용(보안 이슈)
		.allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
		.allowedHeaders("*")
		.allowCredentials(true);
	}
}