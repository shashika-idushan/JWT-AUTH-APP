package com.authapp.config;

import com.authapp.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter authenticationFilter;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests((authorize) -> {
//                    authorize.requestMatchers("/api/auth/**").permitAll();
//                    authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
//                    authorize.requestMatchers("/auth/admin/**").hasAuthority("ROLE_ADMIN");
//                    authorize.anyRequest().authenticated();
//                }).httpBasic(Customizer.withDefaults());
//
//        http.exceptionHandling(exception -> exception
//                .authenticationEntryPoint(authenticationEntryPoint));
//
//        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        // Enable CORS
//        http.cors(Customizer.withDefaults());
//
//        return http.build();
//    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> {
                    authorize.requestMatchers("/api/auth/**").permitAll();
                    authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    authorize.requestMatchers("/auth/admin/**").hasAuthority("ROLE_ADMIN");
                    authorize.requestMatchers("/auth/user/**").hasAuthority("ROLE_USER");
                    authorize.anyRequest().authenticated();
                })
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults());
        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    // CORS configuration bean
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Frontend origin
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true); // Allow credentials like cookies

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}