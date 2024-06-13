package com.example.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@SpringBootApplication
public class AuthserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthserverApplication.class, args);
	}

	/**
	 * Remember, we are not logging in as the client (as an app!)
	 * We want to log-in as a user and by default only one is provided (username=user and password={generated in output})
	 *
	 */

	@Bean
	InMemoryUserDetailsManager inMemoryUserDetailsManager() {
		var one = User.withDefaultPasswordEncoder().roles("admin").username("admin").password("pw").build();
		var two = User.withDefaultPasswordEncoder().roles("user").username("user").password("pw").build() ;
		return new InMemoryUserDetailsManager(one, two);
	}

}
