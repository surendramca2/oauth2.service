package com.surendra.oauth.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="oauth2_registered_client")
public class Oauth2RegisteredClient implements Serializable {

    private static final long serialVersionUID =1L;

    @Id
    @Column(nullable = false,length = 100)
    private String  id;

    @Column(name="client_id",nullable = false,length = 100)
    private String  clientId;

    @Column(name="client_id_issued_at", nullable = false, updatable=false)
    private Date  clientIdIssuedAt;

    @Column(name="client_secret",length = 200)
    private String  clientSecret;

    @Column(name="client_secret_expires_at")
    private Date  clientSecretExpiresAt;

    @Column(name="client_name",nullable = false,length = 200)
    private String  clientName;

    @Column(name="client_authentication_methods",nullable = false,length = 1700)
    private String  clientAuthenticationMethods;

    @Column(name="authorization_grant_types",nullable = false,length = 1700)
    private String  authorizationGrantTypes;

    @Column(name="redirect_uris",length = 1700)
    private String  redirectUris;

    @Column(name="scopes",nullable = false,length = 1700)
    private String  scopes;

    @Column(name="client_settings",nullable = false,length = 2000)
    private String  clientSettings;

    @Column(name="token_settings",nullable = false,length = 2000)
    private String  tokenSettings;


   public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Date getClientIdIssuedAt() {
        return clientIdIssuedAt;
    }

    public void setClientIdIssuedAt(Date clientIdIssuedAt) {
        this.clientIdIssuedAt = clientIdIssuedAt;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Date getClientSecretExpiresAt() {
        return clientSecretExpiresAt;
    }

    public void setClientSecretExpiresAt(Date clientSecretExpiresAt) {
        this.clientSecretExpiresAt = clientSecretExpiresAt;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAuthenticationMethods() {
        return clientAuthenticationMethods;
    }

    public void setClientAuthenticationMethods(String clientAuthenticationMethods) {
        this.clientAuthenticationMethods = clientAuthenticationMethods;
    }

    public String getAuthorizationGrantTypes() {
        return authorizationGrantTypes;
    }

    public void setAuthorizationGrantTypes(String authorizationGrantTypes) {
        this.authorizationGrantTypes = authorizationGrantTypes;
    }

    public String getRedirectUris() {
        return redirectUris;
    }

    public void setRedirectUris(String redirectUris) {
        this.redirectUris = redirectUris;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String getClientSettings() {
        return clientSettings;
    }

    public void setClientSettings(String clientSettings) {
        this.clientSettings = clientSettings;
    }

    public String getTokenSettings() {
        return tokenSettings;
    }

    public void setTokenSettings(String tokenSettings) {
        this.tokenSettings = tokenSettings;
    }
}
