package io.tech.AdvancedSpringBootSecurityWithJwt;

import io.tech.AdvancedSpringBootSecurityWithJwt.models.Roles;
import io.tech.AdvancedSpringBootSecurityWithJwt.models.Users;
import io.tech.AdvancedSpringBootSecurityWithJwt.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class AdvancedSpringBootSecurityWithJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdvancedSpringBootSecurityWithJwtApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserService userService){
		return args -> {
			userService.SaveRole(new Roles(null , "ROLE_USER"));
			userService.SaveRole(new Roles(null , "ROLE_MANAGER"));
			userService.SaveRole(new Roles(null , "ROLE_ADMIN"));
			userService.SaveRole(new Roles(null , "ROLE_SUPER_ADMIN"));

			userService.SaveUser(new Users(null , "John" , "john@gmail.com" , "12345", new ArrayList<>()));
			userService.SaveUser(new Users(null , "Paul" , "paul@gmail.com" , "12345", new ArrayList<>()));
			userService.SaveUser(new Users(null , "Raj" , "raj@gmail.com" , "12345", new ArrayList<>()));
			userService.SaveUser(new Users(null , "Ravi" , "ravi@gmail.com" , "12345", new ArrayList<>()));

			userService.AddRoleToUsers("john@gmail.com" ,"ROLE_USER" );
			userService.AddRoleToUsers("john@gmail.com" ,"ROLE_MANAGER" );
			userService.AddRoleToUsers("john@gmail.com" ,"ROLE_ADMIN" );
			userService.AddRoleToUsers("john@gmail.com" ,"ROLE_SUPER_ADMIN" );
			userService.AddRoleToUsers("paul@gmail.com" ,"ROLE_USER" );
			userService.AddRoleToUsers("raj@gmail.com" ,"ROLE_USER" );
			userService.AddRoleToUsers("ravi@gmail.com" ,"ROLE_USER" );
			userService.AddRoleToUsers("ravi@gmail.com" ,"ROLE_MANAGER" );
			userService.AddRoleToUsers("ravi@gmail.com" ,"ROLE_ADMIN" );
		};
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}



}
