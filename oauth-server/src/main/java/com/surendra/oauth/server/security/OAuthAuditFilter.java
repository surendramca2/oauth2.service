package com.surendra.oauth.server.security;

import com.surendra.oauth.server.api.service.Oauth2ClientService;
import com.surendra.oauth.server.constant.OAuthConstants;
import com.surendra.oauth.server.logger.OAuthLoggerFactory;
import com.surendra.oauth.server.logger.OauthAuditLogFormatter;
import com.surendra.oauth.server.logger.OauthAuditLogger;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Audit filter responsible to log all the requests received on /oauth2/token endpoint. This filter logs audit logs in
 * a separate audit log file defined in the logging configuration using {@link OauthAuditLogger}
 */
@Component
public class OAuthAuditFilter extends OncePerRequestFilter {

    private static final String TokenEndpoint = "/oauth2/token";

    @Autowired
    Oauth2ClientService oauth2ClientService;

    private static final OauthAuditLogger LOGGER = OAuthLoggerFactory.getLogger(OAuthAuditFilter.class);
    private static final Logger log = LoggerFactory.getLogger(OAuthAuditFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("Executing OAuthAudit Log Filter");
        String clientId = "";
        String scopes = "";
        boolean validEndpoint = request.getRequestURI().toLowerCase().endsWith(TokenEndpoint);
        log.debug("Validating API endpoint: {}, isValid: {}", request.getRequestURI(), validEndpoint);
        // Capture requests before those are sent further for access token only for /token endpoint
        if (validEndpoint) {
            clientId = getClientIdFromRequest(request);
            scopes = getScopesFromRequest(request);
            accessTokenRequested(clientId, scopes);
        }

        filterChain.doFilter(request, response);

        // Capture response before it is sent to the client for /token endpoint
        if (validEndpoint) {
            if (response.getStatus() == HttpServletResponse.SC_OK) {
                accessTokenResponseSucceeded(clientId, scopes);
            } else if (response.getStatus() == HttpServletResponse.SC_UNAUTHORIZED || response.getStatus() == HttpServletResponse.SC_FORBIDDEN) {
                accessTokenResponseAccessDenied(clientId, scopes);
            } else {
                accessTokenResponseFailed(clientId, scopes);
            }
        }
    }

    /**
     * Audit log access token request for a client received on /oauth2/token endpoint
     *
     * @param clientId ClientId received in request
     * @param scopes   scopes received in the request
     */
    void accessTokenRequested(String clientId, String scopes) {
        if (StringUtils.isNotBlank(clientId)) {
            log.debug("Access token request for clientId: {}", clientId);
            String logCode = OAuthConstants.OAUTH_ACCESS_TOKEN_REQUESTED.name();
            String auditLog = OauthAuditLogFormatter.formatAuditLog(logCode, clientId, "", "", "", scopes, "");
            LOGGER.oAuthAudit(auditLog);
        }
    }

    /**
     * Audit log access token successful response for a client for /oauth2/token endpoint. Additionally log
     * TokenId created in the database for the issued token
     *
     * @param clientId ClientId received in request
     * @param scopes   scopes received in the request
     */
    void accessTokenResponseSucceeded(String clientId, String scopes) {
        if (StringUtils.isNotBlank(clientId)) {
            log.debug("Access token issued for access token request for clientId: {}", clientId);
            String tokenId = oauth2ClientService.getLastTokenIdByClientId(clientId);
            String logCode = OAuthConstants.OAUTH_ACCESS_TOKEN_REQUEST_SUCCEEDED.name();
            String auditLog = OauthAuditLogFormatter.formatAuditLog(logCode, clientId, "", tokenId, "", scopes, "");
            LOGGER.oAuthAudit(auditLog);
        }
    }

    /**
     * Audit log access token request denied for a client for /oauth2/token endpoint.
     *
     * @param clientId ClientId received in request
     * @param scopes   scopes received in the request
     */
    void accessTokenResponseAccessDenied(String clientId, String scopes) {
        if (StringUtils.isNotBlank(clientId)) {
            log.debug("Access denied for access token request for clientId: {}", clientId);
            String logCode = OAuthConstants.OAUTH_ACCESS_TOKEN_REQUEST_ACCESS_DENIED.name();
            String auditLog = OauthAuditLogFormatter.formatAuditLog(logCode, clientId, "", "", "", scopes, "");
            LOGGER.oAuthAudit(auditLog);
        }
    }

    /**
     * Audit log access token failed response for a client for /oauth2/token endpoint.
     *
     * @param clientId ClientId received in request
     * @param scopes   scopes received in the request
     */
    void accessTokenResponseFailed(String clientId, String scopes) {
        if (StringUtils.isNotBlank(clientId)) {
            log.debug("Request failed for access token request for clientId: {}", clientId);
            String logCode = OAuthConstants.OAUTH_ACCESS_TOKEN_REQUEST_FAILED.name();
            String auditLog = OauthAuditLogFormatter.formatAuditLog(logCode, clientId, "", "", "", scopes, "");
            LOGGER.oAuthAudit(auditLog);
        }
    }

    /**
     * Get {@link OAuth2ParameterNames#CLIENT_ID} parameter from request parameters
     *
     * @param request HttpServletRequest
     * @return ClientId parameter value
     */
    static String getClientIdFromRequest(HttpServletRequest request) {
        String clientId = request.getParameter(OAuth2ParameterNames.CLIENT_ID);
        if (StringUtils.isNotBlank(clientId)) {
            return clientId;
        }
        log.info("No clientId was found in the request");
        return null;
    }

    /**
     * Get {@link OAuth2ParameterNames#SCOPE} parameter from request parameters
     *
     * @param request HttpServletRequest
     * @return Comma separated scopes
     */
    static String getScopesFromRequest(HttpServletRequest request) {
        String requestScopes = request.getParameter(OAuth2ParameterNames.SCOPE);
        if (StringUtils.isNotBlank(requestScopes)) {
            return requestScopes;
        }
        log.info("No scopes were found in the request");
        return "";
    }
}
