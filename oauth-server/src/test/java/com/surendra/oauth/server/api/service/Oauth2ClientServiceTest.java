package com.surendra.oauth.server.api.service;

import com.surendra.oauth.server.api.AbstractTest;
import com.surendra.oauth.server.api.validation.Oauth2AuthorizationValidator;
import com.surendra.oauth.server.domain.Oauth2Authorization;
import com.surendra.oauth.server.domain.Oauth2RegisteredClient;
import com.surendra.oauth.server.domain.RegisteredClientDTO;
import com.surendra.oauth.server.exception.CryptographyException;
import com.surendra.oauth.server.repository.Oauth2AuthorizationRepository;
import com.surendra.oauth.server.repository.Oauth2RegisteredClientRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class Oauth2ClientServiceTest extends AbstractTest {

    @Autowired
    private Oauth2ClientService oauth2RegisteredClientService;

    @Spy
    private Oauth2RegisteredClientRepository clientRepository;
    @Mock
    private ApplicationContext appContext;
    @Mock
    private Oauth2AuthorizationValidator oauth2AuthorizationValidator;
    @Spy
    private Oauth2AuthorizationRepository oauth2AuthorizationRepository;

    @InjectMocks private Oauth2ClientService oAuthService;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * TestCase: Method used to test getClientSetup.
     * Setup: no input passed.
     * Action: oauth2RegisteredClientService.getClientSetup() method called.
     * Expected: clientId and clientSecret should be equal to its value.
     */
    @Test
    public void testGetClientSetup() throws CryptographyException {
        Oauth2ClientService oauth2RegisteredClientService1 = Mockito.spy(oauth2RegisteredClientService);
        RegisteredClientDTO client = new RegisteredClientDTO();
        client.setClientId("001");
        client.setClientSecret("qwerty");
        oauth2RegisteredClientService1.getClientSetup();
        verify(oauth2RegisteredClientService1, times(1)).getClientSetup();
        assertEquals(client.getClientId(), "001");
        assertEquals(client.getClientSecret(), "qwerty");
    }

    /**
     * TestCase: Method used to test getAllRegisteredClient.
     * Setup: no input passed.
     * Action: oauth2RegisteredClientService.getAllRegisteredClient() method called.
     * Expected: Verify that the getAllRegisteredClient() method of mock object was called only once.
     */
    @Test
    public void testGetAllRegisteredClient() {
        Oauth2ClientService oauth2RegisteredClientService1 = Mockito.spy(oauth2RegisteredClientService);
        List<Oauth2RegisteredClient> oauth2RegisteredClients = mock(List.class);
        Oauth2RegisteredClientRepository clientRepository = mock(Oauth2RegisteredClientRepository.class);
        Mockito.when(clientRepository.findAll()).thenReturn(oauth2RegisteredClients);
        oauth2RegisteredClientService1.getAllRegisteredClient();
        verify(oauth2RegisteredClientService1, times(1)).getAllRegisteredClient();
    }

    /**
     * TestCase: Method used to test createNewClient.
     * Setup: pass a client.
     * Action: oauth2RegisteredClientService.createNewClient(client) method called.
     * Expected: ClientId and ClientName should be equal to its value.
     */
    @Test
    public void testCreateNewClient() {
        Oauth2ClientService oauth2RegisteredClientService1 = Mockito.spy(oauth2RegisteredClientService);
        RegisteredClientDTO client = new RegisteredClientDTO();
        client.setClientId("001");
        client.setClientName("Turmeric");
        client.setClientSecret("qwerty");
        client.setOauthClientSecret("ephesoftClientSecret");
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        roles.add("superadmin");
        roles.add("operator");
        client.setRoles(roles);
        oauth2RegisteredClientService1.createNewClient(client);
        verify(oauth2RegisteredClientService1, times(1)).createNewClient(client);
        assertEquals(client.getClientId(), "001");
        assertEquals(client.getClientName(), "Turmeric");
    }

    /**
     * TestCase: Method used to test deleteRegisteredClientById.
     * Setup: pass a registered client Id.
     * Action: oauth2RegisteredClientService.deleteRegisteredClientById(clientId) method called.
     * Expected: Verify that the deleteRegisteredClientById(String clientId)
     * method of mock object was called only once. .
     */
    @Test
    @Sql(value = "classpath:data/oauth2_registered_client1.sql")
    public void testDeleteRegisteredClientById_withRegisteredClient() {
        Oauth2ClientService oauth2RegisteredClientService1 = Mockito.spy(oauth2RegisteredClientService);
        String clientId = "001";
        Oauth2RegisteredClient registeredClient = mock(Oauth2RegisteredClient.class);
        Oauth2RegisteredClientRepository clientRepository = mock(Oauth2RegisteredClientRepository.class);
        Mockito.when(clientRepository.findByClientId(clientId)).thenReturn(registeredClient);
        oauth2RegisteredClientService1.deleteRegisteredClientById(clientId);
        verify(oauth2RegisteredClientService1, times(1)).deleteRegisteredClientById(clientId);
    }

    /**
     * TestCase: Method used to test deleteRegisteredClientById.
     * Setup: pass a not registered client Id.
     * Action: oauth2RegisteredClientService.deleteRegisteredClientById(clientId) method called.
     * Expected: Verify that the deleteRegisteredClientById(String clientId)
     * method of mock object was called only once. .
     */
    @Test
    public void testDeleteRegisteredClientById_withNoRegisteredClient() {
        Oauth2ClientService oauth2RegisteredClientService1 = Mockito.spy(oauth2RegisteredClientService);
        String clientId = "004";
        Oauth2RegisteredClient registeredClient = mock(Oauth2RegisteredClient.class);
        Oauth2RegisteredClientRepository clientRepository = mock(Oauth2RegisteredClientRepository.class);
        Mockito.when(clientRepository.findByClientId(clientId)).thenReturn(registeredClient);
        assertThrows(Exception.class, () -> {
            oauth2RegisteredClientService1.deleteRegisteredClientById(clientId);
        });
        verify(oauth2RegisteredClientService1, times(1)).deleteRegisteredClientById(clientId);
    }

    /**
     * TestCase: Method used to test getClientCredential.
     * Setup: pass a registered client Id.
     * Action: clientRepository.findByClientId(clientId) method called.
     * Expected: Verify that the getClientCredential(clientId)
     * method of mock object was called only once. .
     */
    @Test
    @Sql(value = "classpath:data/oauth2_registered_client1.sql")
    public void testGetClientCredential_withRegisteredClient() throws Exception {
        Oauth2ClientService oauth2RegisteredClientService1 = Mockito.spy(oauth2RegisteredClientService);
        String clientId = "002";
        Oauth2RegisteredClient registeredClient = mock(Oauth2RegisteredClient.class);
        Oauth2RegisteredClientRepository clientRepository = mock(Oauth2RegisteredClientRepository.class);
        Mockito.when(clientRepository.findByClientId(clientId)).thenReturn(registeredClient);
        oauth2RegisteredClientService1.getRegisteredClient(clientId);
        verify(oauth2RegisteredClientService1, times(1)).getRegisteredClient(clientId);
    }

    /**
     * TestCase: Method used to test getClientCredential.
     * Setup: pass a not registered client Id.
     * Action: clientRepository.findByClientId(clientId) method called.
     * Expected: Verify that the getClientCredential(clientId)
     * method of mock object was called only once. .
     */
    @Test
    @Sql(value = "classpath:data/oauth2_registered_client1.sql")
    public void testGetClientCredential_withNoRegisteredClient() throws Exception {
        Oauth2ClientService oauth2RegisteredClientService1 = Mockito.spy(oauth2RegisteredClientService);
        String clientId = "004";
        Oauth2RegisteredClient registeredClient = mock(Oauth2RegisteredClient.class);
        Oauth2RegisteredClientRepository clientRepository = mock(Oauth2RegisteredClientRepository.class);
        Mockito.when(clientRepository.findByClientId(clientId)).thenReturn(registeredClient);
        assertThrows(Exception.class, () -> {
            oauth2RegisteredClientService1.getRegisteredClient(clientId);
        });
        verify(oauth2RegisteredClientService1, times(1)).getRegisteredClient(clientId);
    }

    /**
     * TestCase: Method used to test getLastTokenIdByClientId.
     * Setup: pass registered client Id.
     * Action: oauth2RegisteredClientService1.getLastTokenIdByClientId(clientId) method called.
     * Expected: Verify that the getLastTokenIdByClientId(clientId)
     * method of mock object was returning lastToken.getId().
     */
    @Test
    @Sql(value = "classpath:data/oauth2_registered_client1.sql")
    @Sql(value = "classpath:data/oauth2_authorization.sql")
    public void testGetLastTokenIdByClientId() {
        Oauth2ClientService oauth2RegisteredClientService1 = Mockito.spy(oauth2RegisteredClientService);
        String clientId = "001";
        Oauth2RegisteredClient registeredClient = new Oauth2RegisteredClient();
        registeredClient.setClientSecret("qwerty");
        registeredClient.setClientSettings("settings1");
        registeredClient.setRedirectUris("uris");
        registeredClient.setClientAuthenticationMethods("Method");
        registeredClient.setTokenSettings("settings2");
        registeredClient.setScopes("scopes");
        registeredClient.setClientId("001");
        registeredClient.setClientName("vwx");
        registeredClient.setId("1");
        registeredClient.setClientIdIssuedAt(new Date());
        Date clientSecretExpiresAt = DateUtils.addDays(new Date(), 7);
        registeredClient.setClientSecretExpiresAt(clientSecretExpiresAt);
        Oauth2Authorization lastToken = new Oauth2Authorization();
        lastToken.setId("1");
        lastToken.setRegisteredClientId("1");
        lastToken.setPrincipalName("");
        lastToken.setAuthorizationGrantType("");
        String token = oauth2RegisteredClientService1.getLastTokenIdByClientId(clientId);
        assertEquals(lastToken.getId(), token);
    }
    /**
     * TestCase: Method used to test createNewClient.
     * Setup: pass a client.
     * Action: oauth2RegisteredClientService.createNewClient(client) method called.
     * Expected: Verify that the createNewClient throws ResponseStatusException
     */
    @Test
    public void testCreateNewClient_WhenCreateNewClientThrowsException() {
        JdbcRegisteredClientRepository registeredClientRepository=Mockito.mock(JdbcRegisteredClientRepository.class);
        appContext.getBean(JdbcRegisteredClientRepository.class);
        Mockito.when(appContext.getBean(JdbcRegisteredClientRepository.class)).thenReturn(registeredClientRepository);
        Mockito.doThrow(new IllegalArgumentException()).when(registeredClientRepository).save(Mockito.any());
        RegisteredClientDTO client = new RegisteredClientDTO();
        client.setClientId("001");
        client.setClientName("Turmeric");
        client.setClientSecret("qwerty");
        client.setOauthClientSecret("ephesoftClientSecret");
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        roles.add("superadmin");
        roles.add("operator");
        client.setRoles(roles);
        ReflectionTestUtils.setField(oAuthService, "accessTokenValidity", "60");
        ReflectionTestUtils.setField(oAuthService, "daysDefinedInThePropertiesFile", "10");
        assertThrows(ResponseStatusException.class,()->oAuthService.createNewClient(client));
    }

    /**
     * TestCase: Method used to test deleteRegisteredClientById.
     * Setup: pass a registered client Id.
     * Action: oauth2RegisteredClientService.deleteRegisteredClientById(clientId) method called.
     * Expected: Verify that the deleteRegisteredClientById(String clientId) throws ResponseStatusException
     */
    @Test
    public void testDeleteRegisteredClientById_WhenDeleteThrowsException() {
        String clientId = "001";
        Oauth2RegisteredClient registeredClient = mock(Oauth2RegisteredClient.class);
        Mockito.when(registeredClient.getId()).thenReturn(clientId);
        Mockito.when(clientRepository.findByClientId(clientId)).thenReturn(registeredClient);
       Mockito.doThrow(new NullPointerException()).when(clientRepository).deleteById(clientId);
        assertThrows(ResponseStatusException.class,()->oAuthService.deleteRegisteredClientById(clientId));
    }

    /**
     * TestCase: Method used to test getLastTokenIdByClientId.
     * Setup: pass a client.
     * Action: oauth2RegisteredClientService.getLastTokenIdByClientId(client) method called.
     * Expected: Verify that the returned lastToken is Null
     */
    @Test
    public void testGetLastTokenIdByClientId_whenLastTokenIsNull() {
        JdbcRegisteredClientRepository registeredClientRepository=Mockito.mock(JdbcRegisteredClientRepository.class);
        appContext.getBean(JdbcRegisteredClientRepository.class);
        Mockito.when(appContext.getBean(JdbcRegisteredClientRepository.class)).thenReturn(registeredClientRepository);
        Mockito.doThrow(new IllegalArgumentException()).when(registeredClientRepository).save(Mockito.any());
        RegisteredClientDTO client = new RegisteredClientDTO();
        client.setId("1");
        client.setClientId("001");
        client.setClientName("Turmeric");
        client.setClientSecret("qwerty");
        client.setOauthClientSecret("ephesoftClientSecret");
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        roles.add("superadmin");
        roles.add("operator");
        client.setRoles(roles);
        ReflectionTestUtils.setField(oAuthService, "accessTokenValidity", "60");
        ReflectionTestUtils.setField(oAuthService, "daysDefinedInThePropertiesFile", "10");
        Oauth2RegisteredClient registeredClient = mock(Oauth2RegisteredClient.class);
        Mockito.when(registeredClient.getId()).thenReturn("001");
        Mockito.doReturn(registeredClient).when(clientRepository).findByClientId(Mockito.any());

        Mockito.doReturn(null).when(oauth2AuthorizationRepository).
            findFirstByRegisteredClientIdOrderByAccessTokenIssuedAtDesc(Mockito.any());
        String lastTokenId=oAuthService.getLastTokenIdByClientId(client.getClientId());
        Assertions.assertNull(lastTokenId);
    }


}

