package com.surendra.oauth.server.security.web.authentication;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedConstruction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.io.IOException;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.mockito.Mockito;

public class OAuthAuthenticationSuccessHandlerTests {

    @Mock
    private HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter;
    @InjectMocks
    private OAuthAuthenticationSuccessHandler oAuthAuthenticationSuccessHandler;

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * TestCase: Method used to test onAuthenticationSuccess.
     * Setup: pass a HttpServletRequest request, HttpServletResponse response, Authentication authentication.
     * Action: oAuthAuthenticationSuccessHandler.onAuthenticationSuccess(requestMock, responseMock, authentication) method called.
     * Expected: Verify that the onAuthenticationSuccess(requestMock, responseMock, authentication)
     * method of mock object by writing HttpOutputMessage in a textfile.
     */
    @Test
    public void testOnAuthenticationSuccess() throws ServletException, IOException {
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);
        MockHttpServletResponse responseMock = new MockHttpServletResponse();

        RegisteredClient client = Mockito.mock(RegisteredClient.class);
        Authentication clientPrincipal = Mockito.mock(Authentication.class);
        Set<String> scopes = new HashSet<>(Arrays.asList("scope1", "scope2"));
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "access-token",
                Instant.now(), Instant.now().plus(1, ChronoUnit.HOURS), scopes);
        Instant issuedAt = Instant.now();
        OAuth2RefreshToken refreshToken = new OAuth2RefreshToken("refresh-token", issuedAt);

        Map<String, Object> additionalParameters = new HashMap<>();
        additionalParameters.put("param1", "value1");
        additionalParameters.put("param2", "value2");

        Authentication authentication = new OAuth2AccessTokenAuthenticationToken(client, clientPrincipal, accessToken, refreshToken, additionalParameters);

        File dir = new File("src\\test\\resources");
        if (!dir.exists()) {
            dir.mkdir();
        }
        String fileName = "testFile";
        File testFile = new File(dir, fileName + ".txt");
        if (!testFile.exists()) {
            testFile.createNewFile();
        }
        OutputStream outputStream = new FileOutputStream(testFile);
        try (MockedConstruction<ServletServerHttpResponse> servletServerHttpResponseMockedConstruction = Mockito.mockConstruction(ServletServerHttpResponse.class, (mock, context) -> {
            Mockito.doReturn(outputStream).when(mock).getBody();
            Mockito.doReturn(getHttpHeaders()).when(mock).getHeaders();
        })) {
            oAuthAuthenticationSuccessHandler.onAuthenticationSuccess(requestMock, responseMock, authentication);
            String content = new Scanner(testFile).useDelimiter("\\Z").next();
            Assertions.assertTrue(StringUtils.isNotEmpty(content));
        }
    }

    private HttpHeaders getHttpHeaders() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        final List<MediaType> accept = new ArrayList<>(1);
        accept.add(MediaType.APPLICATION_OCTET_STREAM);
        httpHeaders.setAccept(accept);
        return httpHeaders;
    }
}