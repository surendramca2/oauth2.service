package com.surendra.oauth.server.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {

    @InjectMocks
    AuthTokenFilter authTokenFilter;

    @Mock
    TokenUtil tokenUtil;

    @Mock
    AuthEntryPoint authEntryPoint;

    /**
     * TestCase: Method used to test doFilterInternal with valid token string.
     * Setup: pass request and response.
     * Action: doFilterInternal method called.
     * Expected: Method execution successful without any error.
     */
    @Test
    public void testDoFilterInternal_WithValidTokenString() {
        Assertions.assertDoesNotThrow(() -> {
            HttpServletRequest request = mock(HttpServletRequest.class);
            FilterChain filterChain = mock(FilterChain.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            Mockito.when(tokenUtil.parseToken(request)).thenReturn("test");
            Mockito.when(tokenUtil.validateToken("test")).thenReturn(true);
            authTokenFilter.doFilterInternal(request, response, filterChain);
        });
    }

    /**
     * TestCase: Method used to test doFilterInternal with empty token string.
     * Setup: pass request and response.
     * Action: doFilterInternal method called.
     * Expected: Method execution successful without any error.
     */
    @Test
    public void testDoFilterInternal_WithEmptyTokenString() {
        Assertions.assertDoesNotThrow(() -> {
            HttpServletRequest request = mock(HttpServletRequest.class);
            FilterChain filterChain = mock(FilterChain.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            Mockito.when(tokenUtil.parseToken(request)).thenReturn("");
            authTokenFilter.doFilterInternal(request, response, filterChain);
        });
    }

    /**
     * TestCase: Method used to test shouldNotFilter with invalid parameter.
     * Setup: pass request object.
     * Action: shouldNotFilter method called.
     * Expected: Request should not match and returns false.
     */
    @Test
    public void testShouldNotFilter_withInvalidArguments() throws ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        try (MockedConstruction<NegatedRequestMatcher> mockedNegatedRequestMatcherConstruction = Mockito.mockConstruction(NegatedRequestMatcher.class, (mock, context) -> {
            Mockito.doReturn(false).when(mock).matches(request);
        })) {
            boolean matcher = authTokenFilter.shouldNotFilter(request);
            Assertions.assertFalse(matcher);
        }
    }

    /**
     * TestCase: Method used to test shouldNotFilter with valid parameter.
     * Setup: pass request object.
     * Action: shouldNotFilter method called.
     * Expected: Request should be match and returns true.
     */
    @Test
    public void testShouldNotFilter_withValidArguments() throws ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        try (MockedConstruction<NegatedRequestMatcher> mockedNegatedRequestMatcherConstruction = Mockito.mockConstruction(NegatedRequestMatcher.class, (mock, context) -> {
            Mockito.doReturn(true).when(mock).matches(request);
        })) {
            boolean matcher = authTokenFilter.shouldNotFilter(request);
            Assertions.assertTrue(matcher);
        }
    }

    /**
     * TestCase: Method used to test doFilterInternal with empty token string.
     * Setup: pass request and response.
     * Action: doFilterInternal method called.
     * Expected: Method execution successful without any error.
     */
    @Test
    public void testDoFilterInternal_WithExceptionHandled() {
        Assertions.assertDoesNotThrow(() -> {
            HttpServletRequest request = mock(HttpServletRequest.class);
            FilterChain filterChain = mock(FilterChain.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            Mockito.doThrow(new NullPointerException()).when(authEntryPoint).commence(Mockito.any(),Mockito.any(),Mockito.any());
            authTokenFilter.doFilterInternal(request, response, filterChain);
        });
    }

}
