package org.tyaa.demo.spring.springreactmysqlmongo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.tyaa.demo.spring.springreactmysqlmongo.entities.Role;
import org.tyaa.demo.spring.springreactmysqlmongo.entities.User;
import org.tyaa.demo.spring.springreactmysqlmongo.repositories.RoleRepository;
import org.tyaa.demo.spring.springreactmysqlmongo.repositories.UserRepository;

import java.math.BigDecimal;

@SpringBootApplication
public class SpringReactMysqlMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringReactMysqlMongoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(
			RoleRepository roleRepository,
			UserRepository userRepository
	) {
		return args -> {
			roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
			roleRepository.save(Role.builder().name("ROLE_USER").build());
			Role adminRole = roleRepository.findRoleByName("ROLE_ADMIN");
			Role userRole = roleRepository.findRoleByName("ROLE_USER");
			userRepository.save(
					User.builder()
							.name("admin")
							.password("AdminPassword1")
							.role(adminRole)
							.build()
			);
			userRepository.save(
					User.builder()
							.name("one")
							.password("UserPassword1")
							.role(userRole)
							.build()
			);
			userRepository.save(
					User.builder()
							.name("two")
							.password("UserPassword2")
							.role(userRole)
							.build()
			);
			userRepository.save(
					User.builder()
							.name("three")
							.password("UserPassword3")
							.role(userRole)
							.build()
			);
		};
	}
}
