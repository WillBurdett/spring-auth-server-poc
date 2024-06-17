package com.example.authclient;

import java.security.Principal;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
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
	Map<String, String> getGreeting(){
		return this.service.greet();
	}
}

@EnableMethodSecurity
@Service
class GreetingsService {

	@PreAuthorize("hasAuthority('SCOPE_user')")
	public Map<String, String> greet(){
		var jwt = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return Map.of("Message", "Hello " + jwt.getSubject());
	}

}
