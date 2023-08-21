package com.surendra.oauth.server.config;

import com.surendra.oauth.server.jose.Jwks;
import com.surendra.oauth.server.repository.Oauth2RegisteredClientRepository;
import com.surendra.oauth.server.security.OAuthAuditFilter;
import com.surendra.oauth.server.security.authorization.client.OAuthClientRowMapper;
import com.surendra.oauth.server.security.web.authentication.OAuthAccessTokenConverter;
import com.surendra.oauth.server.security.web.authentication.OAuthAuthenticationSuccessHandler;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.surendra.oauth.server.util.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

/**
 * Class used for authorization server configuration setup and initialization of beans.
 *
 * @author Surendra pal singh
 * @since 0.0.1
 */
@Configuration(proxyBeanMethods = false)
@EnableScheduling
public class AuthorizationServerConfig implements ResourceLoaderAware {


    @Value("${secret.value}")
    private String secretValue;

    @Value("${keystore.password}")
    private String keyStorePassword;

    @Value("${keystore.location}")
    private String keyStoreLocation;

    @Value("${key.password}")
    private String keyPassword;

    @Autowired
    private OAuthAuditFilter oAuthAuditFilter;

    @Autowired
    Oauth2RegisteredClientRepository oauth2RegisteredClientRepository;

    private ResourceLoader resourceLoader;

    /**
     * Cors configuration to allow clients to post on /oauth/token endpoint. This will enable javascript clients to
     * use POST and retrieve a token
     *
     * @return CorsConfigurationSource
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(Collections.singletonList("POST"));
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/oauth2/token", configuration);
        source.setPathMatcher(new AntPathMatcher());
        return source;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuthAccessTokenConverter OAuthAccessTokenConverter = new OAuthAccessTokenConverter(oauth2RegisteredClientRepository);
        http.cors().configurationSource(corsConfigurationSource());
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .tokenEndpoint(customizer -> {
                    customizer.accessTokenRequestConverters(OAuthAccessTokenConverter);
                    customizer.accessTokenResponseHandler(new OAuthAuthenticationSuccessHandler());
                })
                .oidc(Customizer.withDefaults());    // Enable OpenID Connect 1.0
        // @formatter:off
        http.addFilterAfter(oAuthAuditFilter, LogoutFilter.class)
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        // @formatter:on
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // @formatter:off
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
        registeredClientRepository.setRegisteredClientRowMapper(new OAuthClientRowMapper());
        return registeredClientRepository;
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        Resource resource = getResource(keyStoreLocation);
        RSAKey rsaKey = Jwks.generateRsa(resource, keyStorePassword, keyPassword);
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(ConstantUtil.DEFAULT_OAUTH_TOKEN_ISSUER_URI)
                .build();
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Resource getResource(String location) {
        return resourceLoader.getResource(location);
    }

}
