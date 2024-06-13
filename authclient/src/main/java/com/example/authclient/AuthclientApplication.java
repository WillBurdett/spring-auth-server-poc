package com.example.authclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

/**
 * Gateway is acting as our oAuth2 client
 * it's going to manage tokens for us
 * we'll proxy a request over to our resource-server using gateway
 * So we define 1 route that goes to our greeting end point and that's it
 *
 */

@SpringBootApplication
public class AuthclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthclientApplication.class, args);
	}

	@Bean
	RouteLocator gateway(RouteLocatorBuilder rlb){
		return rlb
				.routes()
				.route(rs -> rs
						// this path will not only reachable by this app (127.0.0.1:8082/hello)...
						// ... but also be appended to the 8081 uri below
						.path("/hello")
						.filters(GatewayFilterSpec::tokenRelay)
						.uri("http://localhost:8081"))
				.build();
	}

}
