package com.surendra.oauth.server.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.mock;

@SpringBootTest
public class AuthEntryPointTest {

    @Autowired
    AuthEntryPoint authEntryPoint;


    AuthenticationException authenticationException = mock(AuthenticationException.class);

    /**
     * TestCase: Test for successful execution of commence method
     * SetUp: HttpServletRequest, HttpServletResponse object mocked
     * Action: void commence method
     * Expected: verified successful execution of commence method
     */
    @Test
    public void testCommence_successfulExecution() throws IOException {
        Assertions.assertDoesNotThrow(() -> {
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            PrintWriter printWriter = mock(PrintWriter.class);
            Mockito.when(response.getWriter()).thenReturn(printWriter);
            authEntryPoint.commence(request, response, authenticationException);
        });
    }

    /**
     * TestCase: Test for commence method
     * SetUp: HttpServletRequest, HttpServletResponse object mocked
     * Action: void commence method
     * Expected: Verify that IOException is thrown
     */
    @Test
    public void testCommence_unsuccessfulExecution() throws IOException {
        Assertions.assertThrows(IOException.class, () -> {
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            Mockito.when(response.getWriter()).thenThrow(IOException.class);
            authEntryPoint.commence(request, response, authenticationException);
        });
    }
}
