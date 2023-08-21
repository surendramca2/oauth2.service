package com.surendra.oauth.server.security.authorization.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.surendra.oauth.server.config.AuthorizationServerConfig;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Set;

/**
 * This is a custom override for {@link JdbcRegisteredClientRepository.RegisteredClientRowMapper}. Default implementation
 * adds scopes from the database and validate the scopes which fails because Ephesoft can have space separated scopes.
 * This implementation removes the scope from the default {@link RegisteredClient.Builder} and uses it own implementation
 * of {@link OauthRegisteredClient}
 *
 * @see OauthRegisteredClient
 * @see RegisteredClient
 * @see JdbcRegisteredClientRepository.RegisteredClientRowMapper
 * @see AuthorizationServerConfig#registeredClientRepository
 */
public class OAuthClientRowMapper extends JdbcRegisteredClientRepository.RegisteredClientRowMapper {

    public OAuthClientRowMapper() {
        super();
    }

    @Override
    public RegisteredClient mapRow(ResultSet rs, int rowNum) throws SQLException {
        Timestamp clientIdIssuedAt = rs.getTimestamp("client_id_issued_at");
        Timestamp clientSecretExpiresAt = rs.getTimestamp("client_secret_expires_at");
        Set<String> clientAuthenticationMethods = StringUtils.commaDelimitedListToSet(rs.getString("client_authentication_methods"));
        Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(rs.getString("authorization_grant_types"));
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(rs.getString("redirect_uris"));
        Set<String> clientScopes = StringUtils.commaDelimitedListToSet(rs.getString("scopes"));

        // @formatter:off
        RegisteredClient.Builder builder = RegisteredClient.withId(rs.getString("id"))
                .clientId(rs.getString("client_id"))
                .clientIdIssuedAt(clientIdIssuedAt != null ? clientIdIssuedAt.toInstant() : null)
                .clientSecret(rs.getString("client_secret"))
                .clientSecretExpiresAt(clientSecretExpiresAt != null ? clientSecretExpiresAt.toInstant() : null)
                .clientName(rs.getString("client_name"))
                .clientAuthenticationMethods((authenticationMethods) ->
                        clientAuthenticationMethods.forEach(authenticationMethod ->
                                authenticationMethods.add(resolveClientAuthenticationMethod(authenticationMethod))))
                .authorizationGrantTypes((grantTypes) ->
                        authorizationGrantTypes.forEach(grantType ->
                                grantTypes.add(resolveAuthorizationGrantType(grantType))))
                .redirectUris((uris) -> uris.addAll(redirectUris));
        // @formatter:on

        Map<String, Object> clientSettingsMap = parseMap(rs.getString("client_settings"));
        builder.clientSettings(ClientSettings.withSettings(clientSettingsMap).build());

        Map<String, Object> tokenSettingsMap = parseMap(rs.getString("token_settings"));
        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.withSettings(tokenSettingsMap);
        if (!tokenSettingsMap.containsKey(ConfigurationSettingNames.Token.ACCESS_TOKEN_FORMAT)) {
            tokenSettingsBuilder.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED);
        }
        builder.tokenSettings(tokenSettingsBuilder.build());
        // Return an implementation of RegisteredClient with clientScopes as comma separated instead of space separated
        // This helps skip from RegisteredClient.Builder.validateScopes
        return new OauthRegisteredClient(builder.build(), clientScopes);
    }

    private Map<String, Object> parseMap(String data) {
        try {
            return this.getObjectMapper().readValue(data, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage(), ex);
        }
    }

    private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }
        return new AuthorizationGrantType(authorizationGrantType);        // Custom authorization grant type
    }

    private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }
        return new ClientAuthenticationMethod(clientAuthenticationMethod);        // Custom client authentication method
    }

}
