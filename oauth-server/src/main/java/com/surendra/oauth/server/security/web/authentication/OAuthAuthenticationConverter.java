package com.surendra.oauth.server.security.web.authentication;

import com.surendra.oauth.server.domain.Oauth2RegisteredClient;
import com.surendra.oauth.server.repository.Oauth2RegisteredClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A modified implementation of {@link org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter}
 * Since the default implementation accepts multiple scope values with space separated, it does not work with Transact since
 * transact roles are mapped with OAuth scope and transact roles can have spaces. In order to overcome this issue this
 * implementation accepts scope with comma separated values and allows to have spaces in scope.
 * In addition to that, if no scope are present in the access token request (/oauth2/token) it will add all the scopes
 * defined for the client in the database
 *
 * @see OAuthAccessTokenConverter
 * @see org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter
 */
public class OAuthAuthenticationConverter implements AuthenticationConverter {

    private static final Logger log =
            LoggerFactory.getLogger(OAuthAuthenticationConverter.class);
    private final Oauth2RegisteredClientRepository oauth2RegisteredClientRepository;

    public OAuthAuthenticationConverter(Oauth2RegisteredClientRepository oauth2RegisteredClientRepository) {
        this.oauth2RegisteredClientRepository = oauth2RegisteredClientRepository;
    }


    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
        // grant_type (REQUIRED)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(grantType)) {
            return null;
        }

        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        MultiValueMap<String, String> parameters = OAuthEndpointUtils.getParameters(request);

        // scope (OPTIONAL)
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) &&
                parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            OAuthEndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ParameterNames.SCOPE,
                    OAuthEndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {
            //Look for comma (,) instead of a space to split the scope
            requestedScopes = new HashSet<>(
                    Arrays.asList(StringUtils.delimitedListToStringArray(scope, ",")));
        } else {
            // If scope are not provided by client in the oauth2 access token request, then add all the scopes
            // for the client
            String clientId;

            if (clientPrincipal != null && clientPrincipal.getPrincipal() != null) {
                clientId = clientPrincipal.getPrincipal().toString();
            } else {
                clientId = parameters.getFirst(OAuth2ParameterNames.CLIENT_ID);
            }

            if (StringUtils.hasText(clientId)) {
                Oauth2RegisteredClient client = oauth2RegisteredClientRepository.findByClientId(clientId);
                if (StringUtils.hasText(client.getScopes())) {
                    requestedScopes = new HashSet<>(
                            Arrays.asList(StringUtils.delimitedListToStringArray(client.getScopes(), ",")));
                } else {
                    log.error("Client {} has no scopes defined in the database", clientId);
                }
            } else {
                log.error("Unable to determine ClientID from the Access token request");
            }
        }

        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE) &&
                    !key.equals(OAuth2ParameterNames.SCOPE)) {
                additionalParameters.put(key, value.get(0));
            }
        });

        return new OAuth2ClientCredentialsAuthenticationToken(
                clientPrincipal, requestedScopes, additionalParameters);
    }
}
