package com.presidential.elections;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.presidential.elections.Entities.Role;
import com.presidential.elections.Entities.User;
import com.presidential.elections.Repository.RoleRepository;
import com.presidential.elections.Repository.UserRepository;

@SpringBootApplication
public class ElectionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElectionsApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserRepository userRepository, RoleRepository repository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (repository.findByAuthority("ADMIN").isPresent()) return;
			
			Role adminrole = repository.save(new Role("ADMIN"));
			repository.save(new Role("USER"));

			Set<Role> roles = new HashSet<>();
			roles.add(adminrole);

			User user = new User(1, "admin", "$2a$10$UY1iT2Z4nVItbs6CpY/x/ef1JplNwkYU4g5pAnVfmdF4HVjDyDTa.", roles);
			userRepository.save(user);
		};
	}
	
}
