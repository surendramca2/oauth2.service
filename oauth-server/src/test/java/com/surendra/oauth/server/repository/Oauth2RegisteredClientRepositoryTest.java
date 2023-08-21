package com.surendra.oauth.server.repository;

import com.surendra.oauth.server.OauthServerApplicationTestConfig;
import com.surendra.oauth.server.domain.Oauth2RegisteredClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application.properties")
public class Oauth2RegisteredClientRepositoryTest extends OauthServerApplicationTestConfig {

    @Autowired
    private Oauth2RegisteredClientRepository oauth2RegisteredClientRepository;


    /**
     * TestCase: Method used to test the get all the entity data list
     * Setup: no input passed.
     * Action: oauth2RegisteredClientRepository.findAll() method called.
     * Excepted: Oauth2RegisteredClient result list should not be empty/null
     * and size should be greater than 0.
     */
    @Test
    public void testFindAll_returnListOfAllEntityData() {
        List<Oauth2RegisteredClient> expectedDataList = new ArrayList<>();
        oauth2RegisteredClientRepository.findAll().forEach(e -> expectedDataList.add(e));
        assertTrue(expectedDataList.size() > 0);
    }

    /**
     * TestCase: Method used to test get operation in the table.
     * Setup: pass a Client Id and Client Name.
     * Action: oauth2RegisteredClientRepository.findByClientIdOrClientName(clientId,clientName) method called.
     * Expected: Should not return null if data exist in DB.
     */
    @Test
    @Sql(value = "classpath:data/oauth2_registered_client1.sql")
    public void testfindByClientIdOrClientName_getRegClientWithName() {
        Oauth2RegisteredClient oauth2RegisteredClient = oauth2RegisteredClientRepository.findByClientIdOrClientName("001", "abc");
        assertNotNull(oauth2RegisteredClient);
    }
	
    @Test
    @Sql(value = "classpath:data/oauth2_registered_client1.sql")
    public void testfindByClientIdOrClientName_getRegClientWithNameTest() {
        Oauth2RegisteredClient oauth2RegisteredClient = oauth2RegisteredClientRepository.findByClientIdOrClientName("001", "abc");
        assertNotNull(oauth2RegisteredClient);
    }
}
