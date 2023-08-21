package com.surendra.oauth.server.config;

import com.surendra.oauth.server.security.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Class used for default security configuration for authorization server.
 * @author Surendra pal singh
 * @since 0.1.0
 */
@EnableWebSecurity
public class DefaultSecurityConfig {

    @Autowired
    private AuthTokenFilter authTokenFilter;

    // @formatter:off
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                .cors().and().
                csrf().disable();
        return http.build();
    }
    // @formatter:on

}
