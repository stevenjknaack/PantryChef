package com.pantrychef.backend.configs;

import com.pantrychef.backend.filters.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configurations
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    private JWTAuthenticationFilter jWTAuthFilter;
    @Autowired
    private AuthenticationProvider authenticationProvider;

    /**
     * Determines which endpoints are accessible at a certain authorization level
     * @param http The HttpSecurity object that the configuration will apply to
     * @return The chain of security filters
     * @throws Exception Possible Exception from the building of http
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http //TODO update
                .csrf().disable()
                .authorizeHttpRequests()
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/recipes/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/reviews").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/recipes/{id}/likes").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/reviews/{id}/likes").permitAll()
                    .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                    .addFilterBefore(jWTAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
