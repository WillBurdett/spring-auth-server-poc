package com.example.authserver;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.sql.DataSource;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@SpringBootApplication
public class AuthserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthserverApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}

@Configuration
class UserConfiguration{

	@Bean
	JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
		return new JdbcUserDetailsManager(dataSource);
	}

	@Bean
	ApplicationRunner usersRunner(PasswordEncoder passwordEncoder, UserDetailsManager userDetailsManager){
		return args -> {
			var builder = User.builder().roles("USER").passwordEncoder(passwordEncoder::encode);
			var users = Map.of(
					"user", "pw",
					"admin", "pw");
			users.forEach((username, password) -> {
				if (!userDetailsManager.userExists(username)){
					var user = builder
							.username(username)
							.password(password)
							.build();
					userDetailsManager.createUser(user);
				}
			});
		};
	}

}

@Configuration
class ClientsConfiguration{

	@Bean
	RegisteredClientRepository registeredClientRepository(JdbcTemplate template){
		return new JdbcRegisteredClientRepository(template);
	}

	@Bean
	ApplicationRunner clientsRunner(RegisteredClientRepository repository){
		return args -> {
			var clientId = "client";
			if (repository.findByClientId(clientId) == null){
				repository.save(
						RegisteredClient
								.withId(UUID.randomUUID().toString())
								.clientId(clientId)
								.clientSecret("{bcrypt}$2a$10$jdJGhzsiIqYFpjJiYWMl/eKDOd8vdyQis2aynmFN0dgJ53XvpzzwC")
								.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
								.authorizationGrantTypes(grantTypes -> grantTypes.addAll(Set.of(
										AuthorizationGrantType.CLIENT_CREDENTIALS,
										AuthorizationGrantType.AUTHORIZATION_CODE,
										AuthorizationGrantType.REFRESH_TOKEN)))
								.redirectUri("http://127.0.0.1:8082/login/oauth2/code/spring")
								.scopes(scopes -> scopes.addAll(Set.of("user.read", "user.write", OidcScopes.OPENID)))
								.build()
				);
			}
		};
	}
}
