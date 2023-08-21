package com.surendra.oauth.server.domain;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="oauth2_authorization")
public class Oauth2Authorization implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(nullable = false)
    private String id;

    @Column(name="registered_client_id",nullable = false,length =100)
    private String registeredClientId;

    @Column(name="principal_name",nullable = false,length = 200)
    private String principalName;

    @Column(name="authorization_grant_type",nullable = false)
    private String authorizationGrantType;

    @Column(name="authorized_scopes",length = 1800)
    private String authorizedScopes;

    @Column(name="attributes", length = 1800)
    private String attributes;

    @Column(name="state",length = 500)
    private String state;

    @Column(name="authorization_code_value", length = 1800)
    private String authorizationCodeValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="authorization_code_issued_at")
    private Date authorizationCodeIssuedAt;

    @Column(name="authorization_code_expires_at")
    private String authorizationCodeExpiresAt;

    @Column(name="authorization_code_metadata", length = 1800)
    private String authorizationCodeMetadata;

    @Column(name="access_token_value", length = 1800 )
    private String accessTokenValue;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="access_token_issued_at")
    private Date accessTokenIssuedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="access_token_expires_at")
    private Date accessTokenExpiresAt;

    @Column(name="access_token_metadata", length = 1800)
    private String accessTokenMetadata;

    @Column(name="access_token_type",length = 100)
    private String accessTokenType;

    @Column(name="access_token_scopes",length = 1800)
    private String accessTokenScopes;

    @Column(name="oidc_id_token_value", length = 1800)
    private String oidc_id_token_value;

    @Column(name="oidc_id_token_issued_at")
    private String oidcIdTokenIssuedAt;

    @Column(name="oidc_id_token_expires_at")
    private String oidcIdTokenExpiresAt;

    @Column(name="oidc_id_token_metadata", length = 1800)
    private String oidcIdTokenMetadata;

    @Column(name="refresh_token_value", length = 1800)
    private String refreshTokenValue;

    @Column(name="refresh_token_issued_at")
    private String refreshTokenIssuedAt;

    @Column(name="refresh_token_expires_at")
    private String refreshTokenExpiresAt;

    @Column(name="refresh_token_metadata", length = 1800)
    private String refreshTokenMetadata;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegisteredClientId() {
        return registeredClientId;
    }

    public void setRegisteredClientId(String registeredClientId) {
        this.registeredClientId = registeredClientId;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getAuthorizationGrantType() {
        return authorizationGrantType;
    }

    public void setAuthorizationGrantType(String authorizationGrantType) {
        this.authorizationGrantType = authorizationGrantType;
    }

    public String getAuthorizedScopes() {
        return authorizedScopes;
    }

    public void setAuthorizedScopes(String authorizedScopes) {
        this.authorizedScopes = authorizedScopes;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAuthorizationCodeValue() {
        return authorizationCodeValue;
    }

    public void setAuthorizationCodeValue(String authorizationCodeValue) {
        this.authorizationCodeValue = authorizationCodeValue;
    }

    public Date getAuthorizationCodeIssuedAt() {
        return authorizationCodeIssuedAt;
    }

    public void setAuthorizationCodeIssuedAt(Date authorizationCodeIssuedAt) {
        this.authorizationCodeIssuedAt = authorizationCodeIssuedAt;
    }

    public String getAuthorizationCodeExpiresAt() {
        return authorizationCodeExpiresAt;
    }

    public void setAuthorizationCodeExpiresAt(String authorizationCodeExpiresAt) {
        this.authorizationCodeExpiresAt = authorizationCodeExpiresAt;
    }

    public String getAuthorizationCodeMetadata() {
        return authorizationCodeMetadata;
    }

    public void setAuthorizationCodeMetadata(String authorizationCodeMetadata) {
        this.authorizationCodeMetadata = authorizationCodeMetadata;
    }

    public String getAccessTokenValue() {
        return accessTokenValue;
    }

    public void setAccessTokenValue(String accessTokenValue) {
        this.accessTokenValue = accessTokenValue;
    }

    public Date getAccessTokenIssuedAt() {
        return accessTokenIssuedAt;
    }

    public void setAccessTokenIssuedAt(Date accessTokenIssuedAt) {
        this.accessTokenIssuedAt = accessTokenIssuedAt;
    }

    public Date getAccessTokenExpiresAt() {
        return accessTokenExpiresAt;
    }

    public void setAccessTokenExpiresAt(Date accessTokenExpiresAt) {
        this.accessTokenExpiresAt = accessTokenExpiresAt;
    }

    public String getAccessTokenMetadata() {
        return accessTokenMetadata;
    }

    public void setAccessTokenMetadata(String accessTokenMetadata) {
        this.accessTokenMetadata = accessTokenMetadata;
    }

    public String getAccessTokenType() {
        return accessTokenType;
    }

    public void setAccessTokenType(String accessTokenType) {
        this.accessTokenType = accessTokenType;
    }

    public String getAccessTokenScopes() {
        return accessTokenScopes;
    }

    public void setAccessTokenScopes(String accessTokenScopes) {
        this.accessTokenScopes = accessTokenScopes;
    }

    public String getOidc_id_token_value() {
        return oidc_id_token_value;
    }

    public void setOidc_id_token_value(String oidc_id_token_value) {
        this.oidc_id_token_value = oidc_id_token_value;
    }

    public String getOidcIdTokenIssuedAt() {
        return oidcIdTokenIssuedAt;
    }

    public void setOidcIdTokenIssuedAt(String oidcIdTokenIssuedAt) {
        this.oidcIdTokenIssuedAt = oidcIdTokenIssuedAt;
    }

    public String getOidcIdTokenExpiresAt() {
        return oidcIdTokenExpiresAt;
    }

    public void setOidcIdTokenExpiresAt(String oidcIdTokenExpiresAt) {
        this.oidcIdTokenExpiresAt = oidcIdTokenExpiresAt;
    }

    public String getOidcIdTokenMetadata() {
        return oidcIdTokenMetadata;
    }

    public void setOidcIdTokenMetadata(String oidcIdTokenMetadata) {
        this.oidcIdTokenMetadata = oidcIdTokenMetadata;
    }

    public String getRefreshTokenValue() {
        return refreshTokenValue;
    }

    public void setRefreshTokenValue(String refreshTokenValue) {
        this.refreshTokenValue = refreshTokenValue;
    }

    public String getRefreshTokenIssuedAt() {
        return refreshTokenIssuedAt;
    }

    public void setRefreshTokenIssuedAt(String refreshTokenIssuedAt) {
        this.refreshTokenIssuedAt = refreshTokenIssuedAt;
    }

    public String getRefreshTokenExpiresAt() {
        return refreshTokenExpiresAt;
    }

    public void setRefreshTokenExpiresAt(String refreshTokenExpiresAt) {
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }

    public String getRefreshTokenMetadata() {
        return refreshTokenMetadata;
    }

    public void setRefreshTokenMetadata(String refreshTokenMetadata) {
        this.refreshTokenMetadata = refreshTokenMetadata;
    }
}
