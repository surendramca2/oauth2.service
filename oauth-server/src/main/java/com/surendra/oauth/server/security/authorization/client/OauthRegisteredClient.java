package com.surendra.oauth.server.security.authorization.client;

import com.surendra.oauth.server.config.AuthorizationServerConfig;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.io.Serializable;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.Set;

/**
 * A cutom implementation of {@link RegisteredClient} to skip the {@link RegisteredClient.Builder#validateScope(String)}
 * method which checks for spaces in scope. This implementation helps adding scope to the RegisteredClient after the
 * validateScope method is called. This is used during access token (oauth2/token) api call.
 *
 * @see RegisteredClient
 * @see OAuthClientRowMapper
 * @see AuthorizationServerConfig#registeredClientRepository
 * @see JdbcRegisteredClientRepository.RegisteredClientRowMapper#mapRow(ResultSet, int)
 */
public class OauthRegisteredClient extends RegisteredClient implements Serializable {

    private RegisteredClient delegate;
    private Set<String> scopes;

    public OauthRegisteredClient(RegisteredClient delegate, Set<String> scopes) {
        this.delegate = delegate;
        this.scopes = scopes;
    }

    public String getId() {
        return delegate.getId();
    }

    public String getClientId() {
        return delegate.getClientId();
    }

    @Nullable
    public Instant getClientIdIssuedAt() {
        return delegate.getClientIdIssuedAt();
    }

    @Nullable
    public String getClientSecret() {
        return delegate.getClientSecret();
    }

    @Nullable
    public Instant getClientSecretExpiresAt() {
        return delegate.getClientSecretExpiresAt();
    }

    public String getClientName() {
        return delegate.getClientName();
    }

    public Set<ClientAuthenticationMethod> getClientAuthenticationMethods() {
        return delegate.getClientAuthenticationMethods();
    }

    public Set<AuthorizationGrantType> getAuthorizationGrantTypes() {
        return delegate.getAuthorizationGrantTypes();
    }

    public Set<String> getRedirectUris() {
        return delegate.getRedirectUris();
    }

    public Set<String> getScopes() {
        return this.scopes;
    }

    public ClientSettings getClientSettings() {
        return delegate.getClientSettings();
    }

    public TokenSettings getTokenSettings() {
        return delegate.getTokenSettings();
    }
}
