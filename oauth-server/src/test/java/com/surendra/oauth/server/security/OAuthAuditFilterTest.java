package com.surendra.oauth.server.security;

import com.surendra.oauth.server.api.service.Oauth2ClientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class OAuthAuditFilterTest {

    @InjectMocks
    OAuthAuditFilter oAuthAuditFilter;

    @Mock
    Oauth2ClientService oauth2ClientService;

    /**
     * TestCase: Test for successful execution of doFilterInternal method
     * SetUp: HttpServletRequest, HttpServletResponse, FilterChain object mocked
     * Action: void doFilterInternal method called
     * Expected: verified successful execution of doFilterInternal method
     */
    @Test
    public void testDoFilterInternal_WithValidInput() {
        Assertions.assertDoesNotThrow(() -> {
            HttpServletRequest request = mock(HttpServletRequest.class);
            FilterChain filterChain = mock(FilterChain.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            Mockito.when(request.getRequestURI()).thenReturn("/oauth2/token");
            oAuthAuditFilter.doFilterInternal(request, response, filterChain);
        });
    }

    /**
     * TestCase: Test for successful execution of doFilterInternal method
     * SetUp: HttpServletRequest, HttpServletResponse, FilterChain object mocked
     * Action: void doFilterInternal method called with response status 401
     * Expected: verified successful execution of doFilterInternal method
     */
    @Test
    public void testDoFilterInternal_ifAuthorized() {
        Assertions.assertDoesNotThrow(() -> {
            HttpServletRequest request = mock(HttpServletRequest.class);
            FilterChain filterChain = mock(FilterChain.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            Mockito.when(request.getRequestURI()).thenReturn("/oauth2/token");
            Mockito.when(response.getStatus()).thenReturn(Integer.valueOf("200"));
            oAuthAuditFilter.doFilterInternal(request, response, filterChain);
        });
    }

    /**
     * TestCase: Test for successful execution of doFilterInternal method
     * SetUp: HttpServletRequest, HttpServletResponse, FilterChain object mocked
     * Action: void doFilterInternal method called with response status 401
     * Expected: verified successful execution of doFilterInternal method
     */
    @Test
    public void testDoFilterInternal_ifUnauthorized() {
        Assertions.assertDoesNotThrow(() -> {
            HttpServletRequest request = mock(HttpServletRequest.class);
            FilterChain filterChain = mock(FilterChain.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            Mockito.when(request.getRequestURI()).thenReturn("/oauth2/token");
            Mockito.when(response.getStatus()).thenReturn(Integer.valueOf("401"));
            oAuthAuditFilter.doFilterInternal(request, response, filterChain);
        });
    }

    /**
     * TestCase: Test for successful execution of accessTokenRequested method
     * Action: void accessTokenRequested method called
     * Expected: verified successful execution of accessTokenRequested method
     */
    @Test
    public void testAccessTokenRequested_WithValidInputs() {
        Assertions.assertDoesNotThrow(() -> {
            oAuthAuditFilter.accessTokenRequested("1", "test");
        });
    }

    /**
     * TestCase: Test for successful execution of accessTokenResponseSucceeded method
     * Action: void accessTokenResponseSucceeded method called
     * Expected: verified successful execution of accessTokenResponseSucceeded method
     */
    @Test
    public void testAccessTokenResponseSucceeded_WithValidInputs() {
        Assertions.assertDoesNotThrow(() -> {
            oAuthAuditFilter.accessTokenResponseSucceeded("1", "test");
        });
    }

    /**
     * TestCase: Test for successful execution of accessTokenResponseAccessDenied method
     * Action: void accessTokenResponseAccessDenied method called
     * Expected: verified successful execution of accessTokenResponseAccessDenied method
     */
    @Test
    public void testAccessTokenResponseAccessDenied_WithValidInputs() {
        Assertions.assertDoesNotThrow(() -> {
            oAuthAuditFilter.accessTokenResponseAccessDenied("1", "test");
        });
    }

    /**
     * TestCase: Test for successful execution of accessTokenResponseFailed method
     * Action: void accessTokenResponseFailed method called
     * Expected: verified successful execution of accessTokenResponseFailed method
     */
    @Test
    public void testAccessTokenResponseFailed_WithValidInputs() {
        Assertions.assertDoesNotThrow(() -> {
            oAuthAuditFilter.accessTokenResponseFailed("1", "test");
        });
    }

    /**
     * TestCase: Test for successful execution of getClientIdFromRequest method
     * SetUp: HttpServletRequest mocked
     * Action:  getClientIdFromRequest method called
     * Expected: verified actual argument is equal to expected argument
     */
    @Test
    public void testGetClientIdFromRequest_WithValidInputs() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getParameter(OAuth2ParameterNames.CLIENT_ID)).thenReturn("1");
        String clientIdFromRequest = oAuthAuditFilter.getClientIdFromRequest(request);
        Assertions.assertEquals(clientIdFromRequest, "1");
    }

    /**
     * TestCase: Test for successful execution of getClientIdFromRequest method with Invalid input
     * SetUp: HttpServletRequest mocked
     * Action:  getClientIdFromRequest method called
     * Expected: verified clientIdFromRequest should be null
     */
    @Test
    public void testGetClientIdFromRequest_WithInValidInputs() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String clientIdFromRequest = oAuthAuditFilter.getClientIdFromRequest(request);
        Assertions.assertNull(clientIdFromRequest);
    }

    /**
     * TestCase: Test for successful execution of getScopesFromRequest method with valid input
     * SetUp: HttpServletRequest mocked
     * Action:  getClientIdFromRequest method called
     * Expected: verified actual argument is equal to expected argument
     */
    @Test
    public void testGetScopesFromRequest_WithValidInputs() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String clientIdFromRequest = oAuthAuditFilter.getScopesFromRequest(request);
        Assertions.assertEquals(clientIdFromRequest, "");
    }

    /**
     * TestCase: Test for successful execution of doFilterInternal method
     * SetUp: HttpServletRequest, HttpServletResponse, FilterChain object mocked and invalid End point
     * Action: void doFilterInternal method called with response status 401
     * Expected: verified successful execution of doFilterInternal method
     */
    @Test
    public void testDoFilterInternal_WhenEndpointIsInvalid() {
        Assertions.assertDoesNotThrow(() -> {
            HttpServletRequest request = mock(HttpServletRequest.class);
            FilterChain filterChain = mock(FilterChain.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            Mockito.when(request.getRequestURI()).thenReturn("/url/token");
            oAuthAuditFilter.doFilterInternal(request, response, filterChain);
        });
    }

    /**
     * TestCase: Test for successful execution of doFilterInternal method
     * SetUp: HttpServletRequest, HttpServletResponse, FilterChain object mocked
     * Action: void doFilterInternal method called with response status 403
     * Expected: verified successful execution of doFilterInternal method
     */
    @Test
    public void testDoFilterInternal_WhenStatusIsForbidden() {
        Assertions.assertDoesNotThrow(() -> {
            HttpServletRequest request = mock(HttpServletRequest.class);
            FilterChain filterChain = mock(FilterChain.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            Mockito.when(request.getRequestURI()).thenReturn("/oauth2/token");
            Mockito.when(response.getStatus()).thenReturn(Integer.valueOf("403"));
            oAuthAuditFilter.doFilterInternal(request, response, filterChain);
        });
    }

    /**
     * TestCase: Test for successful execution of doFilterInternal method
     * SetUp: HttpServletRequest, HttpServletResponse, FilterChain object mocked and valid scope in request
     * Action: void doFilterInternal method called with response status 200
     * Expected: verified successful execution of doFilterInternal method
     */
    @Test
    public void testDoFilterInternal_WhenAuthorizedWithValidScope() {
        Assertions.assertDoesNotThrow(() -> {
            HttpServletRequest request = mock(HttpServletRequest.class);
            FilterChain filterChain = mock(FilterChain.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            Mockito.when(request.getRequestURI()).thenReturn("/oauth2/token");
            Mockito.when(response.getStatus()).thenReturn(Integer.valueOf("200"));
            Mockito.when(request.getParameter(OAuth2ParameterNames.SCOPE)).thenReturn("scope");
            Mockito.when(request.getParameter(OAuth2ParameterNames.CLIENT_ID)).thenReturn("clientId");
            oAuthAuditFilter.doFilterInternal(request, response, filterChain);
        });
    }

}
