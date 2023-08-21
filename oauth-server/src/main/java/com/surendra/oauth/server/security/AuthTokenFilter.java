package com.surendra.oauth.server.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.collect.Sets;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    Logger log = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    public TokenUtil tokenUtil;

    @Autowired
    private AuthEntryPoint authEntryPoint;

    private final RequestMatcher uriMatcher =
            new AntPathRequestMatcher("/api/management/**");

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Collection<SimpleGrantedAuthority> authorities = Sets.newHashSet();
        authorities.add(new SimpleGrantedAuthority("TransactAuthority"));

        try {
            String token = tokenUtil.parseToken(request);
            if (Objects.nonNull(token) && !token.equals("") && Boolean.TRUE.equals(tokenUtil.validateToken(token))) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("TransactUsername", token, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                SecurityContextHolder.clearContext();
                authEntryPoint.commence(request, response, new AuthenticationException("invalid token error") {
                });
                return;
            }
        } catch (Exception e) {
            log.error(String.format("Authentication could not be set: %s",
                    e.getMessage()));
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        RequestMatcher matcher = new NegatedRequestMatcher(uriMatcher);
        return matcher.matches(request);
    }

}
