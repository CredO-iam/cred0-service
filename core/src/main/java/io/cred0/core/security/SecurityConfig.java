package io.cred0.core.security;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final String LOGIN_ENDPOINT = "/login";

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) {
        http
                .securityMatcher(PathRequest.toH2Console())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .csrf(csrf -> csrf.ignoringRequestMatchers(PathRequest.toH2Console()))
                .headers(headersCustomizer);

        return http.build();
    }

    @Bean
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(LOGIN_ENDPOINT).permitAll()
                        .requestMatchers("/admin/**").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginProcessingUrl(LOGIN_ENDPOINT)
                        .permitAll()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/admin/**", LOGIN_ENDPOINT));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    private final Customizer<HeadersConfigurer<HttpSecurity>> headersCustomizer =
            headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);


}

