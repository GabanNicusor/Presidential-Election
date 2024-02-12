package com.presidential.elections.SpringConfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	public void addViewControllers(@NonNull ViewControllerRegistry registry) {
		registry.addViewController("/error").setViewName("error_page");
		registry.addViewController("/login").setViewName("login");
	}

}