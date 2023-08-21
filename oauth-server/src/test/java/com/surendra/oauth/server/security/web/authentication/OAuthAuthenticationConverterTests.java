package com.surendra.oauth.server.security.web.authentication;

import com.surendra.oauth.server.domain.Oauth2RegisteredClient;
import com.surendra.oauth.server.repository.Oauth2RegisteredClientRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class OAuthAuthenticationConverterTests {

    private Oauth2RegisteredClientRepository oauth2RegisteredClientRepository;
    private OAuthAuthenticationConverter oAuthAuthenticationConverter;

    /**
     * TestCase: Method used to test convert.
     * Setup: pass a HttpServletRequest request in the presence of scope.
     * Action: oAuthAuthenticationConverter.convert(requestMock) method called.
     * Expected: Verify that the convert(HttpServletRequest request)
     * method of mock object returning NotNull.
     */
    @Test
    public void testConverter_whenParameterHasScope() {
        oAuthAuthenticationConverter = new OAuthAuthenticationConverter(oauth2RegisteredClientRepository);
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(requestMock.getParameter(OAuth2ParameterNames.GRANT_TYPE)).thenReturn("client_credentials");
        AuthorizationGrantType CLIENT_CREDENTIALS = new AuthorizationGrantType("client_credentials");

        Authentication clientPrincipal = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(clientPrincipal);
        SecurityContextHolder.setContext(securityContext);

        Map<String, String[]> parameterMap = new HashMap<>();
        String[] val = new String[1];
        val[0] = "val1";
        parameterMap.put("scope", val);
        Mockito.when(requestMock.getParameterMap()).thenReturn(parameterMap);

        String scope = "scope";
        Set<String> requestedScopes = new HashSet<>(
                Arrays.asList(StringUtils.delimitedListToStringArray(scope, ",")));
        Map<String, Object> additionalParameters = new HashMap<>();
        List<String> list1 = Mockito.mock(List.class);
        additionalParameters.put("abc", list1);

        OAuth2ClientCredentialsAuthenticationToken authenticationToken = new OAuth2ClientCredentialsAuthenticationToken(clientPrincipal, requestedScopes, additionalParameters);
        assertNotNull(oAuthAuthenticationConverter.convert(requestMock));
        assertEquals(authenticationToken.getScopes().size(), 1);
    }

    /**
     * TestCase: Method used to test convert.
     * Setup: pass a HttpServletRequest request in the absence of scope.
     * Action: oAuthAuthenticationConverter.convert(requestMock) method called.
     * Expected: Verify that the convert(HttpServletRequest request)
     * method of mock object returning NotNull.
     */
    @Test
    public void testConverter_whenParameterHasNoScope() {
        oauth2RegisteredClientRepository = Mockito.mock(Oauth2RegisteredClientRepository.class);
        oAuthAuthenticationConverter = new OAuthAuthenticationConverter(oauth2RegisteredClientRepository);
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(requestMock.getParameter(OAuth2ParameterNames.GRANT_TYPE)).thenReturn("client_credentials");
        AuthorizationGrantType CLIENT_CREDENTIALS = new AuthorizationGrantType("client_credentials");

        Authentication clientPrincipal = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(clientPrincipal);
        SecurityContextHolder.setContext(securityContext);

        try (org.mockito.MockedStatic<OAuthEndpointUtils> mockedOAuthEndpointUtils = org.mockito.Mockito.mockStatic(OAuthEndpointUtils.class)) {
            MultiValueMap<String, String> mockParameters = new LinkedMultiValueMap<>();
            List<String> list1 = Mockito.mock(List.class);
            List<String> list2 = Mockito.mock(List.class);
            List<String> list3 = Mockito.mock(List.class);
            mockParameters.put("grant_type", list1);
            mockParameters.put("scope", list2);
            mockParameters.put("client_id", list3);
            mockedOAuthEndpointUtils.when(() -> OAuthEndpointUtils.getParameters(requestMock)).thenReturn(mockParameters);

            String scope = "";
            Mockito.when(mockParameters.getFirst(OAuth2ParameterNames.SCOPE)).thenReturn(scope);
            String client_id = "001";
            Mockito.when(mockParameters.getFirst(OAuth2ParameterNames.CLIENT_ID)).thenReturn(client_id);
            Oauth2RegisteredClient client = mock(Oauth2RegisteredClient.class);
            Mockito.when(oauth2RegisteredClientRepository.findByClientId(client_id)).thenReturn(client);
            Mockito.when(client.getScopes()).thenReturn("abc");
            Set<String> requestedScopes = new HashSet<>(
                    Arrays.asList(StringUtils.delimitedListToStringArray(scope, ",")));
            Map<String, Object> additionalParameters = new HashMap<>();
            additionalParameters.put("abc", list1);
            OAuth2ClientCredentialsAuthenticationToken authenticationToken= new OAuth2ClientCredentialsAuthenticationToken(clientPrincipal,requestedScopes,additionalParameters);
            assertNotNull(oAuthAuthenticationConverter.convert(requestMock));
            assertEquals(authenticationToken.getScopes().size(),0);
        }
    }

    /**
     * TestCase: Method used to test convert.
     * Setup: pass a HttpServletRequest request in the absence of scope.
     * Action: oAuthAuthenticationConverter.convert(requestMock) method called.
     * Expected: Verify that the convert(HttpServletRequest request)
     * method of mock object throw OAuth2AuthenticationException exception.
     */
    @Test
    public void testConverter_ThrowError() {
        oAuthAuthenticationConverter = new OAuthAuthenticationConverter(oauth2RegisteredClientRepository);
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(requestMock.getParameter(OAuth2ParameterNames.GRANT_TYPE)).thenReturn("client_credentials");
        AuthorizationGrantType CLIENT_CREDENTIALS = new AuthorizationGrantType("client_credentials");

        Authentication clientPrincipal = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(clientPrincipal);
        SecurityContextHolder.setContext(securityContext);

        Map<String, String[]> parameterMap = new HashMap<>();
        String[] val = new String[3];
        val[0] = "val1";
        val[1] = "val2";
        val[2] = "val3";
        parameterMap.put("scope", val);
        parameterMap.put("key2", val);
        Mockito.when(requestMock.getParameterMap()).thenReturn(parameterMap);

        assertThrows(OAuth2AuthenticationException.class, () -> {
            oAuthAuthenticationConverter.convert(requestMock);
        });
    }

    /**
     * TestCase: Method used to test convert.
     * Setup: pass a HttpServletRequest request in the presence of scope.
     * Action: oAuthAuthenticationConverter.convert(requestMock) method called.
     * Expected: Verify that the convert(HttpServletRequest request) returns Null Authentication
     */
    @Test
    public void testConverter_whenGrantTypeIsNotClientCredential() {
        oAuthAuthenticationConverter = new OAuthAuthenticationConverter(oauth2RegisteredClientRepository);
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(requestMock.getParameter(OAuth2ParameterNames.GRANT_TYPE)).thenReturn("implicit");
        assertNull(oAuthAuthenticationConverter.convert(requestMock));
    }

    /**
     * TestCase: Method used to test convert.
     * Setup: pass a HttpServletRequest request in the absence of scope.
     * Action: oAuthAuthenticationConverter.convert(requestMock) method called.
     * Expected: Verify that the convert(HttpServletRequest request) returns Authentication with zero scopes
     */
    @Test
    public void testConverter_WhenClientIDNotProvided() {
        oAuthAuthenticationConverter = new OAuthAuthenticationConverter(oauth2RegisteredClientRepository);
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        Mockito.when(requestMock.getParameter(OAuth2ParameterNames.GRANT_TYPE)).thenReturn("client_credentials");

        Authentication clientPrincipal = Mockito.mock(Authentication.class);
        Mockito.when(clientPrincipal.getPrincipal()).thenReturn("");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(clientPrincipal);
        SecurityContextHolder.setContext(securityContext);

        Map<String, String[]> parameterMap = new HashMap<>();
        String[] val = new String[1];
        val[0] = "val1";
        parameterMap.put("key2", val);
        Mockito.when(requestMock.getParameterMap()).thenReturn(parameterMap);
        Authentication authentication=oAuthAuthenticationConverter.convert(requestMock);
        assertNotNull(authentication);
        OAuth2ClientCredentialsAuthenticationToken auth=(OAuth2ClientCredentialsAuthenticationToken)authentication;
        assertEquals(0, auth.getScopes().size());
    }

}
