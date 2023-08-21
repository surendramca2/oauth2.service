package com.surendra.oauth.server.repository;

import com.surendra.oauth.server.OauthServerApplicationTestConfig;
import javax.transaction.Transactional;

import com.surendra.oauth.server.domain.Oauth2Authorization;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Oauth2AuthorizationRepositoryTest extends OauthServerApplicationTestConfig {

    @Autowired
   private Oauth2AuthorizationRepository oauth2AuthorizationRepository;

    /**
     * TestCase: Method used to test the get all the entity data list.
     * Setup: Get all the data and no input passed.
     * Action: oauth2AuthorizationRepository.findAll() method called.
     * Excepted: Oauth2Authorization result list should not be empty/null
     * and size should be greater than 0.
     */
    @Test
    public void testFindAll_ReturnListOfAllEntityData(){
        List<Oauth2Authorization> result = new ArrayList<>();
        List<Oauth2Authorization> actualDataList = new ArrayList<>();
        List<Oauth2Authorization> expectedDataList = new ArrayList<>();
        Oauth2Authorization oauth2Authorization = new Oauth2Authorization();
        oauth2Authorization.setId("466438-test-233");
        oauth2Authorization.setRegisteredClientId("jkhkjah8433-fc61-405f-8054-3ef79b3cfcc8ssss");
        oauth2Authorization.setAttributes("attribute");
        oauth2Authorization.setAccessTokenScopes("Test");
        oauth2Authorization.setState("persist");
        oauth2Authorization.setPrincipalName("ephesoft");
        oauth2Authorization.setAuthorizationGrantType("admin");
        oauth2Authorization.setAuthorizationCodeIssuedAt(new Date());
        oauth2AuthorizationRepository.save(oauth2Authorization);
        oauth2AuthorizationRepository.findAll().forEach(e -> result.add(e));
        assertTrue(result.size()>0 );
    }

    /**
     * TestCase: Method used to test the save operation in the table.
     * Setup: Created a dummy Oauth2Authorization entity data passed as parameter.
     * Action: oauth2AuthorizationRepository.save(oauth2Authorization) method called.
     * Excepted: After success save expectedDataList size should be greater than
     * actualDataList.
     */
    @Test
    public void testSave_SaveOperation(){
        List<Oauth2Authorization> actualDataList = new ArrayList<>();
        List<Oauth2Authorization> expectedDataList = new ArrayList<>();
        Oauth2Authorization oauth2Authorization = new Oauth2Authorization();
        oauth2Authorization.setId("466438-test-233");
        oauth2Authorization.setRegisteredClientId("jkhkjah8433-fc61-405f-8054-3ef79b3cfcc8ssss");
        oauth2Authorization.setAttributes("attribute");
        oauth2Authorization.setAccessTokenScopes("Test");
        oauth2Authorization.setState("persist");
        oauth2Authorization.setPrincipalName("ephesoft");
        oauth2Authorization.setAuthorizationGrantType("admin");
        oauth2Authorization.setAuthorizationCodeIssuedAt(new Date());
        oauth2AuthorizationRepository.findAll().forEach(e -> actualDataList.add(e));
        oauth2AuthorizationRepository.save(oauth2Authorization);
        oauth2AuthorizationRepository.findAll().forEach(e -> expectedDataList.add(e));
        assertTrue(expectedDataList.size()>actualDataList.size() );
    }

    /**
     * TestCase: Method used to test the save operation in the table.
     * Setup: Created a dummy Oauth2Authorization entity data passed as parameter.
     * Action: oauth2AuthorizationRepository.save(oauth2Authorization) method called.
     * Excepted: After success save expectedDataList size should be greater than
     * actualDataList.
     */
    @Test
    public void testSave_SaveOperation2(){
        List<Oauth2Authorization> actualDataList = new ArrayList<>();
        List<Oauth2Authorization> expectedDataList = new ArrayList<>();
        Oauth2Authorization oauth2Authorization = new Oauth2Authorization();
        oauth2Authorization.setId("466438-test-233333");
        oauth2Authorization.setRegisteredClientId("jkhkjah843-ddsf3-fc61-405f-8054-3ef79b3cfcc8ssss");
        oauth2Authorization.setAttributes("attribute");
        oauth2Authorization.setAccessTokenScopes("Test");
        oauth2Authorization.setState("persist");
        oauth2Authorization.setPrincipalName("ephesoft");
        oauth2Authorization.setAuthorizationGrantType("admin");
        oauth2Authorization.setAuthorizationCodeIssuedAt(new Date());
        oauth2AuthorizationRepository.findAll().forEach(e -> actualDataList.add(e));
        oauth2AuthorizationRepository.save(oauth2Authorization);
        oauth2AuthorizationRepository.findAll().forEach(e -> expectedDataList.add(e));
        assertTrue(expectedDataList.size()>actualDataList.size() );
    }
}
