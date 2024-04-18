//package com.web.springmvc.newsweb.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//
//import static jakarta.servlet.DispatcherType.ERROR;
//import static jakarta.servlet.DispatcherType.FORWARD;
//import static org.springframework.http.HttpMethod.GET;
//import static org.springframework.http.HttpMethod.POST;
//import static org.springframework.http.HttpMethod.PUT;
//import static org.springframework.http.HttpMethod.DELETE;
//
//@Configuration
////@EnableWebSecurity(debug = true)
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//    private final AuthenticationProvider authenticationProvider;
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request -> request
//                        .dispatcherTypeMatchers(FORWARD, ERROR).permitAll()
//                        .requestMatchers("/api/auth/**").permitAll()
//                        .requestMatchers(GET, "/api/news/**", "/api/comment/**", "/api/user/**", "/api/category/**").hasAnyAuthority("USER","ADMIN")
//                        .requestMatchers(POST, "/api/news/**", "/api/comment/**", "/api/user/**").hasAnyAuthority("USER","ADMIN")
//                        .requestMatchers(POST, "/api/category/**").hasAuthority("ADMIN")
//                        .requestMatchers(PUT, "/api/news/**", "/api/comment/**", "/api/user/**").hasAnyAuthority("USER","ADMIN")
//                        .requestMatchers(PUT, "/api/category/**").hasAuthority("ADMIN")
//                        .requestMatchers(DELETE, "/api/news/**", "/api/comment/**").hasAnyAuthority("USER","ADMIN")
//                        .requestMatchers(DELETE, "/api/category/**", "/api/user/**").hasAuthority("ADMIN")
//                        .anyRequest().authenticated())
//                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//}
