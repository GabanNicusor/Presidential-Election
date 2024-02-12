package com.presidential.elections;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.presidential.elections.Entities.User;
import com.presidential.elections.Repository.UserRepository;
import com.presidential.elections.storage.StorageProperties;
import com.presidential.elections.storage.StorageService;


@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class ElectionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElectionsApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepository userRepository, PasswordEncoder passwordEncode, StorageService storageService) {
		return args -> {
			storageService.init();
			if (userRepository.findById(1).isPresent() || userRepository.findByusername("admin").isPresent()) {return;}
			User user = new User(1, "admin", "$2a$10$UY1iT2Z4nVItbs6CpY/x/ef1JplNwkYU4g5pAnVfmdF4HVjDyDTa.");
			user.setNumberVotes(0);
			userRepository.save(user);
		};
	}
}
