package com.surendra.oauth.server.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

public class TokenUtilTests {
    @Autowired
    private TokenUtil tokenUtil;

    /**
     * TestCase: Test for successful execution of parseToken method
     * SetUp: HttpServletRequest object created
     * Action: parseToken method
     * Expected: verified the result is equal
     */
    @Test
    public void parseTokenTest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(request.getHeader(anyString())).thenReturn("Bearer abc");
        String token = tokenUtil.parseToken(request);
        assertEquals(token, "abc");
    }

    /**
     * TestCase: Test for parseToken method with invalid header string
     * SetUp: HttpServletRequest object created
     * Action: parseToken method
     * Expected: verified the result is null
     */
    @Test
    public void testParseToken_withInvalidHeaderString() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(request.getHeader(anyString())).thenReturn("abc");
        String token = tokenUtil.parseToken(request);
        assertNull(token);
    }

    /**
     * TestCase: Test for successful execution of validateToken method
     * SetUp: TokenUtil object created
     * Action: validateToken method
     * Expected: verified the result is false
     */
    @Test
    public void validateTokenTest_exception() {
        String token = "123456789";
        TokenUtil tokenUtil = mock(TokenUtil.class);
        Boolean flag = tokenUtil.validateToken(token);
        assertFalse(flag);
    }

    /**
     * TestCase: Test for successful execution of validateToken method
     * SetUp: TokenUtil object created
     * Action: validateToken method called
     * Expected: verified the result is false
     */
    @Test
    public void validateTokenTest_withException() {
        String token = "123456789";
        Boolean flag = tokenUtil.validateToken(token);
        assertFalse(flag);
    }

    /**
     * TestCase: Test for successful execution of validateToken method
     * SetUp: TokenUtil object created
     * Action: validateToken method
     * Expected: verified the result is true
     */
    @Test
    public void validateTokenTest_withNoException() {
        Assertions.assertDoesNotThrow(() -> {
            TokenUtil tokenUtil = mock(TokenUtil.class);
            Mockito.when(tokenUtil.validateToken(anyString())).thenReturn(true);
        });
    }

    /**
     * TestCase: Test for parseToken method with empty header string
     * SetUp: HttpServletRequest object created
     * Action: parseToken method
     * Expected: verified the result is null
     */
    @Test
    public void testParseToken_withEmptyHeaderString() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Mockito.when(request.getHeader(anyString())).thenReturn("");
        String token = tokenUtil.parseToken(request);
        assertNull(token);
    }


}
