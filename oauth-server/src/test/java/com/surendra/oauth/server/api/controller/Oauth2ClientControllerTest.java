package com.surendra.oauth.server.api.controller;

import com.surendra.oauth.server.api.AbstractTest;
import com.surendra.oauth.server.domain.RegisteredClientDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Oauth2ClientControllerTest extends AbstractTest {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * TestCase: Method used to test getClientSetup.
     * Setup: no input passed.
     * Action: oauth2RegisteredClientService.getClientSetup() method called.
     * Expected: HTTP status code 200 is returned.
     */
    @Test
    public void testGetClientSetup() throws Exception {
        String uri = "/api/management/client/setup";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    /**
     * TestCase: Method used to test createNewClient.
     * Setup: pass a client.
     * Action: oauth2RegisteredClientService.createNewClient(client) method called.
     * Expected: HTTP status code 200 is returned.
     * ClientId and ClientName should be equal to its value.
     */
    @Test
    public void testCreateNewClient() throws Exception {
        String uri = "/api/management/client";
        RegisteredClientDTO client = new RegisteredClientDTO();
        List<String> roles = new ArrayList<>();
        roles.add("admin");
        roles.add("superadmin");
        roles.add("operator");
        client.setRoles(roles);
        client.setClientId("123");
        client.setClientName("Ginger");
        client.setClientSecret("qwerty");
        client.setOauthClientSecret("ephesoftClientSecret");
        String inputJson = super.mapToJson(client);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        String message = content.toString();
        assertEquals(content, message);
        assertEquals(client.getClientId(), "123");
        assertEquals(client.getClientName(), "Ginger");
    }

    /**
     * TestCase: Method used to test getAllRegisteredClient.
     * Setup: no input passed.
     * Action: oauth2RegisteredClientService.getAllRegisteredClient() method called.
     * Expected: HTTP status code 200 is returned.
     * length of client list should be more than 0
     * ClientId and ClientName should not be null.
     */
    @Test
    public void testGetAllRegisteredClient() throws Exception {
        String uri = "/api/management/clients";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        RegisteredClientDTO[] clientlist = super.mapFromJson(content, RegisteredClientDTO[].class);
        assertTrue(clientlist.length > 0);
        for (RegisteredClientDTO client : clientlist) {
            assertNotNull(client);
            assertNotNull(client.getClientId());
            assertNotNull(client.getClientName());
        }
    }

    /**
     * TestCase: Method used to test downloadFile.
     * Setup: pass a client Id.
     * Action: oauth2RegisteredClientService.getClientCredential(clientId) method called.
     * Expected: HTTP status code 200 is returned.
     * length of ContentAsByteArray should be 113
     * Response content type should be application/octet-stream.
     */
    @Test
    @Sql(value = "classpath:data/oauth2_registered_client1.sql")
    public void testDownloadFile() throws Exception {
        String uri = "/api/management/client/001/credentials/download";
        String inputJson = super.mapToJson("001");
        HttpHeaders headers = new HttpHeaders();
        headers.set("hostName", "http://localhost:8080/");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).headers(headers)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals("application/octet-stream", mvcResult.getResponse().getContentType());
    }

    /**
     * TestCase: Method used to test deleteRegisteredClientById.
     * Setup: pass a client Id.
     * Action: oauth2RegisteredClientService.deleteRegisteredClientById(clientId) method called.
     * Expected: Action string should be "Client id deleted successfully" .
     */
    @Test
    @Sql(value = "classpath:data/oauth2_registered_client1.sql")
    public void testDeleteRegisteredClientById() throws Exception {
        String inputJson = super.mapToJson("002");
        String uri = "/api/management/client/002";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(content, "Client id deleted successfully");
    }
}
