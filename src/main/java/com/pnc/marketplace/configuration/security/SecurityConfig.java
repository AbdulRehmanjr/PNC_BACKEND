package com.pnc.marketplace.configuration.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.pnc.marketplace.configuration.jwt.JwtAuthenticationEntryPoint;
import com.pnc.marketplace.configuration.jwt.JwtAuthenticationFilter;
import com.pnc.marketplace.utils.UserDetailService;


@EnableWebSecurity
@Configuration
public class SecurityConfig  {

        @Autowired
        private JwtAuthenticationEntryPoint unauthorizedHandler;
        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;
        @Autowired
        private UserDetailService userDetailService;

        private String[] origins = { "http://localhost:4200" };

        private final String[] PUBLICURI= {"/user/create","/token/**","/category/**","/seller/**"};

        @Bean
        AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider dao = new DaoAuthenticationProvider();

                dao.setUserDetailsService(this.userDetailService);
                dao.setPasswordEncoder(this.encoder());
                return dao;
        }

        @Bean
        AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

                return config.getAuthenticationManager();
        }

        @Bean
        PasswordEncoder encoder() {
                return new BCryptPasswordEncoder(13);
        }

        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                .csrf((csrf)-> csrf.disable())
                .cors((cor)-> cor.configurationSource(this.corsConfigurationSource()))
                                .authorizeHttpRequests((req) -> req
                                                .requestMatchers(PUBLICURI).permitAll()
                                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                                .anyRequest().authenticated()
                                                                                      
                                                )
                                .exceptionHandling(
                                                (exception) -> exception.authenticationEntryPoint(unauthorizedHandler))
                                .sessionManagement((session) -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
	CorsConfigurationSource corsConfigurationSource() {
		   CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList(origins));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","PATCH"));
                configuration.setAllowedHeaders(Arrays.asList("*"));
                configuration.setAllowCredentials(true);
                configuration.setExposedHeaders(Arrays.asList("x-Auth-Token", "Acess-Control-Allow-Origin"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}