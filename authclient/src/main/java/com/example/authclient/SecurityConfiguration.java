
// ----- UNUSED -----

//package com.example.authclient;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//@Configuration
//@EnableMethodSecurity
//class SecurityConfiguration {
//
//  @Bean
//  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/**")
//            .authenticated()
//            .anyRequest()
//            .permitAll())
//        .oauth2Login(Customizer.withDefaults())//<4>
//        .oauth2Client(Customizer.withDefaults());
//    return http.build();
//  }
//
//
//}
