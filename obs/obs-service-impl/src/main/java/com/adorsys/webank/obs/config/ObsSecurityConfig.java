package com.adorsys.webank.obs.config;

import com.adorsys.webank.config.*;
import com.adorsys.webank.domain.*;
import com.adorsys.webank.domain.Role;
import com.adorsys.webank.exceptions.*;
import com.adorsys.webank.security.*;
import com.adorsys.webank.security.extractor.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.*;
import org.springframework.security.config.http.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.*;
import org.springframework.security.web.header.writers.*;
import org.springframework.web.cors.*;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Import({
        RequestParameterExtractorFilter.class,
        CertValidator.class,
        JwtExtractor.class,
        JwtValidator.class,
        KeyLoader.class

})
public class ObsSecurityConfig {

    @Autowired
    private RequestParameterExtractorFilter requestParameterExtractorFilter;

    @Autowired
    private CertValidator certValidator;

    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http){

        try {
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
                            .requestMatchers("/swagger-ui.html/**", "/v3/api-docs/**", "/swagger-ui/**", "/api/actuator/**").permitAll()
                            .requestMatchers("/h2-console/**").permitAll()
                            .anyRequest().authenticated())
                    .oauth2ResourceServer(oauth2 -> oauth2
                            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverted())
                            )
                    );


            http.headers(headers -> headers
                    .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                    .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).preload(true).maxAgeInSeconds(31536000))
                    .referrerPolicy(referrer -> referrer.policy(ReferrerPolicy.SAME_ORIGIN))
                    .frameOptions(FrameOptionsConfig::sameOrigin)
                    .permissionsPolicy(policy -> policy.policy("geolocation=(), microphone=(), camera=(self)")));

            return http.build();
        } catch (Exception e) {
            throw new SecurityConfigurationException("Error configuring security filter chain", e);
        }
    }
    public CorsConfigurationSource corsConfigurationSources() {
       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Custom JWT authentication converter that uses the CertValidator to validate the JWT.
     *
     * @return CustomJwtAuthenticationConverter
     */

    @Bean
    public CustomJwtAuthenticationConverter jwtAuthenticationConverted() {
        return new CustomJwtAuthenticationConverter(certValidator);
    }
}