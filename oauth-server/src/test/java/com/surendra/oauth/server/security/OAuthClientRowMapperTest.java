package com.surendra.oauth.server.security;

import com.surendra.oauth.server.security.authorization.client.OAuthClientRowMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Timestamp;


@ExtendWith(MockitoExtension.class)
public class OAuthClientRowMapperTest {

    @InjectMocks
    OAuthClientRowMapper oAuthClientRowMapper;

    @Mock
    ResultSet testResultSet;

    /**
     * TestCase: to test mapRow with valid resultset parameter.
     * Setup: pass mocked resultset object.
     * Action: mapRow method called.
     * Expected: Successful execution of method and registered client obj should not be null.
     */
    @Test
    public void testMapRow_withValidResultset() {
        Assertions.assertDoesNotThrow(() -> {
            Mockito.when(testResultSet.getTimestamp("client_id_issued_at")).thenReturn(new Timestamp(67));
            Mockito.when(testResultSet.getTimestamp("client_secret_expires_at")).thenReturn(new Timestamp(67));
            Mockito.when(testResultSet.getString("client_authentication_methods")).thenReturn("test");
            Mockito.when(testResultSet.getString("authorization_grant_types")).thenReturn("test");
            Mockito.when(testResultSet.getString("redirect_uris")).thenReturn("test");
            Mockito.when(testResultSet.getString("scopes")).thenReturn("test");
            Mockito.when(testResultSet.getString("id")).thenReturn("test");
            Mockito.when(testResultSet.getString("client_id")).thenReturn("test");
            Mockito.when(testResultSet.getString("client_secret")).thenReturn("test");
            Mockito.when(testResultSet.getString("client_name")).thenReturn("test");
            Mockito.when(testResultSet.getString("client_settings")).thenReturn("{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-authorization-consent\":false,\"oauth.client-secret\":\"test\",\"settings.client.require-proof-key\":false}");
            Mockito.when(testResultSet.getString("token_settings")).thenReturn("{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-authorization-consent\":false,\"oauth.client-secret\":\"test\",\"settings.client.require-proof-key\":false}");

            RegisteredClient registeredClient = oAuthClientRowMapper.mapRow(testResultSet, 0);
            Assertions.assertNotNull(registeredClient);
        });

    }

    /**
     * TestCase: to test resolveAuthorizationGrantType with authorization_code as arg.
     * Setup: pass authorization_code as object.
     * Action: resolveAuthorizationGrantType method called.
     * Expected: authorizationGrantType obj should be equal to actual authorization code.
     */
    @Test
    public void testResolveAuthorizationGrantType_withAuthorizationCode() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method privateMethod = oAuthClientRowMapper.getClass().getDeclaredMethod("resolveAuthorizationGrantType",
                String.class);
        privateMethod.setAccessible(true);
        AuthorizationGrantType authorizationGrantType = new AuthorizationGrantType("authorization_code");
        Object authorization_code = privateMethod.invoke(oAuthClientRowMapper, "authorization_code");
        Assertions.assertEquals(authorization_code, authorizationGrantType);
    }

    /**
     * TestCase: to test resolveAuthorizationGrantType with client_credentials as arg.
     * Setup: pass client_credentials as object.
     * Action: resolveAuthorizationGrantType method called.
     * Expected: authorizationGrantType obj should be equal to actual client_credentials.
     */
    @Test
    public void testResolveAuthorizationGrantType_withClientCredentials() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method privateMethod = oAuthClientRowMapper.getClass().getDeclaredMethod("resolveAuthorizationGrantType",
                String.class);
        privateMethod.setAccessible(true);
        AuthorizationGrantType authorizationGrantType = new AuthorizationGrantType("client_credentials");
        Object client_credentials = privateMethod.invoke(oAuthClientRowMapper, "client_credentials");
        Assertions.assertEquals(client_credentials, authorizationGrantType);
    }

    /**
     * TestCase: to test resolveAuthorizationGrantType with refresh_token as arg.
     * Setup: pass refresh_token as object.
     * Action: resolveAuthorizationGrantType method called.
     * Expected: authorizationGrantType obj should be equal to actual refresh_token.
     */
    @Test
    public void testResolveAuthorizationGrantType_withRefreshTokens() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method privateMethod = oAuthClientRowMapper.getClass().getDeclaredMethod("resolveAuthorizationGrantType",
                String.class);
        privateMethod.setAccessible(true);
        AuthorizationGrantType authorizationGrantType = new AuthorizationGrantType("refresh_token");
        Object refreshToken = privateMethod.invoke(oAuthClientRowMapper, "refresh_token");
        Assertions.assertEquals(refreshToken, authorizationGrantType);
    }


    /**
     * TestCase: to test resolveClientAuthenticationMethod with client_secret_basic as arg.
     * Setup: pass client_secret_basic as object
     * Action: resolveClientAuthenticationMethod method called.
     * Expected: clientAuthenticationMethod obj should be equal to actual clientSecretBasic.
     */
    @Test
    public void testresolveClientAuthenticationMethod_withClientSecretBasic() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method privateMethod = oAuthClientRowMapper.getClass().getDeclaredMethod("resolveClientAuthenticationMethod",
                String.class);
        privateMethod.setAccessible(true);
        ClientAuthenticationMethod clientAuthenticationMethod = new ClientAuthenticationMethod("client_secret_basic");
        Object clientSecretBasic = privateMethod.invoke(oAuthClientRowMapper, "client_secret_basic");
        Assertions.assertEquals(clientSecretBasic, clientAuthenticationMethod);
    }

    /**
     * TestCase: to test resolveClientAuthenticationMethod with client_secret_post as arg.
     * Setup: pass client_secret_post as object
     * Action: resolveClientAuthenticationMethod method called.
     * Expected: ClientAuthenticationMethod obj should be equal to actual client_secret_post.
     */
    @Test
    public void testresolveClientAuthenticationMethod_withClientSecretPost() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method privateMethod = oAuthClientRowMapper.getClass().getDeclaredMethod("resolveClientAuthenticationMethod",
                String.class);
        privateMethod.setAccessible(true);
        ClientAuthenticationMethod ClientAuthenticationMethod = new ClientAuthenticationMethod("client_secret_post");
        Object clientSecretPost = privateMethod.invoke(oAuthClientRowMapper, "client_secret_post");
        Assertions.assertEquals(clientSecretPost, ClientAuthenticationMethod);
    }

    /**
     * TestCase: to test resolveClientAuthenticationMethod with none as arg.
     * Setup: pass none as object
     * Action: resolveClientAuthenticationMethod method called.
     * Expected: ClientAuthenticationMethod obj should be equal to actual none.
     */
    @Test
    public void testresolveClientAuthenticationMethod_withNoneAsParameter() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method privateMethod = oAuthClientRowMapper.getClass().getDeclaredMethod("resolveClientAuthenticationMethod",
                String.class);
        privateMethod.setAccessible(true);
        ClientAuthenticationMethod ClientAuthenticationMethod = new ClientAuthenticationMethod("none");
        Object authorization_code = privateMethod.invoke(oAuthClientRowMapper, "none");
        Assertions.assertEquals(authorization_code, ClientAuthenticationMethod);
    }

    /**
     * TestCase: to test parseMap with invalidJsonString.
     * Setup: pass invalid Json obj
     * Action: parseMap method called.
     * Expected: Should throws InvocationTargetException.
     */
    @Test
    public void testParseMap_withInvalidParameter() {
        Assertions.assertThrows(InvocationTargetException.class, () -> {
            Method privateMethod = oAuthClientRowMapper.getClass().getDeclaredMethod("parseMap",
                    String.class);
            privateMethod.setAccessible(true);
            privateMethod.invoke(oAuthClientRowMapper, "test");
        });
    }

}
