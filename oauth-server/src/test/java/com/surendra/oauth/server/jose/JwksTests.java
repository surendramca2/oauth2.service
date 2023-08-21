package com.surendra.oauth.server.jose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mockito;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URISyntaxException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc


public class JwksTests {

    /**
     * TestCase: Test for successful execution of generateRsa method
     * SetUp: Resource object created
     * Action: generateRsa method
     * Expected: verified the result is not throwing exception
     */
    @Test
    public void generateRsaTest() throws URISyntaxException {
        String keyStorePassword = "ephesoft";
        String keyPassword = "ephesoft";
        Resource resource = new ClassPathResource("default-keystore.pfx");
        assertDoesNotThrow(() -> Jwks.generateRsa(resource, keyStorePassword, keyPassword));
    }

    /**
     * TestCase: Test for successful execution of generateEc method
     * SetUp: generateEc static method
     * Action: generateEc method
     * Expected: verified the result is not throwing exception
     */
    @Test
    public void generateEcTest_DoesNotThrowException() {
        assertDoesNotThrow(() -> Jwks.generateEc());
    }

    /**
     * TestCase: Test for successful execution of generateEc method
     * SetUp: generateEc static method when throwing IllegalStateException
     * Action: generateEc method
     * Expected: verified the result, throwing exception
     */
    @Test
    public void generateEcTest_ThrowException() throws InvalidAlgorithmParameterException {
        org.mockito.MockedStatic<KeyPairGenerator> mockedKeyPairGenerator = mockStatic(KeyPairGenerator.class);
        KeyPairGenerator keyPairGenerator = Mockito.mock(KeyPairGenerator.class);
        mockedKeyPairGenerator.when(() -> KeyPairGenerator.getInstance("EC")).thenReturn(keyPairGenerator);
        Mockito.when(keyPairGenerator.generateKeyPair()).thenThrow(new IllegalStateException());

        assertThrows(IllegalStateException.class, () -> {
            Jwks.generateEc();
        });
    }

    /**
     * TestCase: Test for successful execution of generateSecret method
     * SetUp: generateSecret static method
     * Action: generateSecret method
     * Expected: verified the result is not throwing exception
     */
    @Test
    public void generateSecretTest_DoesNotThrowException() {
        assertDoesNotThrow(() -> Jwks.generateSecret());
    }

}
