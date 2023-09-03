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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private JwtAuthenticationEntryPoint unauthorizedHandler;

        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Autowired
        private UserDetailService userDetailService;

        private String[] origins = { "http://localhost:4200" };

        private final String[] PUBLICURI = { "/ws/**","/socket.io/**", "/user/**", "/message/**", "/chatlist/**", "/token/**",
                        "/product/**", "/webhook/**", "/category/**", "/checkout/**", "/seller/**",
                        "/sellerrequest/**" };

        /**
         * The function returns an instance of an AuthenticationProvider with a
         * configured
         * UserDetailsService and PasswordEncoder.
         * 
         * @return The method is returning an instance of the AuthenticationProvider
         *         interface.
         */
        @Bean
        AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
                dao.setUserDetailsService(this.userDetailService);
                dao.setPasswordEncoder(this.encoder());
                return dao;
        }

        /**
         * The function returns an instance of the AuthenticationManager interface.
         * 
         * @param config The `config` parameter is an instance of
         *               `AuthenticationConfiguration`. It is
         *               used to configure and create an `AuthenticationManager` bean.
         * @return The method is returning an instance of the AuthenticationManager
         *         interface.
         */
        @Bean
        AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

                return config.getAuthenticationManager();
        }

        /**
         * The function returns a BCryptPasswordEncoder object with a strength of 13.
         * 
         * @return The method is returning an instance of the BCryptPasswordEncoder
         *         class, which is a
         *         PasswordEncoder implementation that uses the BCrypt hashing algorithm
         *         with a strength factor
         *         of 13.
         */
        @Bean
        PasswordEncoder encoder() {
                return new BCryptPasswordEncoder(13);
        }

        /**
         * This function configures the security filter chain for a Java application,
         * specifying
         * authorization rules and adding a JWT authentication filter.
         * 
         * @param http The `http` parameter is an instance of `HttpSecurity`, which is
         *             used to
         *             configure the security settings for the application.
         * @return The method is returning a SecurityFilterChain object.
         */
        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                .csrf((csrf) -> csrf.disable())
                                .cors((cor) -> cor.configurationSource(this.corsConfigurationSource()))
                                .authorizeHttpRequests((req) -> req
                                                .requestMatchers(PUBLICURI).permitAll()
                                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                                .anyRequest().authenticated())
                                .exceptionHandling(
                                                (exception) -> exception.authenticationEntryPoint(unauthorizedHandler))
                                .sessionManagement((session) -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        /**
         * This function sets up CORS configuration for a Java application, allowing
         * specified origins,
         * methods, headers, and credentials.
         * 
         * @return The method is returning a CorsConfigurationSource object.
         */
        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                // Allow all origins (change this in production!)
                configuration.setAllowedOrigins(Arrays.asList(origins));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("*"));
                configuration.setAllowCredentials(true);
                configuration.setExposedHeaders(
                                Arrays.asList("x-Auth-Token", "Access-Control-Allow-Origin", "Authorization"));

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

}