package com.example.authclient;

import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class AuthclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthclientApplication.class, args);
	}

}

@Controller
@ResponseBody
class GreetingsController {

	private final GreetingsService service;

	public GreetingsController(GreetingsService service) {
		this.service = service;
	}

	@GetMapping("/")
	Map<String, String> getGreetingForAll(){
		return this.service.greetAll();
	}

	@GetMapping("/user")
	Map<String, String> getUserGreeting(){
		return this.service.greetUser();
	}

	@GetMapping("/admin")
	Map<String, String> getAdminGreeting(){
		return this.service.greetAdmin();
	}

}

@EnableMethodSecurity
@Configuration
@Service
class GreetingsService {

	public Map<String, String> greetAll(){
		return Map.of("Message", "Hello everyone!");
	}

	@PreAuthorize("hasAuthority('SCOPE_user.read')")
	public Map<String, String> greetUser(){
		var jwt = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return Map.of("Message", "Hello " + jwt.getSubject());
	}

	@PreAuthorize("hasAuthority('SCOPE_admin.read')")
	public Map<String, String> greetAdmin(){
		var jwt = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return Map.of("Message", "Hello " + jwt.getSubject());
	}

}
