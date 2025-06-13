package com.adorsys.webank.obs.config;

import com.adorsys.webank.config.CertValidator;
import com.adorsys.webank.security.CustomJwtAuthenticationConverter;
import com.adorsys.webank.security.extractor.RequestParameterExtractorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import com.adorsys.webank.domain.Role;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Import({
        com.adorsys.webank.security.extractor.RequestParameterExtractorFilter.class,
        com.adorsys.webank.config.CertValidator.class,
        com.adorsys.webank.config.JwtExtractor.class,
        com.adorsys.webank.config.JwtValidator.class,
        com.adorsys.webank.config.KeyLoader.class

})
public class ObsSecurityConfig {

    @Autowired
    private RequestParameterExtractorFilter requestParameterExtractorFilter;

    @Autowired
    private CertValidator certValidator;

    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSources()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(requestParameterExtractorFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/registration/**").hasAuthority(Role.DEVICE_CERT.getRoleName())
                        .requestMatchers("/api/accounts/**").hasAuthority(Role.ACCOUNT_CERTIFIED.getRoleName())
                        .requestMatchers("/api/accounts/payout/**").hasAnyAuthority(Role.ACCOUNT_CERTIFIED.getRoleName(), Role.KYC_CERT.getRoleName())
                        .requestMatchers("/api/accounts/recovery/**").authenticated()
                        .requestMatchers("/swagger-ui.html/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverted())));

        http.headers(headers -> headers
                .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).preload(true).maxAgeInSeconds(31536000))
                .referrerPolicy(referrer -> referrer.policy(ReferrerPolicy.SAME_ORIGIN))
                .frameOptions(FrameOptionsConfig::sameOrigin)
                .permissionsPolicy(policy -> policy.policy("geolocation=(), microphone=(), camera=(self)")));

        return http.build();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSources() {
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public CustomJwtAuthenticationConverter jwtAuthenticationConverted() {
        return new CustomJwtAuthenticationConverter(certValidator);
    }
}