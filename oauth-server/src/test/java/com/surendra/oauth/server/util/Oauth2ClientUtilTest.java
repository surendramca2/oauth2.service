package com.surendra.oauth.server.util;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.surendra.oauth.server.domain.Oauth2RegisteredClient;
import org.mockito.Mockito;
import java.time.Instant;

import org.junit.jupiter.api.Test;

public class Oauth2ClientUtilTest  {

  /**
   * TestCase: Method used to test getDate.
   * Setup: No of days passed.
   * Action: Oauth2ClientUtil.getDate() method called.
   * Expected: method returns the Instance object which is days after equal to passed arguments.
   */
  @Test
  public void testGetDate_WhenDaysDefinedInThePropertiesFile() {
    Instant now=Instant.now();
    Instant date = Oauth2ClientUtil.getDate("10");
    assertTrue(date.isAfter(now));
  }
  /**
   * TestCase: Method used to test getClientCredentialDataBytes.
   * Setup: Mocked Oauth2RegisteredClient and null string passed as input.
   * Action: Oauth2ClientUtil.getClientCredentialDataBytes() method called.
   * Expected: method Throws the IllegalArgumentException.
   */
  @Test
  public void testGetClientCredentialDataBytes_WhenClientSettingsISNull() {
    Oauth2RegisteredClient oauth2RegisteredClient=Mockito.mock(Oauth2RegisteredClient.class);
    Mockito.when(oauth2RegisteredClient.getScopes()).thenReturn("scope1,scope2");
    Mockito.when(oauth2RegisteredClient.getClientSettings()).thenReturn(null);
    assertThrows(IllegalArgumentException.class,()->Oauth2ClientUtil.getClientCredentialDataBytes(oauth2RegisteredClient,null));
  }
}
