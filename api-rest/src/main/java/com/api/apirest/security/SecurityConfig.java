//package com.api.apirest.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    @SuppressWarnings("unused")
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .httpBasic()
//                .and()
//                .authorizeHttpRequests()
//                .requestMatchers(HttpMethod.GET, "/api-rest/").authenticated()
////                .requestMatchers(HttpMethod.GET, "/api/authentication//listAllUserNoPages").authenticated()
////                .requestMatchers(HttpMethod.POST, "/api/authentication/login").authenticated()
//                .anyRequest().permitAll()
//                .and()
//                .csrf().disable();
//
//        return  http.build();
//    }
//
//        public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
