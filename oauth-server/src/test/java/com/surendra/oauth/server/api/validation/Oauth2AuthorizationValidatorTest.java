package com.surendra.oauth.server.api.validation;

import com.surendra.oauth.server.domain.Oauth2RegisteredClient;
import com.surendra.oauth.server.domain.RegisteredClientDTO;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Oauth2AuthorizationValidatorTest {

    @Autowired
    private Oauth2AuthorizationValidator oauth2AuthorizationValidator;

    /**
     * TestCase: Method used to test validateClient.
     * Setup: pass RegisteredClientDTO and Oauth2RegisteredClient.
     * Action: validator.validate(clientDTO) method called.
     * Expected: clientId and clientName should be equal to its value.
     */
    @Test
    public void testValidateClient_withRegisteredClient() {
        Oauth2AuthorizationValidator oauth2AuthorizationValidator = new Oauth2AuthorizationValidator();
        RegisteredClientDTO client = getClient();
        Oauth2RegisteredClient registeredClient = new Oauth2RegisteredClient();
        registeredClient.setClientSecret("qwerty");
        registeredClient.setClientSettings("settings1");
        registeredClient.setRedirectUris("uris");
        registeredClient.setClientAuthenticationMethods("Method");
        registeredClient.setTokenSettings("settings2");
        registeredClient.setScopes("scopes");
        registeredClient.setClientId("stu");
        registeredClient.setClientName("vwx");
        registeredClient.setId("1");
        registeredClient.setClientIdIssuedAt(new Date());
        Date clientSecretExpiresAt = DateUtils.addDays(new Date(), 7);
        registeredClient.setClientSecretExpiresAt(clientSecretExpiresAt);
        assertThrows(Exception.class, () -> {
            oauth2AuthorizationValidator.validateClient(client, registeredClient);
        });
        assertEquals(registeredClient.getClientId(), "stu");
        assertEquals(registeredClient.getClientName(), "vwx");
    }

    /**
     * TestCase: Method used to test validateClient.
     * Setup: pass RegisteredClientDTO and Oauth2RegisteredClient.
     * Action: validator.validate(clientDTO) method called.
     * Expected: clientId and clientName should be equal to its value.
     */
    @Test
    public void testValidateClient_withNoRegisteredClient() {
        Oauth2AuthorizationValidator oauth2AuthorizationValidator = new Oauth2AuthorizationValidator();
        RegisteredClientDTO client = getClient();
        Oauth2RegisteredClient registeredClient = mock(Oauth2RegisteredClient.class);
        oauth2AuthorizationValidator.validateClient(client, registeredClient);
        assertEquals(client.getClientId(), "456");
        assertEquals(client.getClientName(), "Garlic");
    }

    public RegisteredClientDTO getClient() {
        RegisteredClientDTO clientDTO = new RegisteredClientDTO();
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        roles.add("superadmin");
        roles.add("operator");
        clientDTO.setRoles(roles);
        clientDTO.setClientId("456");
        clientDTO.setClientName("Garlic");
        clientDTO.setClientSecret("qwerty");
        clientDTO.setOauthClientSecret("ephesoftClientSecret");
        clientDTO.setId("1");
        clientDTO.setClientIdIssuedAt(new Date().toString());
        Date secretExpirationDate = DateUtils.addDays(new Date(), 7);
        clientDTO.setSecretExpirationDate(secretExpirationDate.toString());
        clientDTO.setAccessTokenValidity(2l);
        return clientDTO;

    }
}
