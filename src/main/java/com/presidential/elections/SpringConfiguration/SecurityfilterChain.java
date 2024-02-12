package com.presidential.elections.SpringConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityfilterChain {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers( "/", "/CandidateProfile", "/displayUserProfile","/DisplayImages", "/upload/**", "/error", "/register").permitAll().anyRequest().authenticated()
			)
			.formLogin((form) -> form
				.loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
				.permitAll()
			);

		return http.build();
	}
}